import project.ucsd.reee_waste.platformMessage
import kotlin.test.Test
import kotlin.test.assertNotNull

class WebTest {
    @Test
    fun webTest() {
        assertNotNull(platformMessage())
    }
}