package ruleset.rules

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens.IDENTIFIER
import org.jetbrains.kotlin.lexer.KtTokens.OROR
import org.jetbrains.kotlin.lexer.KtTokens.ANDAND
import org.jetbrains.kotlin.KtNodeTypes.FUN
import org.jetbrains.kotlin.KtNodeTypes.BLOCK
import org.jetbrains.kotlin.KtNodeTypes.IF
import org.jetbrains.kotlin.KtNodeTypes.FOR
import org.jetbrains.kotlin.KtNodeTypes.WHEN
import org.jetbrains.kotlin.KtNodeTypes.WHILE
import org.jetbrains.kotlin.KtNodeTypes.DO_WHILE
import org.jetbrains.kotlin.com.intellij.psi.tree.IElementType
import ruleset.constant.ErrorBuilderText.CYCLOMATIC_COMPLEXITY
import ruleset.interfaces.ValidatedRule
import ruleset.utils.ErrorTextCreator

/**
 * Класс провереят цикломатическую сложность
 *
 */
class CycloComplexityRule(private val cyclomaticComplexity: Int) : ValidatedRule("cyclomatic-complexity") {
    private var currentComplexity = 1
    private var methodName = ""

    companion object {
        val COMPLEX_INC_TYPES: ArrayList<IElementType> = arrayListOf(IF, OROR, ANDAND, FOR, WHEN, WHILE, DO_WHILE)
    }

    override fun validate() = cyclomaticComplexity > 0

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        val parent = node.treeParent

        parent ?: return
        parent.treeParent ?: return

        val lastChildOfParent = parent.lastChildNode

        if (node.elementType === IDENTIFIER && parent.elementType === FUN) {
            methodName = node.text
            currentComplexity = 1
        }

        if (methodName.isNotEmpty() && COMPLEX_INC_TYPES.contains(node.elementType)) {
            currentComplexity++
        }

        if (parent.elementType == BLOCK && parent.treeParent.elementType == FUN && lastChildOfParent == node) {
            if (methodName.isNotEmpty() && currentComplexity > cyclomaticComplexity) {
                val errorText = ErrorTextCreator(CYCLOMATIC_COMPLEXITY).createText(node, currentComplexity.toString())

                emit(node.startOffset, errorText, false)
            }

            methodName = ""
            currentComplexity = 1
        }
    }
}
