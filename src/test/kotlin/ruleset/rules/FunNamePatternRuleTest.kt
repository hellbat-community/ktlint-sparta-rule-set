package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNull
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class FunNamePatternRuleTest {
    @Test
    fun ruleTest() {
        val pattern = getFileConfigJSON(TEST_PATH).funNamePattern
        val rule = FunNamePatternRule(pattern)

        val perfectClass = this::class.java.getResource("/PerfectTestClass.kt").readText()
        val badClass = this::class.java.getResource("/fun_name_rule_test_data/Test.kt").readText()
        val perfectClassLintError = rule.lint(perfectClass).find { error -> error.ruleId == rule.id }
        val badClassLintErrors = rule.lint(badClass).filter { error -> error.ruleId == rule.id }

        assertNull(perfectClassLintError)
        assertEquals(badClassLintErrors.count(), 6)
        assertEquals(
            badClassLintErrors.first().detail,
            "Unexpected method name. Rename method badFunName1. " +
                "Method names is't compare with pattern $pattern"
        )
    }
}