package ruleset.util

import com.github.shyiko.ktlint.core.Rule
import com.github.shyiko.ktlint.test.lint
import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.testng.Assert
import org.testng.annotations.Test
import ruleset.rules.KDocRule
import ruleset.utils.getNameByIdentifier
import ruleset.utils.hasModifier

const val MODIFIER = "Modifier"

class NodeTest {
    @Test
    fun hasModifierTest() {
        val rule = NodeTestRule(MODIFIER)
        val modifierClass = this::class.java.getResource("/node_utils_test/ModifierTest").readText()
        val classLintErrorList = rule.lint(modifierClass).filter { error -> error.ruleId == rule.id }

        Assert.assertNotNull(classLintErrorList.find { e ->
            e.detail.contains("Has all modifiers of createOutputFactory method")
        })
    }
}

class NodeTestRule(private val type: String) : Rule("node-test") {
    private lateinit var emit: ((offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit)

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        this.emit = emit

        if (type === MODIFIER) {
            checkModifierFunction(node)
        }
    }

    private fun checkModifierFunction(node: ASTNode) {
        if (node.elementType !== KtNodeTypes.FUN) {
            return
        }

        val name = node.getNameByIdentifier()
        val private = node.hasModifier(KDocRule.PRIVATE)
        val override = node.hasModifier(KDocRule.OVERRIDE)

        if (name == "createOutputFactory" && private && override) {
            emit(node.startOffset, "Has all modifiers of createOutputFactory method", false)
        }
    }
}