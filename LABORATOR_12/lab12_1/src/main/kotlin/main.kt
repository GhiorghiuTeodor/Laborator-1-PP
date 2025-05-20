fun main() {
    val lista = listOf(1, 21, 75, 39, 7, 2, 35, 3, 31, 7, 8)
    val filtrata = lista.filter { it >= 5 }
    println("Filtrata: $filtrata")
    val perechi = filtrata.chunked(2) { it[0] to it.getOrElse(1) { 1 } }  //ia 1 in caz de e nr impar de elemente
    println("Perechi: $perechi")
    val produse = perechi.map { it.first * it.second }
    println("Produse: $produse")
    val suma = produse.sum()
    println("Suma totala: $suma")
}
