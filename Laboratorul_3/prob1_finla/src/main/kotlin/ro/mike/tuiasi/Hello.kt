package ro.mike.tuiasi
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.IOException

data class RSSItem(val title: String, val link: String) {
    override fun toString(): String {
        //puteam sa pun si aici descriere si data publicarii dar in enunt pare ca nu ar fi nevoie
        return "Titlu: $title\nLink: $link\n\n"
    }
}
data class RSSFeed(val title: String, val link: String, val description: String,val pubDate: String, val items: MutableList<RSSItem> = mutableListOf()) {

    fun addItem(item: RSSItem) {
        items.add(item)
    }

    fun writeToFile(filename: String) {
        try {
            BufferedWriter(FileWriter(filename)).use { w ->
                w.write("Flux RSS: $title\n")
                w.write("Link-ul: $link\n")
                w.write("Data publicarii: $pubDate\n")
                w.write("Descriere: $description\n\n")
                items.forEach { w.write(it.toString()) }
                println("Pana aici merge\n")
            }
        } catch (e: IOException) {
            println("Eroare : ${e.message}")
        }
    }
}

fun main() {
    val url = "https://stirileprotv.ro/rss"
    try {
        val doc: Document = Jsoup.connect(url).get()
        val channel: Element = doc.selectFirst("channel")!!

        val titlu = channel.selectFirst("title")!!.text()
        val link = channel.selectFirst("link")!!.text()
        val descriere = channel.selectFirst("description")!!.text()
        val dataPub = channel.selectFirst("pubDate")!!.text()
        val feed = RSSFeed(titlu, link, descriere, dataPub)

        val items: Elements = channel.select("item")
        for (item in items) {
            val itemTitlu = item.selectFirst("title")!!.text()
            val itemLink = item.selectFirst("link")!!.text()
            val rssItem = RSSItem(itemTitlu, itemLink)
            feed.addItem(rssItem)
        }

        feed.writeToFile("Continut.txt")

    } catch (e: IOException) {
        println("Eroare : ${e.message}")
    }
}