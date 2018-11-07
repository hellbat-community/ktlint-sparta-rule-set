package ruleset.rules

import org.jetbrains.kotlin.KtNodeTypes.PACKAGE_DIRECTIVE
import org.jetbrains.kotlin.KtNodeTypes.CLASS
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import ruleset.constant.ErrorBuilderText.CLASS_NAME
import ruleset.dto.ClassContains
import ruleset.interfaces.ValidatedRule
import ruleset.utils.getPackageName
import ruleset.utils.getNameByIdentifier
import ruleset.utils.ErrorTextCreator
import ruleset.utils.upFirstChar

/**
 * Класс проверки именования классов по заданным параметрам
 *
 * Алгоритм:
 * - Получить имя пакета
 * - Найти класс
 * - Выполнить все проверки, по заданным окончаниям, если они требуются
 *
 */
class ClassNameContainsRule(
    private val contains: MutableList<ClassContains> = mutableListOf()
) : ValidatedRule("class-name-contains") {
    private var packageName: String = ""
    private lateinit var emit: ((offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit)

    override fun validate() = contains.all {
        it.namePart.isNotEmpty() && it.packageName.isNotEmpty() && it.paramName.isNotEmpty()
    }

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        this.emit = emit

        if (node.elementType === PACKAGE_DIRECTIVE) {
            packageName = getPackageName(node)

            return
        }

        if (node.elementType !== CLASS) {
            return
        }

        checkContains(node)
    }

    private fun checkContains(node: ASTNode) {
        val error = ErrorTextCreator(CLASS_NAME)
        val className = getNameByIdentifier(node)

        contains.filter {
            packageName == it.packageName
        }.forEach {
            if (it.paramName == "end" && isNotValidEndName(className, it.namePart)) {
                emit(node.startOffset, error.createText(node, it.paramName, upFirstChar(it.namePart)), false)
            }

            if (it.paramName == "start" && isNotValidStartName(className, it.namePart)) {
                emit(node.startOffset, error.createText(node, it.paramName, upFirstChar(it.namePart)), false)
            }
        }
    }

    private fun isNotValidEndName(className: String, end: String): Boolean = !className.endsWith(end, true)

    private fun isNotValidStartName(className: String, start: String): Boolean = !className.startsWith(start, true)
}
