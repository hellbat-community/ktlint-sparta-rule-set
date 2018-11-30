/**
 * objectPropertiesTest
 */
object objectPropertiesTest {
    const val valA: String = "valA"
    val valB: String = "valB"
}

/**
 * objectATest
 */
object objectPropertiesATest {
    const val valA: String = "valA"

    /**
     * valB
     */
    val valB: String = "valB"
}

/**
 * ClassPropertiesTest
 */
class ClassPropertiesTest {
    companion object {
        private const val KEY_PROCESS_CONTEXT2 = "processContext2"
        const val KEY_PROCESS_CONTEXT = "processContext"
    }

    private var handler = "handler"

    private lateinit var eventContext: String

    /**
     * It's the good argument
     */
    var goodArgument: String = ""

    var badArgument: String = ""

    // Not on my shift
    var badArgumentTo: String = ""

    /**
     *
     */
    var badEmptyArgument: String = ""
}