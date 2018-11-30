package ruleset.enums

import org.jetbrains.kotlin.KtNodeTypes
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import ruleset.utils.getNameByIdentifier
import ruleset.utils.getNodeNameByIdentifier

enum class Node(val text: String) {
    CLASS("%class%") {
        override fun getNodeName(node: ASTNode): String {
            return node.getNodeNameByIdentifier(KtNodeTypes.CLASS)
        }
    },
    OBJECT("%object%") {
        override fun getNodeName(node: ASTNode): String {
            return node.getNodeNameByIdentifier(KtNodeTypes.OBJECT_DECLARATION)
        }
    },
    FUN("%fun%") {
        override fun getNodeName(node: ASTNode): String {
            return node.getNodeNameByIdentifier(KtNodeTypes.FUN)
        }
    },
    NODE("%node%");

    open fun getNodeName(node: ASTNode): String {
        return node.getNameByIdentifier()
    }
}