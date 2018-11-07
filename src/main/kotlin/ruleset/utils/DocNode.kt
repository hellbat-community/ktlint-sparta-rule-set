package ruleset.utils

import org.jetbrains.kotlin.KtNodeTypes.FUN
import org.jetbrains.kotlin.KtNodeTypes.PRIMARY_CONSTRUCTOR
import org.jetbrains.kotlin.KtNodeTypes.SECONDARY_CONSTRUCTOR
import org.jetbrains.kotlin.KtNodeTypes.OBJECT_DECLARATION
import org.jetbrains.kotlin.KtNodeTypes.PROPERTY
import org.jetbrains.kotlin.KtNodeTypes.CLASS_BODY
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens.KDOC
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens.TAG_NAME
import org.jetbrains.kotlin.lexer.KtTokens.COMPANION_KEYWORD
import org.jetbrains.kotlin.lexer.KtTokens.CONST_KEYWORD
import org.jetbrains.kotlin.lexer.KtTokens.PRIVATE_KEYWORD
import org.jetbrains.kotlin.lexer.KtTokens.OVERRIDE_KEYWORD
import org.jetbrains.kotlin.kdoc.parser.KDocElementTypes.KDOC_SECTION

/**
 * Получить корневую ноду доки
 *
 * @param node: ASTNode
 *
 * @return : ASTNode?
 */
fun getRootNode(node: ASTNode): ASTNode? {
    if (node.firstChildNode.elementType === KDOC) {
        return node.firstChildNode
    }

    val parent = node.treeParent.firstChildNode

    if (parent.elementType === KDOC) {
        return parent
    }

    if (parent.elementType !== OBJECT_DECLARATION) {
        return null
    }

    val parentOfParentChild = node.treeParent.treeParent.firstChildNode

    if (parentOfParentChild.elementType === KDOC) {
        return parentOfParentChild
    }

    return null
}

/**
 * Получить массив нод тэгов доки
 *
 * @param node: ASTNode
 *
 * @return : List<ASTNode>
 */
fun getDocTagNames(node: ASTNode): List<ASTNode> {
    val tagNodes: MutableList<ASTNode> = mutableListOf()

    findAllChildNodes(getRootNode(node)!!, KDOC_SECTION).forEach {
        tagNodes.addAll(findAllChildNodes(it, TAG_NAME))
    }

    return tagNodes
}

/**
 * Нужна ли проверка по конструктору класса
 *
 * @param node: ASTNode
 *
 * @return : Boolean
 */
fun needClassConstructorCheck(node: ASTNode): Boolean =
    node.elementType === PRIMARY_CONSTRUCTOR || node.elementType === SECONDARY_CONSTRUCTOR

/**
 * Нужна ли проверка по объекту
 *
 * @param node: ASTNode
 *
 * @return : Boolean
 */
fun needObjectCheck(node: ASTNode): Boolean =
    node.elementType == OBJECT_DECLARATION && !hasPropertyModifier(node, COMPANION_KEYWORD)

/**
 * Нужна ли проверка по проперти класса
 *
 * @param node: ASTNode
 *
 * @return : Boolean
 */
fun needPropertyCheck(node: ASTNode): Boolean {
    if (node.elementType != PROPERTY || node.treeParent.elementType != CLASS_BODY) {
        return false
    }

    return !hasPropertyModifier(node, CONST_KEYWORD) &&
        !hasPropertyModifier(node, PRIVATE_KEYWORD) &&
        !hasAnyAnnotation(node, "Mock", "InjectMocks")
}

/**
 * Нужна ли проверка по методу
 *
 * @param node: ASTNode
 *
 * @return : Boolean
 */
fun needFunCheck(node: ASTNode): Boolean {
    if (node.elementType != FUN) return false

    return !hasPropertyModifier(node, PRIVATE_KEYWORD) &&
        !hasAnyAnnotation(node, "BeforeMethod", "Test", "DataProvider") &&
        !hasPropertyModifier(node, OVERRIDE_KEYWORD)
}