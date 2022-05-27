package com.bangkit.capstone.lukaku.utils

import android.Manifest
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.utils.Constants.ANIMATION_FAST_MILLIS
import com.bangkit.capstone.lukaku.utils.Constants.FILENAME_FORMAT
import com.bangkit.capstone.lukaku.utils.Constants.PHOTO_EXTENSION
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, PHOTO_EXTENSION, storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp$PHOTO_EXTENSION")
}

fun ImageView.loadCircleImage(imageSource: Uri?) {
    Glide.with(this)
        .load(imageSource)
        .centerCrop()
        .circleCrop()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.ic_image_load)
        .error(R.drawable.ic_image_broken)
        .into(this)
}

fun <T> ImageView.loadImage(image: T) {
    Glide.with(this)
        .load(image)
        .centerCrop()
        .placeholder(R.drawable.ic_image_load)
        .error(R.drawable.ic_image_broken)
        .into(this)
}

fun ImageView.loadCircleImage(context: Context, imageSource: String?) {
    Glide.with(context)
        .load(imageSource)
        .into(this)
}

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun uriToFile(uri: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val imageFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(uri) as InputStream
    val outputStream: OutputStream = FileOutputStream(imageFile)
    val buf = ByteArray(1024)
    var len: Int

    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return imageFile
}

fun ImageButton.simulateClick(delay: Long = ANIMATION_FAST_MILLIS) {
    performClick()
    isPressed = true
    invalidate()
    postDelayed({
        invalidate()
        isPressed = false
    }, delay)
}