package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert.assertNull
import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class MaxDepthNestedBlocksRuleTest {
    @Test
    fun ruleTest() {
        val rule = MaxDepthNestedBlocksRule(getFileConfigJSON(TEST_PATH).maxDepthNested)

        val perfectClass = this::class.java.getResource("/PerfectTestClass.kt").readText()
        val testClass = this::class.java.getResource("/max_depth_nested_test_data/Test.kt").readText()
        val perfectClassLintError = rule.lint(perfectClass).find { error -> error.ruleId == rule.id }
        val lintErrorList = rule.lint(testClass).filter { error -> error.ruleId == rule.id }

        assertNull(perfectClassLintError)
        assertEquals(lintErrorList.size, 4)
    }
}
