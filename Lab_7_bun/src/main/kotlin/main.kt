import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.io.File

class HistoryLogRecord(
    val timestamp: Long,
    val command: String,
    val startDate: String,
    val otherMetadata: MutableMap<String, String> = mutableMapOf()
) : Comparable<HistoryLogRecord> {

    override fun compareTo(other: HistoryLogRecord): Int {
        return this.timestamp.compareTo(other.timestamp)
    }

    override fun toString(): String {
        return "HistoryLogRecord(timestamp=$timestamp, command='$command', startDate='$startDate', otherMetadata=$otherMetadata)"
    }
}

fun <T : Comparable<T>> findMax(a: T, b: T): T {
    return if (a.compareTo(b) > 0) a else b
}

fun findHistoryLogFiles(directory: File): List<File> {
    val historyLogs = mutableListOf<File>()
    fun scanDir(dir: File) {
        dir.listFiles()?.forEach { file ->
            if (file.name.startsWith("history.log")) {
                historyLogs.add(file)
            }
        }
    }
    scanDir(directory)
    return historyLogs
}

fun parseHistoryLog(file: File): MutableList<HistoryLogRecord> {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss")
    val entries = mutableListOf<HistoryLogRecord>()
    val lines = file.readLines()
    var startDate: String? = null
    var command: String? = null
    val otherMetadata = mutableMapOf<String, String>()

    for (line in lines) {
        when {
            line.startsWith("Start-Date: ") -> {
                startDate = line.substringAfter("Start-Date: ").trim()
            }
            line.startsWith("Commandline: ") -> {
                command = line.substringAfter("Commandline: ").trim()
            }
            line.startsWith("Requested-By: ") -> {
                otherMetadata["Requested-By"] = line.substringAfter("Requested-By: ").trim()
            }
            line.startsWith("End-Date: ") -> {
                if (startDate != null && command != null) {
                    val timestamp = try {
                        val parsedDate = LocalDateTime.parse(startDate, formatter)
                        parsedDate.toEpochSecond(java.time.ZoneOffset.UTC)
                    } catch (e: DateTimeParseException) {
                        0L
                    }
                    entries.add(
                        HistoryLogRecord(
                            timestamp = timestamp,
                            command = command,
                            startDate = startDate,
                            otherMetadata = otherMetadata
                        )
                    )
                }
                startDate = null
                command = null
                otherMetadata.clear()
            }
        }
    }

    return entries.takeLast(50).toMutableList()
}

fun replaceRecord(
    target: HistoryLogRecord,
    replacement: HistoryLogRecord,
    map: MutableMap<Long, HistoryLogRecord>
) {
    map.entries.forEach { (key, value) ->
        if (value == target) {
            map[key] = replacement
        }
    }
}

fun main() {
    val searchDirectory = File("C:/Users/User/Desktop/logs")
    val historyLogs = findHistoryLogFiles(searchDirectory)

    if (historyLogs.isEmpty()) {
        println("Nu s-a găsit niciun fișier history.log")
        return
    }

    println("Au fost găsite ${historyLogs.size} fișiere history.log:")
    val allRecords = mutableListOf<HistoryLogRecord>()

    historyLogs.forEach { logFile ->
        val records = parseHistoryLog(logFile)
        allRecords.addAll(records)
    }

    if (allRecords.isNotEmpty()) {
        var maxRecord = allRecords.first()
        allRecords.forEach { record ->
            maxRecord = findMax(maxRecord, record)
        }

        println("\nCea mai recentă înregistrare:")
        println(maxRecord)

        println("\nToate înregistrările (ultimele 50):")
        allRecords.forEach { println(it) }

        val fakeOld = maxRecord
        val fakeNew = HistoryLogRecord(
            timestamp = fakeOld.timestamp,
            command = "echo 'Comandă înlocuită!'",
            startDate = fakeOld.startDate,
            otherMetadata = fakeOld.otherMetadata
        )

        val recordsMap = allRecords.associateBy { it.timestamp }.toMutableMap()
        replaceRecord(fakeOld, fakeNew, recordsMap)

        println("\nToate înregistrările după înlocuire:")
        recordsMap.values.forEach { println(it) }
    } else {
        println("\nNu au fost găsite înregistrări valide în history.log.")
    }
}
