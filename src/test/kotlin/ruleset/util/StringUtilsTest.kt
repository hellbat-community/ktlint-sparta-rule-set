package ruleset.util

import org.testng.Assert.assertEquals
import org.testng.annotations.Test
import ruleset.utils.upFirstChar

class StringUtilsTest {
    @Test
    fun upFirstCharTest() {
        assertEquals(upFirstChar("test"), "Test")
        assertEquals(upFirstChar("Test"), "Test")
        assertEquals(upFirstChar(""), "")
        assertEquals(upFirstChar("  "), "  ")
    }
}