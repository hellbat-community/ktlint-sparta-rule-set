package ruleset.constant

import ruleset.enums.Node.NODE
import ruleset.enums.Node.CLASS
import ruleset.enums.Node.FUN

object ErrorTextCreatorDictionary {
    val NOT_FOUND_DOC = "Not found JavaDoc of ${NODE.text}."
    val NOT_FOUND_DOC_COMMENT = "Not found JavaDoc description of ${NODE.text}."
    val NOT_FOUND_DOC_PROP = "Not found JavaDoc properties of ${NODE.text}."
    val NOT_FOUND_DOC_ARGUMENTS = "Not found JavaDoc arguments of ${NODE.text}."
    val FUN_MAX_ARGUMENTS = "You can't use more %1 arguments of function ${FUN.text}"
    val CLASS_NAME = "Unexpected name, class ${CLASS.text} must %1 with %2"
    val CYCLOMATIC_COMPLEXITY = "Cyclomatic complexity of function ${FUN.text} is %1. But allowed less or equals %2."
    val UNEXPECTED_METHOD_NAME = "Unexpected method name. Rename method ${FUN.text}. Method names is't compare " +
        "with pattern %1"
    val FUN_BLOCK_NESTING = "Maximum blocks nesting depth is %1. Check method ${FUN.text}."
    const val ELEMENT_BODY_LENGTH = "Element has a body size of %1 lines, expected no more than %2."
    const val ELEMENT_NAME_LENGTH = "Element has a name of %1 characters, expected no more than %2."
    const val MAX_LINE_LENGTH = "Line can't be more than %1 symbols"
}