package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class LineLengthRuleTest {
    @Test
    fun ruleTest() {
        val rule = LineLengthRule(getFileConfigJSON(TEST_PATH).maxLineLength)
        val badClass = this::class.java.getResource("/line_controller_test_data/test").readText()
        val badClassLintErrors = rule.lint(badClass).filter { error -> error.ruleId == rule.id }

        Assert.assertEquals(badClassLintErrors.count(), 1)
        Assert.assertNotNull(badClassLintErrors.find {
            it.detail == "Line can't be more than 120 symbols"
        })
    }
}