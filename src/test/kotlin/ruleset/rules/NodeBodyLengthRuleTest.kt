package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class NodeBodyLengthRuleTest {
    @Test
    fun ruleTest() {
        val rule = NodeBodyLengthRule(getFileConfigJSON(TEST_PATH).nodeBodyLength)

        val funTest = this::class.java.getResource("/node_body_length_test_data/LongFun.kt").readText()
        val classTest = this::class.java.getResource("/node_body_length_test_data/LongClass.kt").readText()

        val errorList = rule.lint(funTest).filter { error -> error.ruleId == rule.id }
        val classLintErrorList = rule.lint(classTest).filter { error -> error.ruleId == rule.id }

        Assert.assertNotNull(errorList.find { e ->
            e.detail.contains("Element has a body size of 40 lines, expected no more than 20.")
        })
        Assert.assertNotNull(classLintErrorList.find { e ->
            e.detail.contains("Element has a body size of 48 lines, expected no more than 7.")
        })
    }
}