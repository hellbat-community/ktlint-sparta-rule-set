package ruleset.utils

/**
 * Сделать первый символ заглавным
 *
 * @param text: String
 *
 * @return : String
 */
fun upFirstChar(text: String): String {
    return if (text.isBlank())
        text
    else
        text.substring(0, 1).toUpperCase() + text.substring(1)
}

/**
 * Проверяет содержание на наличие новой строки
 *
 * @param str: String
 *
 * @return : Boolean
 */
fun containsNewLine(str: String): Boolean = str.contains("\r\n") || str.contains("\n")