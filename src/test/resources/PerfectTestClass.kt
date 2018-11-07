/**
 * This is the perfect class.
 * And he's the best of the best!
 *
 * @param perfectArg perfectArg
 * @param beautyString beautyString
 */
class PerfectTestClass(private val perfectArg: String, beautyString: String) {
    companion object {
        /**
         * perfectConstCompanionValue
         */
        const val perfectConstCompanionValue = "perfectConstCompanionValue"

        /**
         * perfectCompanionValue
         */
        val perfectCompanionValue = "perfectCompanionValue"
    }

    /**
     * This is the perfect property.
     */
    var perfectVal = "perfectVal"

    /**
     * Документ
     */
    val document = object {
        /**
         * flow
         */
        val flow: String? = null

        /**
         * state
         */
        val state: String? = null
    }

    init {
        perfectVal = beautyString
    }

    /**
     * This is the perfect method.
     *
     * @param perfectValue This is the perfect parameter.
     */
    fun perfectFun(perfectValue: String = "value"): String {
        val secondPerfectValue = "secondPerfectValue"

        if (perfectArg == perfectVal) {
            perfectValue.forEach { println(it) }
        }

        return perfectValue + secondPerfectValue
    }
}