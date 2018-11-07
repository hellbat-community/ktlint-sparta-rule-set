package ruleset.dto

import com.beust.klaxon.Json

data class BanLayer(
    @Json("goal")
    var goal: String = "",
    @Json("root")
    var root: String = "",
    @Json("unexpected-layers")
    var unexpectedLayers: MutableList<String> = mutableListOf()
)