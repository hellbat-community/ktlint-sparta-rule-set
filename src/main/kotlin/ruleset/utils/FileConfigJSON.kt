package ruleset.utils

import com.beust.klaxon.Klaxon
import ruleset.dto.Rules
import java.io.File

const val KTLINT_PROPS_FILE = "ktlint-sparta-rule-set.json"

/**
 * Функция позволяет читать параметры из файла конфига
 * @param path: String = "" - Поддержка путей для тестирования файла конфига
 *
 * @return : Rules
 */
fun getFileConfigJSON(path: String = ""): Rules {
    val propFile = if (path.isEmpty()) {
        File(KTLINT_PROPS_FILE)
    } else {
        File("$path/$KTLINT_PROPS_FILE")
    }

    if (!propFile.exists()) {
        return Rules()
    }

    return Klaxon().parse<Rules>(inputStream = propFile.inputStream())!!
}