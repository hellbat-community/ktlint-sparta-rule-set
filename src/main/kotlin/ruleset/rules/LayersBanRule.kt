package ruleset.rules

import org.jetbrains.kotlin.KtNodeTypes.IMPORT_LIST
import org.jetbrains.kotlin.KtNodeTypes.PACKAGE_DIRECTIVE
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import ruleset.dto.BanLayer
import ruleset.interfaces.ValidatedRule

/**
 * Проверяет на использование запрещенных слоев
 * Правило состоит из алгоритма:
 * - Определяем текущий слой
 * - Определяем запрещенные слои для него
 * - Проверяем импорты
 *
 */
class LayersBanRule(private val banLayers: MutableList<BanLayer>) : ValidatedRule("ban-layers") {
    companion object {
        const val ERROR_TEXT = "Unexpected package Layer"
    }

    private lateinit var currentLayerRule: BanLayer
    private var hasRuleForLayer: Boolean = false
    private lateinit var emit: ((offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit)

    override fun validate() = banLayers.isNotEmpty()

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        this.emit = emit
        val chars = node.chars

        if (node.elementType === PACKAGE_DIRECTIVE) {
            setCurrentLayerRule(chars)
        }

        if (!hasRuleForLayer) return

        validateRuleParams(node)

        if (node.elementType === IMPORT_LIST && chars.isNotEmpty()) {
            val listOfImport = chars.split("import")

            checkImportListUnexpectedPackage(node, listOfImport)
        }
    }

    private fun setCurrentLayerRule(chars: CharSequence) {
        hasRuleForLayer = banLayers.any { chars.contains("${it.root}.${it.goal}") }

        if (!hasRuleForLayer) {
            return
        }

        currentLayerRule = banLayers.first { chars.contains("${it.root}.${it.goal}") }
    }

    private fun validateRuleParams(node: ASTNode) {
        val isNotValidConfig = currentLayerRule.unexpectedLayers.isEmpty() ||
            currentLayerRule.goal.isEmpty() ||
            currentLayerRule.root.isEmpty()

        if (isNotValidConfig) {
            emit(node.startOffset, "Config is'n valid", false)
        }
    }

    private fun checkImportListUnexpectedPackage(node: ASTNode, listOfImport: List<String>) = listOfImport
        .filter { it.contains(currentLayerRule.root) }
        .filter { importItem ->
            currentLayerRule.unexpectedLayers.any {
                importItem.contains("${currentLayerRule.root}.$it")
            }
        }
        .forEach {
            emit(node.startOffset, ERROR_TEXT, false)
        }
}