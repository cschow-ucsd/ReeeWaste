package project.ucsd.reee_waste

import org.junit.Test
import kotlin.test.assertEquals

class AndroidPlatformTest {
    @Test
    fun platform() {
        assertEquals(getPlatform(), "Android")
                .let(::println)
    }
}