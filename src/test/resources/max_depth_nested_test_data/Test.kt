package a.b

class Test {
    fun good(num: Int, name: String) {
        if (num == 1) {
            if (name == "1") {
                println("1")
            }

            if (name == "2") {
                println("2")
            }
        } else {
            name.forEach { println(it) }
        }
    }

    fun goodEach(list: List<List<String>>) {
        for (items in list) {
            items.forEach { println(it) }
        }
    }

    fun badIfs(num: Int, name: String) {
        var index = 10 + num

        if (num > 1) {
            while (index > 10) {
                if (num > 20) {
                    index = 20
                }

                index -= 1
            }
        }
    }

    fun badFor(list: List<String>) {
        for (item in list) {
            item.forEach {
                run {
                    if (it == '1') {
                        println("1")
                    }
                }
            }
        }
    }

    fun badWhen(list: List<Int>) {
        list.forEach {
            run {
                if (it < 5) {
                    when (it) {
                        2 -> println("2")
                        3 -> println("3")
                        4 -> println("4")
                    }
                }
            }
        }
    }

    fun badEach(list: List<List<String>>) {
        for (items in list) {
            items.forEach { value -> value.forEach { println(it) } }
        }
    }
}
