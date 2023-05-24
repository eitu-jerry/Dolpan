package com.eitu.dolpan.etc

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import com.eitu.dolpan.R
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

object ImageDownloader {
    private const val TAG = "ImageDownloader"
    private const val CACHE_DIR_NAME = "image_cache"
    private const val IO_BUFFER_SIZE = 8 * 1024

    interface ImageDownloadListener {
        fun onImageDownloaded(imageFile: File)
        fun onImageDownloadError(errorMsg: String)
    }

    @SuppressLint("StaticFieldLeak")
    fun downloadImage(context: Context, imageUrl: String, listener: ImageDownloadListener) {
        object : AsyncTask<String, Void, File>() {
            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg params: String): File {
                val imageUrl = params[0]
                val cacheDir = getCacheDir(context)
                val imageFile = File(cacheDir, getFileNameFromUrl(imageUrl))

                if (imageFile.exists()) {
                    Log.d(TAG, "Image cache hit: ${imageFile.absolutePath}")
                    return imageFile
                }

                try {
                    val url = URL(imageUrl)
                    val connection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val input = connection.inputStream
                    val bitmap = BitmapFactory.decodeStream(input)

                    val outputStream = FileOutputStream(imageFile)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.flush()
                    outputStream.close()

                    Log.d(TAG, "Image downloaded and cached: ${imageFile.absolutePath}")
                    return imageFile
                } catch (e: Exception) {
                    Log.e(TAG, "Error downloading image from $imageUrl", e)
                    return imageFile
                }
            }

            override fun onPostExecute(imageFile: File) {
                if (imageFile.exists()) {
                    listener.onImageDownloaded(imageFile)
                } else {
                    listener.onImageDownloadError("Error downloading image")
                }
            }
        }.execute(imageUrl)
    }

    private fun getCacheDir(context: Context): File {
        val cacheDir = context.applicationContext.cacheDir
        val imageCacheDir = File(cacheDir, CACHE_DIR_NAME)
        if (!imageCacheDir.exists()) {
            imageCacheDir.mkdirs()
        }
        return imageCacheDir
    }

    private fun getFileNameFromUrl(url: String): String {
        return try {
            val lastDotIndex = url.lastIndexOf(".")
            url.hashCode().toString() + url.substring(lastDotIndex)
        } catch (e: Exception) {
            e.printStackTrace()
            generateRandomId() + ".png"
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

    fun setProfile(context: Context, profile: String, imgProfile: ImageView, sampleSize: Int) {
        val split = profile.split("/")
        val fileName = split[split.size - 1]
        if (profile.isEmpty() || fileName == "null") {
            try {
                //imgProfile.setImageResource(R.drawable.img_no_profile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            downloadImage(context, profile, object : ImageDownloadListener {
                override fun onImageDownloaded(imageFile: File) {
                    val options = BitmapFactory.Options()
                    options.inSampleSize = sampleSize
                    val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath, options)
                    if (bitmap != null) {
                        try {
                            imgProfile.setImageBitmap(bitmap)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onImageDownloadError(errorMsg: String) {
                    try {
                        //imgProfile.setImageResource(R.drawable.img_no_profile)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }
}
