package ruleset.rules

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import ruleset.constant.ErrorTextCreatorDictionary.MAX_LINE_LENGTH
import ruleset.interfaces.ValidatedRule
import ruleset.utils.ErrorTextCreator

/**
 * Класс контролирует длинну строки
 */
class LineLengthRule(private val maxLineLength: Int) : ValidatedRule("max-line-length") {
    override fun validate() = maxLineLength > 0
    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node.elementType.toString() != "kotlin.FILE") return

        val error = ErrorTextCreator(MAX_LINE_LENGTH)

        node.chars.split("\n").forEach {
            if (it.length > maxLineLength) {
                emit(node.startOffset, error.createTextByProps(maxLineLength.toString()), false)
            }
        }
    }
}