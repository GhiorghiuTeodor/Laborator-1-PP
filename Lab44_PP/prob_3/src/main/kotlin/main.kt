import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Notita(val numeFisier: String, val autor: String, val continut: String) {

    private val dataOra: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))


    fun salveazaInFisier() {
        val numeFisierComplet = "notite/$numeFisier.txt"


        val folderNotite = File("notite")
        if (!folderNotite.exists()) {
            folderNotite.mkdirs()
        }


        val fisier = File(numeFisierComplet)
        fisier.writeText("# Autor: $autor\n## Data: $dataOra\n\n$continut")

        println("Notita a fost salvata: $numeFisierComplet")
    }
}

class ManagerNotite {

    fun listeazaNotite() {
        val director = File("notite")
        val fisiere = director.listFiles()

        if (fisiere == null || fisiere.isEmpty()) {
            println("Nu exista notite salvate.")
        } else {
            for (fisier in fisiere) {
                println(fisier.name)
            }
        }
    }

    fun incarcaNotita(numeFisier: String) {
        val fisier = File("notite/$numeFisier")
        if (fisier.exists()) {
            println(fisier.readText())
        } else {
            println("Fisierul nu exista.")
        }
    }

    fun stergeNotita(numeFisier: String) {
        val fisier = File("notite/$numeFisier")
        if (fisier.exists() && fisier.delete()) {
            println("Notita $numeFisier a fost stearsa.")
        } else {
            println("Eroare la stergerea notitei.")
        }
    }
}

class User(val nume: String) {
    private val managerNotite = ManagerNotite()

    fun vizualizeazaNotite() {
        println("Notitele tale sunt: ")
        managerNotite.listeazaNotite()
    }

    fun citesteNotita(numeFisier: String) {
        println("Citirea notitei $numeFisier: ")
        managerNotite.incarcaNotita(numeFisier)
    }

    fun creeazaNotita() {
        println("Introduceti numele fisierului pentru notita: ")
        val numeFisier = readLine() ?: ""
        println("Introduceti continutul notitei: ")
        val continut = readLine() ?: ""
        Notita(numeFisier, nume, continut).salveazaInFisier()
    }

    fun stergeNotita() {
        println("Introduceti numele fisierului pe care doriti sa il stergeti: ")
        val numeFisier = readLine() ?: ""
        managerNotite.stergeNotita(numeFisier)
    }
}

fun main() {
    println("Introduceti numele utilizatorului: ")
    val numeUtilizator = readLine() ?: "Utilizator"
    val user = User(numeUtilizator)

    while (true) {
        println("\n1. Afisare notite\n2. Citire notita\n3. Creare notita\n4. Stergere notita\n5. Iesire")
        when (readLine()) {
            "1" -> user.vizualizeazaNotite()
            "2" -> {
                println("Introduceti numele fisierului: ")
                val numeFisier = readLine() ?: ""
                user.citesteNotita(numeFisier)
            }
            "3" -> user.creeazaNotita()
            "4" -> user.stergeNotita()
            "5" -> return
            else -> println("Optiune invalida.")
        }
    }
}
