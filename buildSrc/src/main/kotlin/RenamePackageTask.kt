import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

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

        project.projectDir.walkBottomUp()
            .onEnter { it.name !in folderBlockList }
            .forEach { entry ->
                if (!entry.isFile || !entry.canRead()) return@forEach

                val oldText = entry.readText()
                val newText = oldText.replace(fromPackageName, toPackageName)

                if (oldText != newText) {
                    entry.writeText(newText)
                    println("Rewrote ${entry.absolutePath}")
                }
            }
    }
}
