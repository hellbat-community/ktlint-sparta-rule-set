package ruleset.rules

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.lexer.KtTokens.IDENTIFIER
import org.jetbrains.kotlin.KtNodeTypes.IMPORT_LIST
import org.jetbrains.kotlin.KtNodeTypes.IMPORT_DIRECTIVE
import org.jetbrains.kotlin.KtNodeTypes.PROPERTY
import org.jetbrains.kotlin.KtNodeTypes.BLOCK
import ruleset.interfaces.ValidatedRule

/**
 * Контролирует вызов елемента пакета дважды
 *
 * Правило состоит из алгоритма:
 *  - Проверить наличие пакета
 *  - Получить переменную
 *  - Проверить тело на вызов дважды, при успешном выполнении первых двух шагов
 */
class PackageNotUseTwiceRule(private val packageNames: MutableList<String>) : ValidatedRule("package-use-not-twice") {
    companion object {
        const val ERROR_TEXT = "You can't use package twice"
    }

    private lateinit var emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    private lateinit var currentVariableFinder: Regex
    private var variables: MutableList<String> = mutableListOf()
    private var hasImportChildren: Boolean = false

    override fun validate() = packageNames.isNotEmpty()

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        this.emit = emit

        if (node.elementType === IMPORT_LIST) {
            hasImportChildren = node.findChildByType(IMPORT_DIRECTIVE) !== null

            return
        }

        if (!hasImportChildren) return

        if (node.elementType === PROPERTY) {
            packageNames.forEach {
                val className = it.split(".").last()

                if (className.isNotEmpty() && hasPackageVariable(node, className)) {
                    variables.add(getPackageVariable(node))
                }
            }
        }

        if (node.elementType !== BLOCK) {
            return
        }

        variables.forEach {
            if (hasPackageTwice(node, it)) {
                emit(node.startOffset, ERROR_TEXT, false)
            }
        }
    }

    private fun hasPackageVariable(node: ASTNode, className: String) = Regex(className).find(node.chars) !== null

    private fun getPackageVariable(node: ASTNode) = node.findChildByType(IDENTIFIER)!!.text

    private fun hasPackageTwice(node: ASTNode, variable: String): Boolean {
        val charsWithoutSpace = node.chars
            .replace(Regex("(\\/\\*).*(\n|.)*(\\*\\/)"), "")
            .replace(Regex("(\\/\\/).*"), "")
            .replace(Regex(" "), "")
            .replace(Regex("\n\n\n\n\n"), "\n")
            .replace(Regex("\n\n\n\n"), "\n")
            .replace(Regex("\n\n\n"), "\n")
            .replace(Regex("\n\n"), "\n")
        /**
         * Первый Regex для блочных комментариев
         * Второй для строковых комментариев
         */

        val listOfLines = charsWithoutSpace.split("\n")

        currentVariableFinder = Regex(variable)

        return listOfLines.any {
            it.contains(currentVariableFinder) && listOfLines[listOfLines.indexOf(it) + 1].contains(currentVariableFinder)
        }
    }
}
