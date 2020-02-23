package project.ucsd.reee_waste

import kotlin.test.Test
import kotlin.test.assertNotNull

class CommonPlatformTest {
    @Test
    fun platform() {
        assertNotNull(getPlatform())
                .let(::println)
    }
}