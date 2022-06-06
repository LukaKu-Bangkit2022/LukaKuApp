package com.bangkit.capstone.lukaku.utils

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.ViewPropertyAnimator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.bangkit.capstone.lukaku.R
import com.bangkit.capstone.lukaku.utils.Constants.ANIMATION_FAST_MILLIS
import com.bangkit.capstone.lukaku.utils.Constants.FILENAME_FORMAT
import com.bangkit.capstone.lukaku.utils.Constants.PHOTO_EXTENSION
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.cacheDir
    return File.createTempFile(timeStamp, PHOTO_EXTENSION, storageDir)
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

fun <T> ImageView.loadImage(image: T, corners: Int = 1) {
    Glide.with(this)
        .load(image)
        .transform(CenterCrop(), RoundedCorners(corners))
        .placeholder(R.drawable.ic_image_load)
        .error(R.drawable.ic_image_broken)
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

fun bitmapToFile(bitmap: Bitmap, context: Context): File {
    val imageFile = createTempFile(context)

    var compressQuality = 100
    var streamLength: Int

    do {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, compressQuality, outputStream)
        val bmpPicByteArray = outputStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)

    bitmap.compress(CompressFormat.JPEG, compressQuality, FileOutputStream(imageFile))
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

fun reduceFileImage(file: File): File {
    val bitmap = BitmapFactory.decodeFile(file.path)

    var compressQuality = 100
    var streamLength: Int

    do {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.JPEG, compressQuality, outputStream)
        val bmpPicByteArray = outputStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > 1000000)

    bitmap.compress(CompressFormat.JPEG, compressQuality, FileOutputStream(file))

    return file
}

fun LinearLayout.withAnimationY(value: Float = 0f): ViewPropertyAnimator {
    return animate().translationY(value)
}