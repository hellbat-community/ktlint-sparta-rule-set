package a.b

import com.github.shyiko.ktlint.core.RuleSet

class Complex {
    fun complexFunc(num: Int, name: String) {
        if (num == 1) {
            if (name == "ggg" || name == "bbb") {
                println("tsssss...")
            }
            if (name == "ggg" && name == "bbb") {
                println("tsssss...")
            }
        }
    }

    fun funcA(num: Int, name: String) {
        if (num > 1) {
            if (name == "ggg" || name == "bbb") {
                when (num) {
                    2 -> println("2")
                    3 -> println("3")
                    4 -> println("4")
                    5 -> println("5")
                    6 -> println("6")
                    7 -> println("7")
                    8 -> println("8")
                }
            }
        }
    }

    fun funcB(num: Int, name: String) {
        if (num < 0) return
        if (num == 1) {
            if (name == "ggg" || name == "bbb") {
                for (s in ArrayList<String>()) {
                    println(s)
                }
            }
        }
    }
}