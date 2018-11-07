package ruleset.rules

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens.LBRACE
import org.jetbrains.kotlin.KtNodeTypes.BLOCK
import org.jetbrains.kotlin.KtNodeTypes.LAMBDA_EXPRESSION
import org.jetbrains.kotlin.KtNodeTypes.LAMBDA_ARGUMENT
import org.jetbrains.kotlin.KtNodeTypes.CALL_EXPRESSION
import org.jetbrains.kotlin.KtNodeTypes.FUNCTION_LITERAL
import org.jetbrains.kotlin.KtNodeTypes.DOT_QUALIFIED_EXPRESSION
import ruleset.constant.ErrorBuilderText.FUN_BLOCK_NESTING
import ruleset.interfaces.ValidatedRule
import ruleset.utils.ErrorTextCreator

/**
 * Класс проверяет максимальную вложенность
 */
class MaxDepthNestedBlocksRule(private val maxDepthNested: Int) : ValidatedRule("max-depth-nested") {
    companion object {
        /**
         * Последовательность блоков лямбда функции с телом.
         * Например: some.forEach { run { ... } }
         */
        val lambdaBlockLine = listOf(
            LAMBDA_EXPRESSION,
            LAMBDA_ARGUMENT,
            CALL_EXPRESSION,
            BLOCK,
            FUNCTION_LITERAL,
            LAMBDA_EXPRESSION,
            LAMBDA_ARGUMENT,
            CALL_EXPRESSION,
            DOT_QUALIFIED_EXPRESSION
        )
    }

    override fun validate() = maxDepthNested > 0

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        node.treeParent ?: return

        if (node.elementType == LBRACE && hasDepthNested(node)) {
            val error = ErrorTextCreator(FUN_BLOCK_NESTING)

            emit(node.startOffset, error.createText(node, maxDepthNested.toString()), false)
        }
    }

    private fun hasDepthNested(node: ASTNode): Boolean {
        var blocksCounter = 0
        var parent = node.treeParent

        while (parent !== null) {
            parent = parent.treeParent ?: return false

            val parentType = parent.elementType

            if (isLambdaExpressionWithBlock(parent)) {
                blocksCounter--
            }

            if (parentType === BLOCK) {
                blocksCounter++
            }

            if (parentType === BLOCK && blocksCounter > maxDepthNested) return true
        }

        return false
    }

    private fun isLambdaExpressionWithBlock(node: ASTNode): Boolean {
        var target = node

        return lambdaBlockLine.all {
            if (target.elementType !== it) return false

            target = target.treeParent

            true
        }
    }
}
