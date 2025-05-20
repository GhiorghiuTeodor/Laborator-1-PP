import kotlin.math.sqrt

fun main() {
    val n = readLine()!!.toInt()  //daca e null arunca o exceptie
    val points = mutableListOf<Pair<Double, Double>>()

    repeat(n) {
        val (x, y) = readLine()!!.split(" ").map { it.toDouble() }
        points.add(x to y)
    }

    val distances = points.zipWithNext { p1, p2 ->
        distance(p1, p2)
    }.toMutableList()

    distances.add(distance(points.last(), points.first()))
    val perimeter = distances.sum()
    println(perimeter)
}

fun distance(p1: Pair<Double, Double>, p2: Pair<Double, Double>): Double {
    val dx = p1.first - p2.first
    val dy = p1.second - p2.second
    return sqrt(dx * dx + dy * dy)
}
