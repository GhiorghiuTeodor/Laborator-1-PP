import java.io.File

fun caesarCipher(word: String, offset: Int): String {
    return word.map { char ->
        when (char) {
            in 'a'..'z' -> ((char - 'a' + offset) % 26 + 'a'.code).toChar()
            in 'A'..'Z' -> ((char - 'A' + offset) % 26 + 'A'.code).toChar()
            else -> char
        }
    }.joinToString("")
}

fun main() {
    val filename = "input.txt"
    val offset = 3
    val text = File(filename).readText()
    val words = text.split(" ")
    val encrypted = words.map { word ->
        if (word.length in 4..7) caesarCipher(word, offset) else word
    }
    val result = encrypted.joinToString(" ")
    println(result)
}
