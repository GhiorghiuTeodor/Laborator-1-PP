import java.util.*
import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.Font
import java.util.Date


interface PaymentMethod {
    fun pay(fee: Double): Boolean
}
class CashPayment(private var availableAmount: Double) : PaymentMethod {

    override fun pay(fee: Double): Boolean {
        if (availableAmount >= fee) {
            availableAmount -= fee
            println("Plată cu numerar: $fee")
            return true
        }
        else {
            println("Fonduri insuficiente pentru plata cu numerar.")
            return false
        }
    }
}
class BankAccount(private var available: Double, private val cardNumber: String, private val expirationDate: Date, private val cvvCode: Int, private val name: String)
{
    fun updateAmount(value: Double): Boolean {
        if (available >= value) {
            available -= value
            println("Plată efectuată cu cardul: $value")
             return true
        } else {
            println("Fonduri insuficiente pe card.")
             return false
        }
    }

    fun getAvailableAmount(): Double {
        return available
    }
}

class CardPayment(private val bankAccount: BankAccount) : PaymentMethod {

    override fun pay(fee: Double): Boolean {
        if (bankAccount.updateAmount(fee)) {
            println("Plata cu cardul a fost realizată.")
            println("Mai aveti in cont " + bankAccount.getAvailableAmount())
            return true
        } else {
            println("Plata cu cardul a eșuat.")
            println("Mai aveti in cont" + bankAccount.getAvailableAmount())
            return false
        }
    }
}

fun main() {
    val bankAccount = BankAccount(100.0, "1234567890123456", Date(), 123, "Ion Popescu")
    val cardPayment = CardPayment(bankAccount)
    val cashPayment = CashPayment(50.0)

    val frame = JFrame("Cinema App")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.setSize(450, 400)
    val panel = JPanel()
    frame.add(panel)
    panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
    val largeFont = Font("Arial", Font.PLAIN, 25)
    val titleLabel = JLabel("Film: 'Inception'")
    titleLabel.font = largeFont
    panel.add(titleLabel)
    val ticketPrice = 30.0
    val priceLabel = JLabel("Prețul biletului: $ticketPrice Lei")
    priceLabel.font = largeFont
    panel.add(priceLabel)
    val statusLabel = JLabel("")
    statusLabel.font = largeFont
    panel.add(statusLabel)
    val cardButton = JButton("Plătește cu Cardul")
    cardButton.font = largeFont

    cardButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            val fee = ticketPrice
            val result = cardPayment.pay(fee)
            statusLabel.text = if (result) "Plata cu cardul a fost realizată!" else "Plata cu cardul a eșuat."
        }
    })
    panel.add(cardButton)

    val cashButton = JButton("Plătește cu Numerar")
    cashButton.font = largeFont
    cashButton.addActionListener(object : ActionListener {
        override fun actionPerformed(e: ActionEvent?) {
            val fee = ticketPrice
            val result = cashPayment.pay(fee)
            statusLabel.text = if (result) "Plata cu numerar a fost realizată!" else "Plata cu numerar a eșuat."
        }
    })
    panel.add(cashButton)

    frame.isVisible = true
}
