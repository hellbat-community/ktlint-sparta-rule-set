package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class FunArgumentsRuleTest {
    @Test
    fun testRule() {
        val rule = FunArgumentsRule(getFileConfigJSON(TEST_PATH).maxFunArguments)

        val classWithError = this::class.java.getResource("/function_arguments_rule_data/testOne").readText()
        val perfectClass = this::class.java.getResource("/PerfectTestClass.kt").readText()

        val withError = rule.lint(classWithError).filter { error -> error.ruleId == rule.id }
        val withoutErrors = rule.lint(perfectClass).filter { error -> error.ruleId == rule.id }

        Assert.assertEquals(withError.count(), 1)
        Assert.assertEquals(withoutErrors.count(), 0)
        Assert.assertNotNull(withError.find { it.detail == "You can't use more 5 arguments of function funcA" })
    }
}