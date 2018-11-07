package ruleset

import com.github.shyiko.ktlint.core.RuleSet
import com.github.shyiko.ktlint.core.RuleSetProvider
import ruleset.interfaces.ValidatedRule
import ruleset.rules.MaxDepthNestedBlocksRule
import ruleset.rules.CycloComplexityRule
import ruleset.rules.PackageNotUseTwiceRule
import ruleset.rules.LayersBanRule
import ruleset.rules.LineLengthRule
import ruleset.rules.JavaDocsRule
import ruleset.rules.NodeBodyLengthRule
import ruleset.rules.NodeNameLengthRule
import ruleset.rules.ClassNameContainsRule
import ruleset.rules.FunArgumentsRule
import ruleset.rules.FunNamePatternRule
import ruleset.utils.getFileConfigJSON

val ruleSetConfig = getFileConfigJSON()

class BootstrapRuleSetProvider : RuleSetProvider {
    private var ruleSet: MutableList<ValidatedRule> = mutableListOf(
        MaxDepthNestedBlocksRule(ruleSetConfig.maxDepthNested),
        CycloComplexityRule(ruleSetConfig.cyclomaticComplexity),
        PackageNotUseTwiceRule(ruleSetConfig.packageNotUsedTwice),
        LayersBanRule(ruleSetConfig.banLayers),
        LineLengthRule(ruleSetConfig.maxLineLength),
        JavaDocsRule(ruleSetConfig.javaDoc),
        NodeBodyLengthRule(ruleSetConfig.nodeBodyLength),
        NodeNameLengthRule(ruleSetConfig.nodeNameLength),
        FunArgumentsRule(ruleSetConfig.maxFunArguments),
        FunNamePatternRule(ruleSetConfig.funNamePattern),
        ClassNameContainsRule(ruleSetConfig.classNameContains)
    )

    init {
        ruleSet = getValidatedRuleSet()
    }

    private fun getValidatedRuleSet(): MutableList<ValidatedRule> = ruleSet.filter { it.validate() }.toMutableList()

    override fun get() = RuleSet("ktlint-sparta-rule-set", *ruleSet.toTypedArray())
}