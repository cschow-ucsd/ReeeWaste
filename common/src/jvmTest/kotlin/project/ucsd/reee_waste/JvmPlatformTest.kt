package project.ucsd.reee_waste

import org.junit.Test
import kotlin.test.assertEquals

class JvmPlatformTest {
    @Test
    fun platform() {
        assertEquals(getPlatform(), "JVM")
                .let(::println)
    }
}