package ruleset

import org.testng.Assert.assertTrue
import org.testng.annotations.Test

class BootstrapRuleSetProviderTest {
    @Test
    fun sizeTest() {
        val provider = BootstrapRuleSetProvider().get()

        assertTrue(provider.rules.size == 9)
    }
}