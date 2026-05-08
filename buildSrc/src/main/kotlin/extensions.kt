import org.gradle.api.Project

fun Project.runGitCommand(args: List<String>): String {
    return providers.exec {
        commandLine("git")
        args(args)
    }.standardOutput.asText.get().trim()
}

fun Project.latestCommitDate(): String {
    return runGitCommand(listOf("log", "-1", "--format=%cd", "--date=format:%y.%m.%d"))
}

fun Project.latestCommitHash(): String {
    return runGitCommand(listOf("rev-parse", "--short", "HEAD"))
}

fun Project.firstParentCommitCount(): String {
    return runGitCommand(listOf("rev-list", "--count", "--first-parent", "HEAD"))
}
