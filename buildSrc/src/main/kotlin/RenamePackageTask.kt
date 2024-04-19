import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

abstract class RenamePackageTask : DefaultTask() {
    init {
        description = """
            Walks through project files and renames matching package mentions and
            directories.

            Options --to and --from, and setting 'directoryBlockList' should be provided
            carefully, as they can cause drastic changes if not set properly.
            Optionally, project can be cleaned before executing this task, however by
            default 'build' directories are excluded by 'directoryBlockList'.
        """.trimIndent()
    }

    @Input
    @Option(
        option = "from",
        description = "Name of the module to be renamed.",
    )
    var fromPackageName = ""

    @Input
    @Option(
        option = "to",
        description = "Name of the module to rename previous module to.",
    )
    var toPackageName = ""

    /** Directories that won't be visited for renaming. Can be extended in `tasks.register`. */
    @Input
    var directoryBlockList = hashSetOf(".idea", ".gradle", "build", ".git")

    @TaskAction
    fun execute() {
        when {
            fromPackageName.isBlank() ->
                throw IllegalArgumentException("'--from <package>' argument should be provided.")

            toPackageName.isBlank() ->
                throw IllegalArgumentException("'--to <package>' argument should be provided.")
        }
        val fromFolders = fromPackageName.replace('.', File.separatorChar)
        val toFolders = toPackageName.replace('.', File.separatorChar)

        // BottomUp is needed to edit files first, then move them into a new directory
        project.projectDir.walkBottomUp()
            .onEnter { it.name !in directoryBlockList }
            .forEach { entry ->
                if (entry.isFile && entry.canRead() && entry.canWrite()) {
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
