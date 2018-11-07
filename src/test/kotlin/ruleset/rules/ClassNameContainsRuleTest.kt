package ruleset.rules

import com.github.shyiko.ktlint.test.lint
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNull
import org.testng.Assert.assertNotNull
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class ClassNameContainsRuleTest {
    @Test
    fun ruleTest() {
        val rule = ClassNameContainsRule(getFileConfigJSON(TEST_PATH).classNameContains)

        val tableClass = this::class.java.getResource("/class_name_rule_test_data/Table.kt").readText()
        val uiTableClass = this::class.java.getResource("/class_name_rule_test_data/UITable.kt").readText()
        val testClass = this::class.java.getResource("/class_name_rule_test_data/Test.kt").readText()
        val testFormClass = this::class.java.getResource("/class_name_rule_test_data/TestForm.kt").readText()

        val tableLintError = rule.lint(tableClass).find { error -> error.ruleId == rule.id }

        assertNotNull(tableLintError)

        if (tableLintError != null) {
            assertEquals(tableLintError.detail, "Unexpected name, class Table must start with UI")
        }

        val uiTableLintError = rule.lint(uiTableClass).find { error -> error.ruleId == rule.id }

        assertNull(uiTableLintError)

        val testClassLintError = rule.lint(testClass).find { error -> error.ruleId == rule.id }

        assertNotNull(testClassLintError)

        if (testClassLintError != null) {
            assertEquals(testClassLintError.detail, "Unexpected name, class Test must end with Form")
        }

        val testFormClassLintError = rule.lint(testFormClass).find { error -> error.ruleId == rule.id }

        assertNull(testFormClassLintError)
    }
}