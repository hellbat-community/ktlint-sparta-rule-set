package ruleset.rules

import org.jetbrains.kotlin.KtNodeTypes.CLASS
import org.jetbrains.kotlin.KtNodeTypes.FUN
import org.jetbrains.kotlin.KtNodeTypes.OBJECT_DECLARATION
import org.jetbrains.kotlin.KtNodeTypes.CLASS_BODY
import org.jetbrains.kotlin.KtNodeTypes.PROPERTY
import org.jetbrains.kotlin.KtNodeTypes.SECONDARY_CONSTRUCTOR
import org.jetbrains.kotlin.KtNodeTypes.PRIMARY_CONSTRUCTOR
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens.TEXT
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens.TAG_NAME
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens.MARKDOWN_LINK
import org.jetbrains.kotlin.kdoc.parser.KDocElementTypes.KDOC_SECTION
import org.jetbrains.kotlin.lexer.KtTokens.CONST_KEYWORD
import org.jetbrains.kotlin.lexer.KtTokens.PRIVATE_KEYWORD
import org.jetbrains.kotlin.lexer.KtTokens.IDENTIFIER
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes.FILE
import ruleset.constant.ErrorTextCreatorDictionary.NOT_FOUND_DOC
import ruleset.constant.ErrorTextCreatorDictionary.NOT_FOUND_DOC_ARGUMENTS
import ruleset.constant.ErrorTextCreatorDictionary.NOT_FOUND_DOC_COMMENT
import ruleset.constant.ErrorTextCreatorDictionary.NOT_FOUND_DOC_PROP
import ruleset.interfaces.ValidatedRule
import ruleset.utils.hasModifier
import ruleset.utils.hasPropertyModifier
import ruleset.utils.hasAnyAnnotation
import ruleset.utils.findAllChildNodes
import ruleset.utils.getRootDocNode
import ruleset.utils.ErrorTextCreator
import ruleset.utils.getDocTagNodes
import ruleset.utils.getParamNodes

/**
 * Правило проверяющее наличие kotlin-doc у классов и объектов, их полей, конструкторов и публичных методов с
 * аргументами.
 *
 */
class KDocRule(private val needKDoc: Boolean) : ValidatedRule("kotlin-doc") {
    private lateinit var emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    private var skipChecks: Boolean = false

    companion object {
        const val COMPANION = "companion"
        const val OVERRIDE = "override"
        const val PRIVATE = "private"
        const val MOCK = "Mock"
        const val INJECT_MOCKS = "InjectMocks"
        const val TEST = "Test"
        const val PROPERTY_ANNOTATE = "@property"
        const val PARAM_ANNOTATE = "@param"
    }

    override fun validate() = needKDoc

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        this.emit = emit

        if (hasSkipChecks(node)) return

        if (node.elementType === CLASS) {
            checkClass(node)
        }

        if (node.elementType === OBJECT_DECLARATION && !node.hasModifier(COMPANION)) {
            checkNode(node)
        }

        if (node.elementType === PROPERTY && node.treeParent.elementType === CLASS_BODY) {
            checkProperties(node)
        }

        if (node.elementType !== FUN) return
        if (node.hasModifier(PRIVATE) || node.hasModifier(OVERRIDE)) return

        checkNode(node)
    }

    private fun checkClass(node: ASTNode) {
        checkNode(node)

        val constructor = node.findChildByType(CLASS_BODY)?.findChildByType(SECONDARY_CONSTRUCTOR)

        if (constructor === null) return

        checkNode(constructor)
    }

    private fun checkProperties(node: ASTNode) {
        if (node.hasPropertyModifier(CONST_KEYWORD)) return
        if (node.hasPropertyModifier(PRIVATE_KEYWORD)) return
        if (node.hasAnyAnnotation(MOCK, INJECT_MOCKS)) return

        checkNode(node)
    }

    private fun hasSkipChecks(node: ASTNode): Boolean {
        if (node.elementType === FILE) {
            skipChecks = false
        }

        if (node.elementType === CLASS) {
            skipChecks = node.findAllChildNodes(FUN).any { node.hasAnyAnnotation(TEST) }
        }

        return skipChecks
    }

    private fun hasBlockCommentDescription(node: ASTNode): Boolean {
        val comment = node.getRootDocNode()?.findChildByType(KDOC_SECTION)?.firstChildNode?.treeNext
            ?: return false

        return comment.elementType === TEXT
    }

    private fun checkNode(node: ASTNode) {
        if (node.getRootDocNode() === null) {
            emit(node.startOffset, ErrorTextCreator(NOT_FOUND_DOC).createTextByNode(node), false)

            return
        }

        if (!hasBlockCommentDescription(node)) {
            emit(node.startOffset, ErrorTextCreator(NOT_FOUND_DOC_COMMENT).createTextByNode(node), false)

            return
        }

        if (node.elementType === FUN || node.elementType === SECONDARY_CONSTRUCTOR) {
            checkParams(node)

            return
        }

        if (node.elementType === CLASS) {
            checkPrimaryConstructor(node)

            return
        }
    }

    private fun checkPrimaryConstructor(node: ASTNode) {
        val primaryConstructor = node.findChildByType(PRIMARY_CONSTRUCTOR)

        if (primaryConstructor === null) return

        val params = primaryConstructor.getParamNodes()

        if (params.isEmpty()) return

        val docTagsProperty = getDocNodesByTag(node, PROPERTY_ANNOTATE)

        val hasAllPropertiesInDocTags = params.all { param ->
            docTagsProperty.any { node ->
                hasValidTagInfo(node, param.findChildByType(IDENTIFIER)?.chars.toString())
            }
        }

        if (!hasAllPropertiesInDocTags) {
            emit(node.startOffset, ErrorTextCreator(NOT_FOUND_DOC_PROP).createTextByNode(node), false)
        }
    }

    private fun checkParams(node: ASTNode) {
        val params = node.getParamNodes()

        if (params.isEmpty()) return

        val docTagsParam = getDocNodesByTag(node, PARAM_ANNOTATE)

        val hasAllArgumentsInDocTags = params.all { param ->
            docTagsParam.any { node -> hasValidTagInfo(node, param.firstChildNode.chars.toString()) }
        }

        if (!hasAllArgumentsInDocTags) {
            emit(node.startOffset, ErrorTextCreator(NOT_FOUND_DOC_ARGUMENTS).createTextByNode(node), false)
        }
    }

    private fun getDocNodesByTag(node: ASTNode, tag: String) = node.getDocTagNodes().filter {
        it.findChildByType(TAG_NAME)?.chars == tag
    }

    private fun hasValidTagInfo(node: ASTNode, tagName: String) =
        node.findChildByType(MARKDOWN_LINK)?.chars?.contains(tagName) ?: false &&
            node.findChildByType(TEXT)?.chars?.isNotEmpty() ?: false
}