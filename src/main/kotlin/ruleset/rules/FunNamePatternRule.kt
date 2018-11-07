package ruleset.rules

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens.IDENTIFIER
import org.jetbrains.kotlin.KtNodeTypes.FUN
import ruleset.constant.ErrorBuilderText.UNEXPECTED_METHOD_NAME
import ruleset.interfaces.ValidatedRule
import ruleset.utils.ErrorTextCreator

/**
 * Класс проверяет, что название функции соответсвует шаблону
 */
class FunNamePatternRule(private val pattern: String) : ValidatedRule("fun-name-pattern") {
    private val regExpPattern = Regex(pattern)
    override fun validate() = pattern.isNotEmpty()

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        node.treeParent ?: return

        if (node.elementType !== IDENTIFIER || node.treeParent.elementType !== FUN) {
            return
        }

        if (!regExpPattern.matches(node.text)) {
            val error = ErrorTextCreator(UNEXPECTED_METHOD_NAME)

            emit(node.startOffset, error.createText(node, pattern), false)
        }
    }
}
