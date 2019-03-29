import java.io.File
import java.util.concurrent.TimeUnit

fun String.asciiToHex(): String {
    var hex = ""
    for (ch in this.toCharArray()) hex += Integer.toHexString(ch.toInt())
    return hex
}

fun String.hexToAscii(): String {
    var ascii = ""
    for (i in 0 until this.length step 2) {
        val byte = this.substring(i until i + 2)
        ascii += Integer.parseInt(byte, 16).toChar()
    }
    return ascii
}

fun String.runCommand(workingDir: File) {
    ProcessBuilder(*split(" ").toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor(60, TimeUnit.MINUTES)
}