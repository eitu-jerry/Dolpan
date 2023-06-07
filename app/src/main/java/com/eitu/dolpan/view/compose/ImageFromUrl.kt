package com.eitu.dolpan.view.compose

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ImageFromUrl {

    companion object {
        fun loadImageBitmapFromUrl(url: String, onBitmapLoaded: (Bitmap?) -> Unit) {
            val handler = Handler(Looper.getMainLooper())

            Thread {
                var bitmap: Bitmap? = null

                try {
                    val imageUrl = URL(url)
                    val connection: HttpURLConnection = imageUrl.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val inputStream: InputStream = connection.inputStream
                    bitmap = BitmapFactory.decodeStream(inputStream)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                handler.post { onBitmapLoaded(bitmap) }
            }.start()
        }


        @SuppressLint("UnrememberedMutableState")
        @Composable
        fun ImageFromUrl(url: String, imageSize: Dp) {
            val bitmapState: MutableState<Bitmap?> = mutableStateOf(null)

            loadImageBitmapFromUrl(url) { bitmap ->
                Log.d("bitmap", "$bitmap")
                bitmapState.value = bitmap
            }

            bitmapState.value?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(imageSize)
                )
            }
        }
    }



}