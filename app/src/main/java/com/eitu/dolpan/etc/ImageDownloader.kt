package com.eitu.dolpan.etc
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class ImageDownloader {

    private val client: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    suspend fun downloadImage(url: String): Result<Bitmap> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()
            val inputStream = response.body?.byteStream()

            if (response.isSuccessful && inputStream != null) {
                val bitmap = BitmapFactory.decodeStream(inputStream)
                Result.Success(bitmap)
            } else {
                Result.Error(IOException("Error downloading image."))
            }
        } catch (e: Exception) {
            Result.Error(IOException("Error downloading image.", e))
        }
    }

    companion object {

        fun setImage(imgView: ImageView, url: String) {
            CoroutineScope(Dispatchers.IO).launch {
                val result = ImageDownloader().downloadImage(url)
                if (result is Result.Success) {
                    val bitmap = result.data
                    launch(Dispatchers.Main) {
                        imgView.setImageBitmap(bitmap)
                    }
                }
            }
        }

        fun setBanner(context: Context, imgView: ImageView, _url: String) {
            CoroutineScope(Dispatchers.Main).launch {
                val url = _url + "=w2560"
                var bitmap: Bitmap? = null
                val fileName = getFileNameFromUrl(url);
                val file = File(context.cacheDir, fileName)
                if (file.exists()) bitmap = BitmapFactory.decodeFile(file.absolutePath)
                else {
                    CoroutineScope(Dispatchers.IO).launch {
                        val result = ImageDownloader().downloadImage(url)
                        if (result is Result.Success) {
                            bitmap = createBanner(result.data)
                            bitmapToFile(context, bitmap!!, fileName)
                        }
                    }.join()
                }
                imgView.setImageBitmap(bitmap)
            }
        }

        private fun createBanner(bitmap: Bitmap): Bitmap {
            val height = (bitmap.height / 3.4).toInt()
            val width = (height * 3.7).toInt()
            val startY = (bitmap.height - height) / 2
            val startX = (bitmap.width - width) / 2


            Log.d("image", "checksize\noriginal -> ${bitmap.width} : ${bitmap.height}\nnew -> $width : $height")
            return Bitmap.createBitmap(bitmap, startX, startY, width, height)
        }

        private fun getFileNameFromUrl(url: String): String {
            return try {
                url.hashCode().toString() + ".jpg"
            } catch (e: Exception) {
                e.printStackTrace()
                generateRandomId() + ".jpg"
            }
        }

        private fun generateRandomId(): String {
            val s: String
            s = if (Random().nextBoolean()) {
                Random().nextInt(10).toString()
            } else {
                (Random().nextInt(26) + 97).toChar().toString()
            }
            return s
        }

        private fun bitmapToFile(context: Context, bitmap: Bitmap, strFilePath: String) {
            val file = File(context.cacheDir, strFilePath)
            Log.d("file", file.path)

            if (!file.exists()) file.createNewFile()

            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            outputStream.flush()
            outputStream.close()
        }
    }
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
