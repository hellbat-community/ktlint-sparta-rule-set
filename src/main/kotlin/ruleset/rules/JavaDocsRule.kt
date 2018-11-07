package ruleset.rules

import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.kdoc.lexer.KDocTokens.TEXT
import org.jetbrains.kotlin.KtNodeTypes.OBJECT_DECLARATION
import org.jetbrains.kotlin.KtNodeTypes.CLASS
import org.jetbrains.kotlin.KtNodeTypes.FUN
import org.jetbrains.kotlin.kdoc.parser.KDocElementTypes.KDOC_SECTION
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes.FILE
import ruleset.utils.ErrorTextCreator
import ruleset.constant.ErrorBuilderText.CLASS_DOC
import ruleset.constant.ErrorBuilderText.CLASS_DOC_COMMENT
import ruleset.constant.ErrorBuilderText.CLASS_PROP
import ruleset.constant.ErrorBuilderText.CLASS_PROP_COMMENT
import ruleset.constant.ErrorBuilderText.OBJECT_DOC
import ruleset.constant.ErrorBuilderText.OBJECT_DOC_COMMENT
import ruleset.constant.ErrorBuilderText.OBJECT_PROP
import ruleset.constant.ErrorBuilderText.CLASS_CONSTRUCTOR_ARGS
import ruleset.constant.ErrorBuilderText.CLASS_CONSTRUCTOR_ARGS_DESC
import ruleset.constant.ErrorBuilderText.CLASS_FUN
import ruleset.constant.ErrorBuilderText.CLASS_FUN_COMMENT
import ruleset.constant.ErrorBuilderText.CLASS_FUN_ARGS
import ruleset.constant.ErrorBuilderText.CLASS_FUN_ARGS_DESC
import ruleset.constant.ErrorBuilderText.CLASS_FUN_ARGS_EMPTY
import ruleset.constant.ErrorBuilderText.CLASS_CONSTRUCTOR
import ruleset.constant.ErrorBuilderText.CLASS_CONSTRUCTOR_ARGS_EMPTY
import ruleset.constant.ErrorBuilderText.OBJECT_PROP_COMMENT
import ruleset.interfaces.ValidatedRule
import ruleset.utils.getRootNode
import ruleset.utils.getDocTagNames
import ruleset.utils.needObjectCheck
import ruleset.utils.needPropertyCheck
import ruleset.utils.needClassConstructorCheck
import ruleset.utils.needFunCheck
import ruleset.utils.getParamNodes
import ruleset.utils.getNameByIdentifier
import ruleset.utils.hasAnyAnnotation
import ruleset.utils.findAllChildNodes

/**
 * Правило проверяющее наличие java-doc у классов и объектов, их полей, конструкторов и публичных методов с аргументами.
 *
 */
class JavaDocsRule(private val javaDoc: Boolean) : ValidatedRule("java-docs") {
    private lateinit var emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    private var skipChecks: Boolean = false

    data class CheckArgumentsResult(
        val notFound: MutableList<ASTNode> = mutableListOf(),
        val withoutDescription: MutableList<ASTNode> = mutableListOf()
    )

    private fun emitError(node: ASTNode, errorText: String) {
        emit(node.startOffset, errorText, false)
    }

    override fun validate() = javaDoc

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        this.emit = emit

        if (hasSkipChecks(node)) return

        if (node.elementType === CLASS) {
            checkDocumentation(node, ErrorTextCreator(CLASS_DOC), ErrorTextCreator(CLASS_DOC_COMMENT))
        }

        if (needObjectCheck(node)) checkDocumentation(node, ErrorTextCreator(OBJECT_DOC), ErrorTextCreator(OBJECT_DOC_COMMENT))

        if (needPropertyCheck(node)) {
            if (node.treeParent.treeParent.elementType === OBJECT_DECLARATION) {
                checkDocumentation(node, ErrorTextCreator(OBJECT_PROP), ErrorTextCreator(OBJECT_PROP_COMMENT))
            } else {
                checkDocumentation(node, ErrorTextCreator(CLASS_PROP), ErrorTextCreator(CLASS_PROP_COMMENT))
            }
        }

        if (needClassConstructorCheck(node)) checkClassConstructor(node)

        if (needFunCheck(node)) checkFunction(node)
    }

    private fun hasSkipChecks(node: ASTNode): Boolean {
        if (node.elementType === CLASS) {
            skipChecks = findAllChildNodes(node, FUN).any { hasAnyAnnotation(node, "Test") }
        }

        if (node.elementType === FILE) {
            skipChecks = false
        }

        return skipChecks
    }

    private fun hasBlockComment(node: ASTNode): Boolean = getRootNode(node) != null

    private fun hasBlockCommentDescription(node: ASTNode): Boolean {
        val comment = getRootNode(node)
            ?.findChildByType(KDOC_SECTION)
            ?.firstChildNode
            ?.treeNext
            ?: return false

        return comment.elementType == TEXT
    }

    private fun checkDocumentation(node: ASTNode, blockCommentError: ErrorTextCreator, descriptionError: ErrorTextCreator) {
        if (!hasBlockComment(node)) {
            emitError(node, blockCommentError.createTextByNode(node))

            return
        }

        if (hasBlockCommentDescription(node)) return

        emitError(node, descriptionError.createTextByNode(node))
    }

    private fun checkDocumentationParams(node: ASTNode): CheckArgumentsResult {
        val docTags = getDocTagNames(node)
        val result = CheckArgumentsResult()

        getParamNodes(node).forEachIndexed { index: Int, paramNode: ASTNode ->
            val propName = getNameByIdentifier(paramNode)
            val hasResultNotFound = docTags.size <= index || propName != docTags[index].treeNext?.treeNext?.text

            if (hasResultNotFound) {
                result.notFound.add(paramNode)
            } else if (docTags[index].treeNext?.treeNext?.treeNext?.treeNext?.elementType != TEXT) {
                result.withoutDescription.add(paramNode)
            }
        }

        return result
    }

    private fun checkClassConstructor(node: ASTNode) {
        if (!hasBlockComment(node)) {
            emitError(node, ErrorTextCreator(CLASS_CONSTRUCTOR).createTextByNode(node))

            return
        }

        if (getParamNodes(node).isEmpty()) return

        if (getDocTagNames(node).isEmpty()) {
            emitError(node, ErrorTextCreator(CLASS_CONSTRUCTOR_ARGS_EMPTY).createTextByNode(node))

            return
        }

        val result = checkDocumentationParams(node)

        result.notFound.forEach { emitError(node, ErrorTextCreator(CLASS_CONSTRUCTOR_ARGS).createTextByNode(it)) }
        result.withoutDescription.forEach { emitError(node, ErrorTextCreator(CLASS_CONSTRUCTOR_ARGS_DESC).createTextByNode(it)) }
    }

    private fun checkFunction(node: ASTNode) {
        if (!hasBlockComment(node)) {
            emitError(node, ErrorTextCreator(CLASS_FUN).createTextByNode(node))

            return
        }

        if (!hasBlockCommentDescription(node)) {
            emitError(node, ErrorTextCreator(CLASS_FUN_COMMENT).createTextByNode(node))
        }

        checkFunctionParams(node)
    }

    private fun checkFunctionParams(node: ASTNode) {
        if (getParamNodes(node).isEmpty()) {
            return
        }

        if (getDocTagNames(node).isEmpty()) {
            emitError(node, ErrorTextCreator(CLASS_FUN_ARGS_EMPTY).createTextByNode(node))

            return
        }

        val result = checkDocumentationParams(node)

        result.notFound.forEach { emitError(node, ErrorTextCreator(CLASS_FUN_ARGS).createTextByNode(it)) }
        result.withoutDescription.forEach { emitError(node, ErrorTextCreator(CLASS_FUN_ARGS_DESC).createTextByNode(it)) }
    }
}
