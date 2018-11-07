package java_docs_rule_test_data

import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

/**
 * logger
 */
fun logger(): Boolean = true

fun notLogger(): Boolean = false

/**
 * PublicFunTest
 */
class PublicFunTest {
    /**
     * goodFun
     */
    fun goodFun() {}

    /**
     * goodFunA
     *
     * @param arg arg
     */
    fun goodFunA(arg: String) {}

    private fun goodFunB() {}

    /**
     * goodFunC
     */
    private fun goodFunC() {}

    fun badFun() {}

    fun badAFun(arg: String) {}

    /**
     * badBFun
     */
    fun badBFun(arg: String) {}

    /**
     * badCFun
     *
     * @param arg arg
     */
    fun badCFun(arg: String, test: String) {}

    // badDun
    fun badDun() {}

    /**
     *
     */
    fun badEun() {}

    /**
     * badFFun
     *
     * @param arg
     */
    fun badFFun(arg: String) {}
}

/**
 * AbstractFunTest
 */
abstract class AbstractFunTest {
    /**
     * abstractFun
     */
    abstract fun abstractFun()
}

/**
 * AbstractFunImplTest
 */
class AbstractFunImplTest : AbstractFunTest() {
    override fun abstractFun() {
    }
}

/**
 * Тест
 */
class ServiceTest {
    @BeforeMethod
    @Suppress
    fun init() {
    }

    @Test(description = "description")
    fun someTest() {
    }
}
