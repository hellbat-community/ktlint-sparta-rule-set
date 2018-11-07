package java_docs_rule_test_data

import org.testng.annotations.Test

class GoodTestTest(arg: String) {
    var text: String = arg

    private lateinit var value: String

    constructor(arg: Int) : this(arg.toString()) {
        value = arg.toString()
    }

    @Test(description = "badAFun")
    fun badAFun(arg: String) {}
}