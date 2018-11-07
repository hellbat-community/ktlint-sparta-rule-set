package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class NodeNameLengthRuleTest {
    @Test
    fun ruleTest() {
        val rule = NodeNameLengthRule(getFileConfigJSON(TEST_PATH).nodeNameLength)

        val classTest = this::class.java.getResource("/node_name_length_test_data/ClassLengthTest.kt").readText()
        val funTest = this::class.java.getResource("/node_name_length_test_data/FunLengthTest.kt").readText()

        val classLintErrorList = rule.lint(classTest).filter { error -> error.ruleId == rule.id }
        val funLintErrorList = rule.lint(funTest).filter { error -> error.ruleId == rule.id }

        assertNotNull(classLintErrorList.find { e ->
            e.detail.contains("Element has a name of 15 characters, expected no more than 5.")
        })
        assertNotNull(funLintErrorList.find { e ->
            e.detail.contains("Element has a name of 11 characters, expected no more than 10.")
        })
    }
}