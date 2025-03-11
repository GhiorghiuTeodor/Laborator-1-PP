import java.io.File
import java.io.IOException

fun spatii_multiple(text: String): String {
    return text.replace(Regex("[ \t]+"), " ")
}

fun enter(text: String): String {
    return text.replace(Regex("(\r?\n)+"), "\n")
}
fun nr_pag(text: String): String {
    return text.replace(Regex("\\s+\\d+\\s+"), " ")
}

fun autor(text: String): String {
    val cuv = text.split(Regex("\\s+"))
    val per_cuv = mutableListOf<String>()

    for (i in 0 until cuv.size - 1) {
        per_cuv.add("${cuv[i]} ${cuv[i + 1]}")
    }

    val frec = per_cuv.groupingBy { it }.eachCount()
    val per_frec = frec.maxByOrNull { it.value }?.key

    return if (per_frec != null) {
        text.replace(per_frec, "")
    } else {
        text
    }
}

fun caractere(text: String): String {
    val caractere_de_inlocuit = mapOf(
        'ă' to 'a',
        'Ă' to 'A',
        'â' to 'a',
        'Â' to 'A',
        'î' to 'i',
        'Î' to 'I'
    )

    var text_final = text
    for ((c_vechi, c_nou) in caractere_de_inlocuit) {
        text_final = text_final.replace(c_vechi, c_nou)
    }
    text_final = text_final.replace(Regex("[\\u0219\\u0218]"), "s") // Ş
    text_final = text_final.replace(Regex("[\\u021B\\u021A]"), "t")  //Ţ
    return text_final
}

fun main() {
    val fileName = "ebook.txt"
    var text = try {
        File(fileName).readText(Charsets.UTF_8)
    } catch (e: IOException) {
        println("Eroare la citire: ${e.message}")
        return
    }
    text = spatii_multiple(text)
    text = nr_pag(text)
    text = autor(text)
    text = caractere(text)
    text = enter(text)

    val output = "rezultar.txt"
    try {
        File(output).writeText(text, Charsets.UTF_8)
        println("Fișierul final a fost salvat $output")
    } catch (e: IOException) {
        println("Eroare la salvare: ${e.message}")
    }
}
