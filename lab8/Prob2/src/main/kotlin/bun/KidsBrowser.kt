

interface Clonabil {
    fun clone(): GenericRequest
}

class GenericRequest(private val url: String, private val params: Map<String, String>? = null) : Clonabil {
    constructor(url: String) : this(url, null)

    fun getUrl(): String = url
    fun getParams(): Map<String, String>? = params

    override fun clone(): GenericRequest {
        return GenericRequest(url, params?.toMap())
    }
}

interface HTTPGet {
    fun getResponse(): String
}


class GetRequest(private val genericRequest: GenericRequest, private val timeout: Int) : HTTPGet {
    constructor(url: String, params: Map<String, String>, timeout: Int) : this(GenericRequest(url, params), timeout)

    override fun getResponse(): String {
        return "Raspuns GET primit de la: ${genericRequest.getUrl()} (timeout: ${timeout}ms)"
    }

    fun getGenericRequest(): GenericRequest {
        return genericRequest
    }
}


class CleanGetRequest(private val getRequest: GetRequest) : HTTPGet {
    private val parentalControlDisallow = listOf(
       "alcool", "viol", "porn", "pacanele", "droguri", "maxbet", "bet", "superbet"
    )

    constructor(url: String, params: Map<String, String>, timeout: Int) : this(GetRequest(url, params, timeout))

    override fun getResponse(): String {
        val url = getRequest.getGenericRequest().getUrl()
        return if (isBlocked(url)) {
            "Acces interzis de controlul parental pentru site-ul: $url"
        } else {
            getRequest.getResponse()
        }
    }

    private fun isBlocked(url: String): Boolean {
        return parentalControlDisallow.any { badWord -> url.contains(badWord, ignoreCase = true) }
    }
}


class PostRequest(private val genericRequest: GenericRequest) {
    constructor(url: String, params: Map<String, String>) : this(GenericRequest(url, params))

    fun postData(): String {
        return "Datele au fost trimise prin POST catre: ${genericRequest.getUrl()}"
    }
}


class KidsBrowser(private val cleanGetRequest: CleanGetRequest, private val postRequest: PostRequest?) {

    fun start() {
        println("\n     BROWSER PENTRU COPII \n")

        println("   Se incearca accesarea site-ului: ")
        print(cleanGetRequest.getResponse())
        println()

        if (postRequest != null) {
            println(">>> Se efectueaza o cerere de tip POST...")
            println(postRequest.postData())
        }

        println("\n    INCHIDERE BROWSER   \n")
    }
}


fun main() {
    val genericGet = GenericRequest("https://www.superbet.ro")
    val getRequest = GetRequest(genericGet, 5000)
    val cleanGetRequest = CleanGetRequest(getRequest)

    val postGeneric = GenericRequest("https://httpbin.org/post", mapOf("cheie" to "valoare"))
    val postRequest = PostRequest(postGeneric)

    val browser = KidsBrowser(cleanGetRequest, postRequest)
    browser.start()
}