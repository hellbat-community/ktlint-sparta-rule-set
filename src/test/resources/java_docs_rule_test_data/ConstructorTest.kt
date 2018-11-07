package java_docs_rule_test_data

/**
 * GoodConstructorTest
 *
 * @property arg description
 */
class GoodConstructorTest(arg: String) {
    /**
     * text
     */
    var text: String = arg
}

/**
 * GoodEmptyConstructorTest
 */
class GoodEmptyConstructorTest()

/**
 * GoodAConstructorTest
 *
 * @property arg description
 */
class GoodAConstructorTest(private val arg: String)

/**
 * GoodBConstructorTest
 */
class GoodBConstructorTest {
    /**
     * value
     */
    private lateinit var value: String

    /**
     * constructor
     *
     * @param arg description
     */
    constructor(arg: String) {
        value = arg
    }
}

/**
 * BadConstructorTest
 */
class BadConstructorTest(arg: String) {
    /**
     * text
     */
    var text: String = arg
}

/**
 * BadConstructorATest
 */
class BadConstructorATest(private val arg: String)

/**
 * BadConstructorBTest
 *
 * @property
 */
class BadConstructorBTest(private val arg: String)

/**
 * BadConstructorCTest
 *
 * @property arg
 */
class BadConstructorCTest(private val arg: String)

/**
 * BadConstructorDTest
 */
class BadConstructorDTest {
    /**
     * value
     */
    private lateinit var value: String

    constructor(arg: String) {
        value = arg
    }
}

/**
 * BadConstructorETest
 */
class BadConstructorETest {
    /**
     * value
     */
    private lateinit var value: String

    // constructor
    constructor(arg: String) {
        value = arg
    }
}

/**
 * BadConstructorFTest
 */
class BadConstructorFTest {
    /**
     * value
     */
    private lateinit var value: String

    /**
     *
     */
    constructor(arg: String) {
        value = arg
    }
}
