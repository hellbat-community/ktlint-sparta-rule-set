package ruleset.rules

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.KtNodeTypes.VALUE_PARAMETER_LIST
import org.jetbrains.kotlin.KtNodeTypes.FUN
import ruleset.utils.ErrorTextCreator
import ruleset.constant.ErrorTextCreatorDictionary.FUN_MAX_ARGUMENTS
import ruleset.interfaces.ValidatedRule

/**
 * Класс провереят колличество аругментов в функции
 *
 * Алгоритм:
 * - Находим узел с объявлением аргументов функции
 * - Проверяем кол-во аргументов
 */
class FunArgumentsRule(private val maxArguments: Int) : ValidatedRule("max-fun-arguments") {
    override fun validate() = maxArguments > 0
    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        node.treeParent ?: return

        if (node.elementType !== VALUE_PARAMETER_LIST || node.treeParent.elementType !== FUN) return

        if (hasMoreThanAllowedArguments(node)) {
            val error = ErrorTextCreator(FUN_MAX_ARGUMENTS)

            emit(node.startOffset, error.createText(node.treePrev ?: node, maxArguments.toString()), false)
        }
    }

    private fun hasMoreThanAllowedArguments(node: ASTNode) = node.chars.split(",").size > maxArguments
}