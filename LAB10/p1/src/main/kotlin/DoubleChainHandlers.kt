import kotlinx.coroutines.*

interface Handler {
    var next1: Handler?
    var next2: Handler?
    suspend fun handleRequest(mesaj: String)
}

class CEOHandler : Handler {
    override var next1: Handler? = null
    override var next2: Handler? = null

    override suspend fun handleRequest(mesaj: String) {
        val parts = mesaj.split("-", limit = 2)
        val priority = parts[0].trim().toIntOrNull() ?: return
        val message = parts[1].trim()

        if (priority == 1) {
            println("Sunt CEO si prelucrez mesajul: $message")
            delay(300)
            next2?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    it.handleRequest("Raspuns - Task procesat de CEO")
                }
            }
        } else {
            println("CEO paseaza mai departe...")
            next1?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    it.handleRequest(mesaj)
                }
            }
        }
    }
}

class ExecutiveHandler : Handler {
    override var next1: Handler? = null
    override var next2: Handler? = null

    override suspend fun handleRequest(mesaj: String) {
        val parts = mesaj.split("-", limit = 2)
        val priority = parts[0].trim().toIntOrNull() ?: return
        val message = parts[1].trim()

        if (priority == 2) {
            println("Sunt Executive si prelucrez mesajul: $message")
            delay(300)
            next2?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    it.handleRequest("Raspuns - Task procesat de Executive")
                }
            }
        } else {
            println("Executive paseaza mai departe...")
            next1?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    it.handleRequest(mesaj)
                }
            }
        }
    }
}

class ManagerHandler : Handler {
    override var next1: Handler? = null
    override var next2: Handler? = null

    override suspend fun handleRequest(mesaj: String) {
        val parts = mesaj.split("-", limit = 2)
        val priority = parts[0].trim().toIntOrNull() ?: return
        val message = parts[1].trim()

        if (priority == 3) {
            println("Sunt Manager si prelucrez mesajul: $message")
            delay(300)
            next2?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    it.handleRequest("Raspuns - Task procesat de Manager")
                }
            }
        } else {
            println("Manager paseaza mai departe...")
            next1?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    it.handleRequest(mesaj)
                }
            }
        }
    }
}

class HappyWorkerHandler : Handler {
    override var next1: Handler? = null
    override var next2: Handler? = null

    override suspend fun handleRequest(mesaj: String) {
        val parts = mesaj.split("-", limit = 2)
        val priority = parts[0].trim().toIntOrNull() ?: return
        val message = parts[1].trim()

        if (priority >= 4) {
            println("Sunt HappyWorker si prelucrez mesajul: $message")
            delay(300)
            next2?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    it.handleRequest("Raspuns - Task procesat de HappyWorker")
                }
            }
        } else {
            println("HappyWorker paseaza mai departe...")
            next1?.let {
                CoroutineScope(Dispatchers.Default).launch {
                    it.handleRequest(mesaj)
                }
            }
        }
    }
}

interface AbstractFactory {
    fun getHandler(handler: String): Handler
}

class EliteFactory : AbstractFactory {
    override fun getHandler(handler: String): Handler {
        return when (handler) {
            "CEO" -> CEOHandler()
            "Executive" -> ExecutiveHandler()
            "Manager" -> ManagerHandler()
            else -> throw IllegalArgumentException("Nu exista handler-ul cerut in EliteFactory")
        }
    }
}

class HappyWorkerFactory : AbstractFactory {
    override fun getHandler(handler: String): Handler {
        if (handler == "HappyWorker") {
            return HappyWorkerHandler()
        } else {
            throw IllegalArgumentException("Nu exista handler-ul cerut in HappyWorkerFactory")
        }
    }
}

object FactoryProducer {
    fun getFactory(choice: String): AbstractFactory {
        return when (choice) {
            "Elite" -> EliteFactory()
            "Happy" -> HappyWorkerFactory()
            else -> throw IllegalArgumentException("Factory necunoscuta")
        }
    }
}

fun main() = runBlocking {
    val eliteFactory = FactoryProducer.getFactory("Elite")
    val happyFactory = FactoryProducer.getFactory("Happy")

    val ceo = eliteFactory.getHandler("CEO")
    val exec = eliteFactory.getHandler("Executive")
    val manager = eliteFactory.getHandler("Manager")
    val worker = happyFactory.getHandler("HappyWorker")

    ceo.next1 = exec
    exec.next1 = manager
    manager.next1 = worker

    worker.next2 = manager
    manager.next2 = exec
    exec.next2 = ceo

    ceo.handleRequest("4 - Trimite raportul trimestrial")

    delay(1000)
}
