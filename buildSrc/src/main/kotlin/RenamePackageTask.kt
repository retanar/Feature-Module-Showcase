import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

abstract class RenamePackageTask : DefaultTask() {
    @Input
    @Option(
        option = "from",
        description = "Name of the module to be renamed.",
    )
    lateinit var fromPackageName: String

    @Input
    @Option(
        option = "to",
        description = "Name of the module to rename previous module to.",
    )
    lateinit var toPackageName: String

    @Input
    var folderBlockList = hashSetOf(".idea", ".gradle", "build", ".git")

    @TaskAction
    fun execute() {
        when {
            !::fromPackageName.isInitialized || fromPackageName.isBlank() ->
                throw IllegalArgumentException("'--from <package>' argument should be provided.")

            !::toPackageName.isInitialized || toPackageName.isBlank() ->
                throw IllegalArgumentException("'--to <package>' argument should be provided.")
        }
        val fromFolders = fromPackageName.replace('.', File.separatorChar)
        val toFolders = toPackageName.replace('.', File.separatorChar)

        project.projectDir.walkBottomUp()
            .onEnter { it.name !in folderBlockList }
            .forEach { entry ->
                if (entry.isFile && entry.canRead()) {
                    val oldText = entry.readText()
                    val newText = oldText.replace(fromPackageName, toPackageName)

                    if (oldText != newText) {
                        entry.writeText(newText)
                        println("Rewrote $entry")
                    }
                } else if (entry.isDirectory && entry.absolutePath.endsWith(fromFolders)) {
                    val outputPath = entry.absolutePath.replace(fromFolders, toFolders)
                    Files.move(entry.toPath(), Path.of(outputPath))
                    println("Moved $entry, to $outputPath")
                }
            }
    }
}
