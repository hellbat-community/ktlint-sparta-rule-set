package ruleset.rules

import com.github.shyiko.ktlint.core.LintError
import com.github.shyiko.ktlint.test.lint
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotNull
import org.testng.Assert.assertNull
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class KDocRuleTest {
    private val rule = KDocRule(getFileConfigJSON(TEST_PATH).kotlinDoc)

    @Test
    fun constructorsTest() {
        val badConstructor = this::class.java.getResource("/kotlin_doc_rule_test_data/ConstructorTest.kt").readText()
        val lintErrors: List<LintError>

        lintErrors = rule.lint(badConstructor).filter { error -> error.ruleId == rule.id }
        assertEquals(lintErrors.count(), 8)
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc properties of BadConstructorTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc properties of BadConstructorATest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc properties of BadConstructorBTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc properties of BadConstructorCTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of node." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of node." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc description of node." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc arguments of node." })
    }

    @Test
    fun publicMethodsTest() {
        val lintErrors: List<LintError>
        val badPublicFun = this::class.java.getResource("/kotlin_doc_rule_test_data/PublicFunTest.kt").readText()

        lintErrors = rule.lint(badPublicFun).filter { error -> error.ruleId == rule.id }
        assertEquals(lintErrors.count(), 8)
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of notLogger." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of badFun." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of badAFun." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc arguments of badBFun." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc arguments of badCFun." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of badDun." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc description of badEun." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc arguments of badFFun." })
    }

    @Test
    fun classTest() {
        val lintErrors: List<LintError>
        val badClass = this::class.java.getResource("/kotlin_doc_rule_test_data/ClassTest.kt").readText()

        lintErrors = rule.lint(badClass).filter { error -> error.ruleId == rule.id }
        assertEquals(lintErrors.count(), 5)
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of objectTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of ClassATest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of ClassBTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc description of ClassCTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc description of ClassDTest." })
    }

    @Test
    fun classPropertyTest() {
        val lintErrors: List<LintError>
        val badClassProperties = this::class.java
            .getResource("/kotlin_doc_rule_test_data/ClassPropertiesTest.kt").readText()

        lintErrors = rule.lint(badClassProperties).filter { error -> error.ruleId == rule.id }
        assertEquals(lintErrors.count(), 4)
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of valB." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of badArgument." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc of badArgumentTo." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc description of badEmptyArgument." })
    }

    @Test
    fun perfectTest() {
        val goodTests = this::class.java.getResource("/kotlin_doc_rule_test_data/TestsTest.kt").readText()
        val perfectClass = this::class.java.getResource("/PerfectTestClass.kt").readText()

        assertNull(rule.lint(perfectClass).find { error -> error.ruleId == rule.id })
        assertNull(rule.lint(goodTests).find { error -> error.ruleId == rule.id })
    }
}