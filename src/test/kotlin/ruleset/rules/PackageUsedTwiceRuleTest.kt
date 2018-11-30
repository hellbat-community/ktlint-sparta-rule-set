package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class PackageUsedTwiceRuleTest {
    @Test
    fun testRule() {
        val rule = PackageNotUseTwiceRule(getFileConfigJSON(TEST_PATH).packageNotUsedTwice)

        val complexClassOne = this::class.java.getResource("/package_used_twice_rule_test/loggerTestOne").readText()
        val complexClassTwo = this::class.java.getResource("/package_used_twice_rule_test/loggerTestTwo").readText()
        val complexClassWithoutLogger = this::class.java
            .getResource("/package_used_twice_rule_test/loggerTestTwo").readText()

        val errorLogger = rule.lint(complexClassOne).filter { error -> error.ruleId == rule.id }
        val nonError = rule.lint(complexClassTwo).filter { error -> error.ruleId == rule.id }
        val withoutLogger = rule.lint(complexClassWithoutLogger).filter { error -> error.ruleId == rule.id }

        Assert.assertEquals(errorLogger.count(), 1)
        Assert.assertEquals(nonError.count(), 0)
        Assert.assertEquals(withoutLogger.count(), 0)
    }
}