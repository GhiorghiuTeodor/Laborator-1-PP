import kotlinx.coroutines.*
import java.util.concurrent.ArrayBlockingQueue

fun main() = runBlocking {

    val queue = ArrayBlockingQueue<Int>(4).apply {
        add(100)
        add(1000)
        add(10000)
        add(100000)
    }


    val jobs = mutableListOf<Job>()

    repeat(4) { workerId ->
        val job = launch {
            val n = queue.take()

            var sum = 0L
            for (i in 0..n) {
                sum += i
            }

            println("Corutina $workerId a calculat suma pana la $n: $sum")
        }

        jobs.add(job)
    }

    jobs.forEach { it.join() }

    println("Toate corutinele au terminat calculele.")
}
