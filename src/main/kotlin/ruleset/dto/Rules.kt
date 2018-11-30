package ruleset.dto

import com.beust.klaxon.Json

data class Rules(
    @Json("kotlin-doc")
    var kotlinDoc: Boolean = false,
    @Json("max-depth-nested")
    var maxDepthNested: Int = 0,
    @Json("cyclomatic-complexity")
    var cyclomaticComplexity: Int = 0,
    @Json("package-not-use-twice")
    var packageNotUsedTwice: MutableList<String> = mutableListOf(),
    @Json("ban-layers")
    var banLayers: MutableList<BanLayer> = mutableListOf(),
    @Json("max-line-length")
    var maxLineLength: Int = 0,
    @Json("class-name-contains")
    var classNameContains: MutableList<ClassContains> = mutableListOf(),
    @Json("max-fun-arguments")
    var maxFunArguments: Int = 0,
    @Json("fun-name-pattern")
    var funNamePattern: String = "",
    @Json("node-body-max-length-rule")
    var nodeBodyLength: MutableList<Node> = mutableListOf(),
    @Json("node-name-length-rule")
    var nodeNameLength: MutableList<Node> = mutableListOf()
)