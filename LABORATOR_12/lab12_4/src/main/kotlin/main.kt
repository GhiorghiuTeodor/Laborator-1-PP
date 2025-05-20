fun String.toPascalCase(): String = this
    .split(" ")
    .joinToString("") { it.lowercase().replaceFirstChar { c -> c.uppercase() } }

class MapFunctor(private val map: MutableMap<Int, String>) {
    fun map(transform: (String) -> String): MapFunctor {
        val newMap = map.mapValues { (_, value) -> transform(value) }.toMutableMap()
        return MapFunctor(newMap)
    }

    fun getMap(): MutableMap<Int, String> = map
}

fun main() {
    val initialMap = mutableMapOf(
        1 to "acesta este un test",
        2 to "alt exemplu de text"
    )

    val functor = MapFunctor(initialMap)
    val result = functor
        .map { "Test$it" }
        .map { it.toPascalCase() } 
        .getMap()

    result.forEach { (key, value) ->
        println("$key -> $value")
    }
}
