interface Poarta_generica {
    fun calculeaza(intrari: List<Int>): Boolean
}

class PoartaAND : Poarta_generica {
    override fun calculeaza(intrari: List<Int>): Boolean {
        for (v in intrari) {
            if (v != 1) return false
        }
        return true
    }
}

abstract class Poarta(protected val implementare: Poarta_generica) {
    abstract fun evalueaza(): Boolean
}

class ConstructorPoarta(private val implementare: Poarta_generica) {
    private val intrari = mutableListOf<Int>()

    fun adaugaIntrare(intrare: Int): ConstructorPoarta {
        intrari.add(intrare)
        return this
    }

    fun construieste(): Poarta {
        return when (intrari.size) {
            2 -> Poarta2(implementare, intrari)
            3 -> Poarta3(implementare, intrari)
            4 -> Poarta4(implementare, intrari)
            8 -> Poarta8(implementare, intrari)
            else -> throw IllegalArgumentException("Numar gresit de intrari")
        }
    }
}

class Poarta2(impl: Poarta_generica, private val intrari: List<Int>) : Poarta(impl) {
    override fun evalueaza(): Boolean = AutomatStari().evalueaza(intrari, implementare)
}

class Poarta3(impl: Poarta_generica, private val intrari: List<Int>) : Poarta(impl) {
    override fun evalueaza(): Boolean = AutomatStari().evalueaza(intrari, implementare)
}

class Poarta4(impl: Poarta_generica, private val intrari: List<Int>) : Poarta(impl) {
    override fun evalueaza(): Boolean = AutomatStari().evalueaza(intrari, implementare)
}

class Poarta8(impl: Poarta_generica, private val intrari: List<Int>) : Poarta(impl) {
    override fun evalueaza(): Boolean = AutomatStari().evalueaza(intrari, implementare)
}

interface Stare {
    fun urmatoare(intrare: Int): Stare
    fun esteFinala(): Boolean
}

class StareAcceptare : Stare {
    override fun urmatoare(intrare: Int): Stare = if (intrare == 1) this else StareRespinge()
    override fun esteFinala(): Boolean = true
}

class StareRespinge : Stare {
    override fun urmatoare(intrare: Int): Stare = this
    override fun esteFinala(): Boolean = false
}

class AutomatStari() {
    fun evalueaza(intrari: List<Int>, implementare: Poarta_generica): Boolean {
        var stare: Stare = StareAcceptare()
        for (intrare in intrari) {
            stare = stare.urmatoare(intrare)
        }
        return stare.esteFinala() && implementare.calculeaza(intrari)
    }
}

fun Rezultat_Poarta(numarIntrari: Int) {
    val implementare = PoartaAND()
    val combinatii = genereazaCombinatii(numarIntrari)

    println("Poarta AND cu $numarIntrari intrari:")

    for (comb in combinatii) {
        val constructor = ConstructorPoarta(implementare)
        comb.forEach { constructor.adaugaIntrare(it) }
        val poarta = constructor.construieste()
        val rezultat = poarta.evalueaza()
        println("${comb.joinToString(" ")} -> ${if (rezultat) "true" else "false"}")
    }

    println()
}

fun genereazaCombinatii(n: Int): List<List<Int>> {
    val rezultat = mutableListOf<List<Int>>()
    val total = 1 shl n
    for (i in 0 until total) {
        val combinatie = mutableListOf<Int>()
        for (j in n - 1 downTo 0) {
            combinatie.add((i shr j) and 1)
        }
        rezultat.add(combinatie)
    }
    return rezultat
}

fun main() {
    Rezultat_Poarta(2)
    Rezultat_Poarta(3)
    Rezultat_Poarta(4)
    //Rezultat_Poarta(8)
}
