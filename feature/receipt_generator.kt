/**
 * Generates a 55 mm ESC/POS receipt as a ByteArray.
 *
 * @param tx       the Transaction object
 * @param items    list of TransactionItem objects (already joined with Product)
 * @return         ByteArray ready to send to a Bluetooth thermal printer
 */
fun generateReceipt(
    tx: Transaction,
    items: List<TransactionItemWithProduct>
): ByteArray {

    // 55 mm paper ≈ 42 characters wide at 12 CPI monospace
    val PAPER_WIDTH = 42
    val line = "-".repeat(PAPER_WIDTH)     // dashed divider

    val sb = StringBuilder()

    /* ---------- ESC/POS helpers ---------- */
    val ESC   = 0x1B.toByte()
    val LF    = 0x0A.toByte()
    val GS    = 0x1D.toByte()

    fun center(text: String): String = text.padStart((PAPER_WIDTH + text.length) / 2)

    fun right(text: String): String = text.padStart(PAPER_WIDTH)

    fun left(text: String): String = text.padEnd(PAPER_WIDTH)

    /* ---------- Build text ---------- */
    sb.appendLine(center("GROCERY STORE"))
    sb.appendLine(center("123 Market St, City"))
    sb.appendLine()

    sb.appendLine(
        "Date: ${tx.date}" +
        "  Time: ${tx.time}".padStart(PAPER_WIDTH - ("Date: ${tx.date}".length))
    )
    sb.appendLine("Receipt #: ${tx.receipt_number}  Cashier: ${tx.cashier_id}")
    sb.appendLine(line)

    items.forEach { (item, product) ->
        // Main line: SHORT_DESC QTY PRICE TOTAL
        val desc = product.name.take(14).padEnd(14)
        val qty  = "%.2f".format(item.quantity).padStart(5)
        val price = "₹%.2f".format(item.applied_price).padStart(7)
        val total = "₹%.2f".format(item.quantity * item.applied_price).padStart(9)

        sb.appendLine("$desc $qty $price $total")

        // Optional tier tag
        if (item.price_tier != "retail") {
            sb.appendLine("  [${item.price_tier.uppercase()}]")
        }
    }

    sb.appendLine(line)
    sb.appendLine(right("Total: ₹%.2f".format(tx.total_amount)))
    sb.appendLine("Payment: ${tx.payment_method}")
    if (tx.customer_name != null) {
        sb.appendLine("Customer: ${tx.customer_name} | ${tx.customer_phone}")
    }
    sb.appendLine()
    sb.appendLine(center("Thank You!"))
    sb.append(LF)   // extra feed
    sb.append(LF)

    /* ---------- ESC/POS commands ---------- */
    val bytes = mutableListOf<Byte>()

    // Initialize printer
    bytes += byteArrayOf(ESC, '@'.code.toByte())

    // Select 12 CPI monospace (Font A)
    bytes += byteArrayOf(ESC, 'M'.code.toByte(), 0)

    // Append text
    bytes += sb.toString().toByteArray(Charsets.US_ASCII)

    // Feed & cut (partial cut)
    bytes += byteArrayOf(GS, 'V'.code.toByte(), 66, 0)

    return bytes.toByteArray()
}

/* ------------- Sample data classes (for reference) ------------- */
data class Transaction(
    val date: String,
    val time: String,
    val receipt_number: Long,
    val cashier_id: String,
    val total_amount: Double,
    val payment_method: String,
    val customer_name: String? = null,
    val customer_phone: String? = null
)

data class TransactionItemWithProduct(
    val item: TransactionItem,
    val product: Product
)

data class TransactionItem(
    val quantity: Double,
    val applied_price: Double,
    val price_tier: String
)

data class Product(
    val name: String
)