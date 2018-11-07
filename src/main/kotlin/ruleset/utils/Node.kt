package ruleset.utils

import org.jetbrains.kotlin.KtNodeTypes.ANNOTATION_ENTRY
import org.jetbrains.kotlin.KtNodeTypes.VALUE_PARAMETER_LIST
import org.jetbrains.kotlin.KtNodeTypes.VALUE_PARAMETER
import org.jetbrains.kotlin.KtNodeTypes.MODIFIER_LIST
import org.jetbrains.kotlin.KtNodeTypes.CONSTRUCTOR_CALLEE
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.tree.IElementType
import org.jetbrains.kotlin.lexer.KtTokens.WHITE_SPACE
import org.jetbrains.kotlin.lexer.KtTokens.DOC_COMMENT
import org.jetbrains.kotlin.lexer.KtTokens.EOL_COMMENT
import org.jetbrains.kotlin.lexer.KtTokens.IDENTIFIER
import org.jetbrains.kotlin.lexer.KtTokens.BLOCK_COMMENT
import org.jetbrains.kotlin.kdoc.parser.KDocElementTypes.KDOC_SECTION
import org.jetbrains.kotlin.psi.psiUtil.parents

/**
 * Получить все узлы родителя
 *
 * @param anchor: ASTNode
 * @param type: IElementType
 *
 * @return : List<ASTNode>
 */
fun findAllChildNodes(anchor: ASTNode, type: IElementType): List<ASTNode> {
    val result = ArrayList<ASTNode>()
    var node = anchor.firstChildNode

    while (node != null) {
        if (node.elementType.equals(type)) {
            result.add(node)
        }

        result.addAll(findAllChildNodes(node, type))

        node = node.treeNext
    }

    return result
}

/**
 * Получить массив нод аргументов переданных в метод/конструктор
 *
 * @param node нода метода/конструктора
 *
 * @return : List<ASTNode>
 */
fun getParamNodes(node: ASTNode): List<ASTNode> {
    val paramList = node.findChildByType(VALUE_PARAMETER_LIST) ?: return listOf()

    return findAllChildNodes(paramList, VALUE_PARAMETER)
}

/**
 * Получить имя ноды по типу и IDENTIFIER
 *
 * @param node Сама нода либо нода находящаяся внутри нужной
 * @param nodeType Тип ноды имя которой нужно найти
 *
 * @return : String
 */
fun getNodeNameByIdentifier(node: ASTNode, nodeType: IElementType): String {
    var classNode = node

    if (node.elementType != nodeType) {
        classNode = node.parents().find { it.elementType == nodeType } ?: return "undefined"
    }

    return getNameByIdentifier(classNode)
}

/**
 * Получить наименование, чего либо, по объекту IDENTIFIER
 *
 * @param node: ASTNode
 *
 * @return : String
 */
fun getNameByIdentifier(node: ASTNode): String = node.findChildByType(IDENTIFIER)?.text ?: "undefined"

/**
 * Имеет модификатор проперти
 *
 * @param node Нода
 * @param modifier Модификатор
 */
fun hasPropertyModifier(node: ASTNode, modifier: IElementType): Boolean {
    if (node.firstChildNode.elementType != MODIFIER_LIST) return false

    return node.firstChildNode.findChildByType(modifier) != null
}

/**
 * Имеет аннотацию
 *
 * @param node Нода
 * @param annotationNames Имя аннотации
 *
 * @return : Boolean
 */
fun hasAnyAnnotation(node: ASTNode, vararg annotationNames: String) = findAllChildNodes(node, ANNOTATION_ENTRY).any {
    val annotationNameNode = it.findChildByType(CONSTRUCTOR_CALLEE) ?: return false

    annotationNames.contains(findAllChildNodes(annotationNameNode, IDENTIFIER).first().text)
}

/**
 * Получить тело узла исключив комментарии
 *
 * @param node: ASTNode
 *
 * @return : List<ASTNode>
 */
fun getBodyWithoutComments(node: ASTNode): List<ASTNode> {
    return findAllChildNodes(node, WHITE_SPACE)
        .filter {
            containsNewLine(it.text) &&
                it.treeParent.elementType !in listOf<IElementType>(
                DOC_COMMENT,
                KDOC_SECTION,
                EOL_COMMENT,
                BLOCK_COMMENT
            )
        }
}

/**
 * Получить имя пакета из import или package узла
 *
 * Работает для node.elementType === PACKAGE_DIRECTIVE
 *
 * @param node: ASTNode
 *
 * @return String
 */
fun getPackageName(node: ASTNode): String = node.chars
    .replace(Regex("(\n)|( )|(package)|(import)"), "")
    .split(".")
    .last()