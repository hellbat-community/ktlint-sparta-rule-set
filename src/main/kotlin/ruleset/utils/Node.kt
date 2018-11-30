package ruleset.utils

import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.KtNodeTypes.ANNOTATION_ENTRY
import org.jetbrains.kotlin.KtNodeTypes.VALUE_PARAMETER_LIST
import org.jetbrains.kotlin.KtNodeTypes.VALUE_PARAMETER
import org.jetbrains.kotlin.KtNodeTypes.MODIFIER_LIST
import org.jetbrains.kotlin.KtNodeTypes.CONSTRUCTOR_CALLEE
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.tree.IElementType
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens
import org.jetbrains.kotlin.kdoc.parser.KDocElementTypes
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
 * @param type: IElementType
 *
 * @return : List<ASTNode>
 */
fun ASTNode.findAllChildNodes(type: IElementType): List<ASTNode> {
    val result = ArrayList<ASTNode>()
    var node = this.firstChildNode

    while (node != null) {
        if (node.elementType.equals(type)) {
            result.add(node)
        }

        result.addAll(node.findAllChildNodes(type))

        node = node.treeNext
    }

    return result
}

/**
 * Получить массив нод аргументов переданных в метод/конструктор
 *
 * @return : List<ASTNode>
 */
fun ASTNode.getParamNodes(): List<ASTNode> {
    val paramNode = this.findChildByType(VALUE_PARAMETER_LIST) ?: return listOf()

    return paramNode.findAllChildNodes(VALUE_PARAMETER)
}

/**
 * Получить имя ноды по типу и IDENTIFIER
 *
 * @param nodeType Тип ноды имя которой нужно найти
 *
 * @return : String
 */
fun ASTNode.getNodeNameByIdentifier(nodeType: IElementType): String {
    var classNode = this

    if (this.elementType != nodeType) {
        classNode = this.parents().find { it.elementType == nodeType } ?: return ""
    }

    return classNode.getNameByIdentifier()
}

/**
 * Получить наименование, чего либо, по объекту IDENTIFIER
 *
 * @return : String
 */
fun ASTNode.getNameByIdentifier(): String = this.findChildByType(IDENTIFIER)?.text ?: ""

/**
 * Имеет модификатор проперти
 *
 * @param modifier Модификатор
 */
fun ASTNode.hasPropertyModifier(modifier: IElementType): Boolean {
    if (this.firstChildNode.elementType != MODIFIER_LIST) return false

    return this.firstChildNode.findChildByType(modifier) != null
}

/**
 * Имеет аннотацию
 *
 * @param annotationNames Имя аннотации
 *
 * @return : Boolean
 */
fun ASTNode.hasAnyAnnotation(vararg annotationNames: String) = this.findAllChildNodes(ANNOTATION_ENTRY).any {
    val constructorCalleeNode = it.findChildByType(CONSTRUCTOR_CALLEE) ?: return false

    annotationNames.contains(constructorCalleeNode.findAllChildNodes(IDENTIFIER).first().text)
}

/**
 * Получить тело узла исключив комментарии
 *
 * @return : List<ASTNode>
 */
fun ASTNode.getBodyWithoutComments(): List<ASTNode> {
    return this.findAllChildNodes(WHITE_SPACE)
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
 * @return String
 */
fun ASTNode.getPackageName(): String = this.chars
    .replace(Regex("(\n)|( )|(package)|(import)"), "")
    .split(".")
    .last()

/**
 * Проверить, есть ли модификатор узла по строке с именем
 *
 * @param modifier Строка с именем подификатора
 *
 * @return : Boolean
 */
fun ASTNode.hasModifier(modifier: String) = this.findChildByType(MODIFIER_LIST)?.chars?.contains(modifier) ?: false

/**
 * Получить документацию узла
 *
 * @return : ASTNode?
 */
fun ASTNode.getRootDocNode(): ASTNode? {
    if (this.firstChildNode.elementType === KDocTokens.KDOC) {
        return this.firstChildNode
    }

    val parent = this.treeParent.firstChildNode

    if (parent.elementType === KDocTokens.KDOC) {
        return parent
    }

    if (parent.elementType !== KtNodeTypes.OBJECT_DECLARATION) {
        return null
    }

    val parentOfParentChild = this.treeParent.treeParent.firstChildNode

    if (parentOfParentChild.elementType === KDocTokens.KDOC) {
        return parentOfParentChild
    }

    return null
}

/**
 * Получить массив нод тэгов доки
 *
 * @return : List<ASTNode>
 */
fun ASTNode.getDocTagNodes(): List<ASTNode> {
    val tagNodes: MutableList<ASTNode> = mutableListOf()

    this.getRootDocNode()
        ?.findAllChildNodes(KDOC_SECTION)
        ?.forEach {
            tagNodes.addAll(it.findAllChildNodes(KDocElementTypes.KDOC_TAG))
        }
        ?: return tagNodes

    return tagNodes
}