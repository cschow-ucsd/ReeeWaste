package project.ucsd.reee_waste

import kotlin.test.Test
import kotlin.test.assertEquals

class JsPlatformTest {
    @Test
    fun platform() {
        assertEquals(getPlatform(), "JS")
                .let(::println)
    }
}