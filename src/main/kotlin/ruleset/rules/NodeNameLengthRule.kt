package ruleset.rules

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens.IDENTIFIER
import ruleset.constant.ErrorTextCreatorDictionary
import ruleset.dto.Node
import ruleset.interfaces.ValidatedRule
import ruleset.utils.ErrorTextCreator

/**
 * Класс проверяет максимальную длину имени класса и функции
 */
class NodeNameLengthRule(private val nodes: List<Node>) : ValidatedRule("node-name-length-rule") {
    override fun validate() = nodes.all { it.type.isNotEmpty() && it.maxLength > 0 }

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType !== IDENTIFIER) return

        nodes.forEach {
            val nodeLength = node.text.length
            val element = it.type
            val nodeType = node.treeParent.elementType.toString()

            if (nodeType.toLowerCase() == element && nodeLength > it.maxLength) {
                val error = ErrorTextCreator(ErrorTextCreatorDictionary.ELEMENT_NAME_LENGTH)
                    .createTextByProps(nodeLength.toString(), it.maxLength.toString())

                emit(node.startOffset, error, false)
            }
        }
    }
}