package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class LayersBanTest {
    private var rule = LayersBanRule(getFileConfigJSON(TEST_PATH).banLayers)

    @Test
    fun testPL() {
        val classEventPL = this::class.java.getResource("/layer_ban_rule_test_data/testPL").readText()
        val checkPL = rule.lint(classEventPL).filter { error -> error.ruleId == rule.id }

        Assert.assertEquals(checkPL.count(), 9)
    }

    @Test
    fun testDA() {
        val classDA = this::class.java.getResource("/layer_ban_rule_test_data/testDA").readText()
        val checkDA = rule.lint(classDA).filter { error -> error.ruleId == rule.id }

        Assert.assertEquals(checkDA.count(), 9)
    }

    @Test
    fun testBS() {
        val classBS = this::class.java.getResource("/layer_ban_rule_test_data/testBS").readText()
        val checkBS = rule.lint(classBS).filter { error -> error.ruleId == rule.id }

        Assert.assertEquals(checkBS.count(), 3)
    }

    @Test
    fun testWithoutErrors() {
        val perfectClass = this::class.java.getResource("/PerfectTestClass.kt").readText()
        val withoutErrors = rule.lint(perfectClass).filter { error -> error.ruleId == rule.id }

        Assert.assertEquals(withoutErrors.count(), 0)
    }
}