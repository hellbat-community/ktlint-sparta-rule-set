package ruleset.util

import junit.framework.Assert.assertEquals
import org.testng.annotations.Test
import ruleset.constant.TEST_PATH
import ruleset.utils.getFileConfigJSON

class FileConfigJSONTest {
    private var ruleSetConfig = getFileConfigJSON(TEST_PATH)

    @Test
    fun loadCurrentTestConfigTest() {
        assertEquals(ruleSetConfig.maxDepthNested, 2)
        assertEquals(ruleSetConfig.classNameContains.size, 2)
        assertEquals(ruleSetConfig.cyclomaticComplexity, 2)
        assertEquals(ruleSetConfig.kotlinDoc, true)
        assertEquals(ruleSetConfig.maxFunArguments, 5)
        assertEquals(ruleSetConfig.funNamePattern, "[a-z][a-zA-Z]*")
        assertEquals(ruleSetConfig.banLayers.size, 3)
        assertEquals(ruleSetConfig.maxLineLength, 120)
        assertEquals(ruleSetConfig.nodeBodyLength.size, 2)
        assertEquals(ruleSetConfig.packageNotUsedTwice.size, 1)
        assertEquals(ruleSetConfig.nodeNameLength.size, 2)
    }

    @Test
    fun loadEmptyFileTest() {
        ruleSetConfig = getFileConfigJSON("$TEST_PATH/file_config_json_test_data")

        assertEquals(ruleSetConfig.maxDepthNested, 0)
        assertEquals(ruleSetConfig.classNameContains.size, 0)
        assertEquals(ruleSetConfig.cyclomaticComplexity, 0)
        assertEquals(ruleSetConfig.kotlinDoc, false)
        assertEquals(ruleSetConfig.maxFunArguments, 0)
        assertEquals(ruleSetConfig.funNamePattern, "")
        assertEquals(ruleSetConfig.banLayers.size, 0)
        assertEquals(ruleSetConfig.maxLineLength, 0)
        assertEquals(ruleSetConfig.nodeBodyLength.size, 0)
        assertEquals(ruleSetConfig.packageNotUsedTwice.size, 0)
        assertEquals(ruleSetConfig.nodeNameLength.size, 0)
    }
}