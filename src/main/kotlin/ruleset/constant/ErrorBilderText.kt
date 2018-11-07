package ruleset.constant

import ruleset.constant.ErrorBuilderNameType.NODE
import ruleset.constant.ErrorBuilderNameType.CLASS
import ruleset.constant.ErrorBuilderNameType.OBJECT
import ruleset.constant.ErrorBuilderNameType.FUN

object ErrorBuilderText {
    const val CLASS_DOC = "Not found JavaDoc for class $CLASS."
    const val CLASS_DOC_COMMENT = "Add description for class $CLASS in JavaDoc."
    const val CLASS_PROP = "Not found JavaDoc for property $NODE of class $CLASS."
    const val CLASS_PROP_COMMENT = "Add description for property $NODE of class $CLASS in JavaDoc."
    const val OBJECT_DOC = "Not found JavaDoc for object $OBJECT."
    const val OBJECT_DOC_COMMENT = "Add description for object $OBJECT in JavaDoc."
    const val OBJECT_PROP = "Not found JavaDoc for property $NODE of object $OBJECT."
    const val OBJECT_PROP_COMMENT = "Add description for property $NODE of object $OBJECT in JavaDoc."
    const val CLASS_CONSTRUCTOR = "Not found JavaDoc for constructor of class $CLASS."
    const val CLASS_CONSTRUCTOR_ARGS = "Not found JavaDoc for constructor property $NODE of class $CLASS."
    const val CLASS_CONSTRUCTOR_ARGS_EMPTY = "Not found JavaDoc for constructor properties of class $CLASS."
    const val CLASS_CONSTRUCTOR_ARGS_DESC = "Not found JavaDoc for constructor description property $NODE of " +
        "class $CLASS."
    const val CLASS_FUN = "Not found JavaDoc for method $NODE of class $CLASS."
    const val CLASS_FUN_COMMENT = "Add description for method $NODE of class $CLASS."
    const val CLASS_FUN_ARGS = "Not found JavaDoc for argument $NODE of method $FUN of class $CLASS."
    const val CLASS_FUN_ARGS_EMPTY = "Not found JavaDoc for method $NODE arguments of class $CLASS."
    const val CLASS_FUN_ARGS_DESC = "Not found JavaDoc for argument description $NODE of method $FUN of class $CLASS."
    const val FUN_MAX_ARGUMENTS = "You can't use more %1 arguments of function $FUN"
    const val CLASS_NAME = "Unexpected name, class $CLASS must %1 with %2"
    const val CYCLOMATIC_COMPLEXITY = "Cyclomatic complexity of function $FUN is %1. But allowed less or equals %2."
    const val UNEXPECTED_METHOD_NAME = "Unexpected method name. Rename method $FUN. Method names is't compare " +
        "with pattern %1"
    const val FUN_BLOCK_NESTING = "Maximum blocks nesting depth is %1. Check method $FUN."
    const val ELEMENT_BODY_LENGTH = "Element has a body size of %1 lines, expected no more than %2."
    const val ELEMENT_NAME_LENGTH = "Element has a name of %1 characters, expected no more than %2."
    const val MAX_LINE_LENGTH = "Line can't be more than %1 symbols"
}