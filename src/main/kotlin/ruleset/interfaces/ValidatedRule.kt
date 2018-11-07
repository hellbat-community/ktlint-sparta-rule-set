package ruleset.interfaces

import com.github.shyiko.ktlint.core.Rule

abstract class ValidatedRule(id: String) : Rule(id) {
    abstract fun validate(): Boolean
}