package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class CycloComplexityRuleTest {
    @Test
    fun ruleTest() {
        val rule = CycloComplexityRule(getFileConfigJSON(TEST_PATH).cyclomaticComplexity)
        val complexClass = this::class.java.getResource("/cyclo_complex_test_data/Complex").readText()
        val lintErrorList = rule.lint(complexClass).filter { error -> error.ruleId == rule.id }

        assertEquals(lintErrorList.size, 3)
        assertNotNull(lintErrorList.find { e -> e.detail.contains("complexFunc is 6") })
        assertNotNull(lintErrorList.find { e -> e.detail.contains("funcA is 5") })
        assertNotNull(lintErrorList.find { e -> e.detail.contains("funcB is 6") })
    }
}