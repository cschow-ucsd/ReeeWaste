package project.ucsd.reee_waste

import kotlin.test.Test
import kotlin.test.assertEquals

class IosPlatformTest {
    @Test
    fun platform() {
        assertEquals(getPlatform(), "iOS")
                .let(::println)
    }
}