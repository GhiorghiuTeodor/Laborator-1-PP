import java.util.concurrent.ArrayBlockingQueue

fun main() {
    val numbers = listOf(5, 2, 8, 1, 9, 3)
    val alpha = 2

    val queue1 = ArrayBlockingQueue<Int>(10)
    val queue2 = ArrayBlockingQueue<Int>(10)

    Thread {
        for (num in numbers) {
            val result = num * alpha
            queue1.put(result)
            println("Thread 1: $num * $alpha = $result")
        }
        queue1.put(999)
    }.start()

    Thread {
        val sortedList = mutableListOf<Int>()
        while (true) {
            val num = queue1.take()
            if (num == 999) break

            sortedList.add(num)
            println("Thread 2: a primitpentru sortare numarul $num ")
        }

        sortedList.sort()
        for (sortedNum in sortedList) {
            queue2.put(sortedNum)
        }

        queue2.put(999)
    }.start()


    Thread {
        while (true) {
            val num = queue2.take()
            if (num == 999) break
            println("Thread 3: rezultatul final = $num")
        }
    }.start()
}
