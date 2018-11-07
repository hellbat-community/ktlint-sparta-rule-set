package ruleset.rules

import com.github.shyiko.ktlint.core.LintError
import com.github.shyiko.ktlint.test.lint
import org.testng.Assert.assertEquals
import org.testng.Assert.assertNotNull
import org.testng.Assert.assertNull
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class JavaDocsRuleTest {
    private val rule = JavaDocsRule(getFileConfigJSON(TEST_PATH).javaDoc)

    @Test
    fun constructorsTest() {
        val badConstructor = this::class.java.getResource("/java_docs_rule_test_data/ConstructorTest.kt").readText()
        val lintErrors: List<LintError>

        lintErrors = rule.lint(badConstructor).filter { error -> error.ruleId == rule.id }
        assertEquals(lintErrors.count(), 7)
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for constructor properties of class BadConstructorTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for constructor properties of class BadConstructorATest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for constructor property arg of class BadConstructorBTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for constructor description property arg of class BadConstructorCTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for constructor of class BadConstructorDTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for constructor of class BadConstructorETest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for constructor properties of class BadConstructorFTest." })
    }

    @Test
    fun publicMethodsTest() {
        val lintErrors: List<LintError>
        val badPublicFun = this::class.java.getResource("/java_docs_rule_test_data/PublicFunTest.kt").readText()

        lintErrors = rule.lint(badPublicFun).filter { error -> error.ruleId == rule.id }
        assertEquals(lintErrors.count(), 8)
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for method notLogger of class undefined." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for method badFun of class PublicFunTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for method badAFun of class PublicFunTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for method badBFun arguments of class PublicFunTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for argument test of method badCFun of class PublicFunTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for method badDun of class PublicFunTest." })
        assertNotNull(lintErrors.find { it.detail == "Add description for method badEun of class PublicFunTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for argument description arg of method badFFun of class PublicFunTest." })
    }

    @Test
    fun classTest() {
        val lintErrors: List<LintError>
        val badClass = this::class.java.getResource("/java_docs_rule_test_data/ClassTest.kt").readText()

        lintErrors = rule.lint(badClass).filter { error -> error.ruleId == rule.id }
        assertEquals(lintErrors.count(), 5)
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for object objectTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for class ClassATest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for class ClassBTest." })
        assertNotNull(lintErrors.find { it.detail == "Add description for class ClassCTest in JavaDoc." })
        assertNotNull(lintErrors.find { it.detail == "Add description for class ClassDTest in JavaDoc." })
    }

    @Test
    fun classPropertyTest() {
        val lintErrors: List<LintError>
        val badClassProperties = this::class.java.getResource("/java_docs_rule_test_data/ClassPropertiesTest.kt").readText()

        lintErrors = rule.lint(badClassProperties).filter { error -> error.ruleId == rule.id }
        assertEquals(lintErrors.count(), 4)
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for property valB of object objectPropertiesTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for property badArgument of class ClassPropertiesTest." })
        assertNotNull(lintErrors.find { it.detail == "Not found JavaDoc for property badArgumentTo of class ClassPropertiesTest." })
        assertNotNull(lintErrors.find { it.detail == "Add description for property badEmptyArgument of class ClassPropertiesTest in JavaDoc." })
    }

    @Test
    fun perfectTest() {
        val goodTests = this::class.java.getResource("/java_docs_rule_test_data/TestsTest.kt").readText()
        val perfectClass = this::class.java.getResource("/PerfectTestClass.kt").readText()

        assertNull(rule.lint(perfectClass).find { error -> error.ruleId == rule.id })
        assertNull(rule.lint(goodTests).find { error -> error.ruleId == rule.id })
    }
}
