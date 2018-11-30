package ruleset.utils

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import ruleset.enums.Node.CLASS
import ruleset.enums.Node.OBJECT
import ruleset.enums.Node.NODE
import ruleset.enums.Node.FUN

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
    fun createTextByNode(node: ASTNode): String {
        val text = text
            .replace(CLASS.text, CLASS.getNodeName(node))
            .replace(OBJECT.text, OBJECT.getNodeName(node))
            .replace(FUN.text, FUN.getNodeName(node))

        val name = NODE.getNodeName(node)

        if (name.isEmpty()) {
            return text.replace(NODE.text, "node")
        }

        return text.replace(NODE.text, name)
    }

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
}