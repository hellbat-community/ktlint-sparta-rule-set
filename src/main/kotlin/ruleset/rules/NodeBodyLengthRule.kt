package ruleset.rules

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import ruleset.constant.ErrorBuilderText.ELEMENT_BODY_LENGTH
import ruleset.dto.Node
import ruleset.interfaces.ValidatedRule
import ruleset.utils.getBodyWithoutComments
import ruleset.utils.ErrorTextCreator

/**
 * Класс проверяет максимальную длину тела класса и функции
 */
class NodeBodyLengthRule(private val nodes: List<Node>) : ValidatedRule("node-body-max-length-rule") {
    override fun validate() = nodes.all { it.type.isNotEmpty() && it.maxLength > 0 }

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        nodes.forEach {
            val element = it.type
            val nodeType = node.elementType.toString()
            val linesSize = getBodyWithoutComments(node).size

            if (nodeType.toLowerCase() == element && linesSize > it.maxLength) {
                val error = ErrorTextCreator(ELEMENT_BODY_LENGTH)
                    .createTextByProps(linesSize.toString(), it.maxLength.toString())

                emit(node.startOffset, error, false)
            }
        }
    }
}
