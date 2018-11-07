package java_docs_rule_test_data

object objectTest {
    const val valA: String = "valA"
}

/**
 * objectATest
 */
object objectATest {
    const val valA: String = "valA"

    /**
     * valB
     */
    val valB: String = "valB"
}

/**
 * ClassTest
 */
class ClassTest

class ClassATest

// ClassBTest
class ClassBTest

/**
 *
 */
class ClassCTest

/**
 *
 * @property arg arg
 */
class ClassDTest(private val arg: String)
