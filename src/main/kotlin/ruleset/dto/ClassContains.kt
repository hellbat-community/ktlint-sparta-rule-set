package ruleset.dto

import com.beust.klaxon.Json

data class ClassContains(
    @Json("param-name")
    var paramName: String = "",
    @Json("package-name")
    var packageName: String = "",
    @Json("name-part")
    var namePart: String = ""
)