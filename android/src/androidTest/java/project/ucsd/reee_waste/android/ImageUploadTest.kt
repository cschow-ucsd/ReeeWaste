package project.ucsd.reee_waste.android

import android.graphics.BitmapFactory
import io.ktor.client.request.forms.InputProvider
import io.ktor.utils.io.streams.asInput
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test
import project.ucsd.reee_waste.backendless.service.RwService
import java.nio.ByteBuffer


class ImageUploadTest {
    @Test
    fun uploadTest(): Unit = runBlocking {
        val bitmap = BitmapFactory.decodeFile(
                "/Users/alanwarren/IdeaProjects/ReeeWaste/android/src/main/res/drawable")
        val width = bitmap.width
        val height = bitmap.height

        val size = bitmap.rowBytes * bitmap.height
        val byteBuffer: ByteBuffer = ByteBuffer.allocate(size)
        bitmap.copyPixelsToBuffer(byteBuffer)
        val byteArray = byteBuffer.array()
        val ioStream = byteArray.inputStream()
        val ioProvider: InputProvider = InputProvider(byteArray.size.toLong()) { ioStream.asInput() }
        val response = RwService(BuildConfig.BACKENDLESS_APP_ID, BuildConfig.BACKENDLESS_API_KEY)
                .postPictureAsync("baseballcap.jpg",ioProvider).await()
        assertNotNull(response)
    }
}