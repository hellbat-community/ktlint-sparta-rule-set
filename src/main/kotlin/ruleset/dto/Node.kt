package ruleset.dto

import com.beust.klaxon.Json

data class Node(
    @Json("type")
    var type: String = "",
    @Json("max-length")
    var maxLength: Int = 0
)