package ruleset.utils

import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.KtNodeTypes.OBJECT_DECLARATION
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import ruleset.constant.ErrorBuilderNameType.CLASS
import ruleset.constant.ErrorBuilderNameType.OBJECT
import ruleset.constant.ErrorBuilderNameType.NODE
import ruleset.constant.ErrorBuilderNameType.FUN

/**
 * Создает тект ошибки по шаблону
 *
 * @param template основной шаблон
 */
class ErrorTextCreator(private val template: String) {
    private var text: String

    init {
        text = template
    }

    /**
     * Получить текст ошибки, по параметрам
     *
     * @param node: ASTNode
     * @param properties: vararg String
     *
     * @return : String
     */
    fun createText(node: ASTNode, vararg properties: String): String {
        text = createTextByNode(node)

        return createTextByProps(*properties)
    }

    /**
     * Получить текст ошибки по узлу
     *
     * @param node: ASTNode
     *
     * @return : String
     */
    fun createTextByNode(node: ASTNode): String = text
        .replace(CLASS, getValue(CLASS, node))
        .replace(OBJECT, getValue(OBJECT, node))
        .replace(FUN, getValue(FUN, node))
        .replace(NODE, getValue(NODE, node))

    /**
     * Получить текст ошибки по массиву свойств
     *
     * @param properties
     *
     * @return : String
     */
    fun createTextByProps(vararg properties: String): String {
        properties.forEach {
            text = text.replace("%" + (properties.indexOf(it) + 1), it)
        }

        return text
    }

    private fun getValue(key: String, node: ASTNode): String = when (key) {
        CLASS -> getNodeNameByIdentifier(node, KtNodeTypes.CLASS)
        OBJECT -> getNodeNameByIdentifier(node, OBJECT_DECLARATION)
        FUN -> getNodeNameByIdentifier(node, KtNodeTypes.FUN)
        NODE -> getNameByIdentifier(node)
        else -> getNameByIdentifier(node)
    }
}
