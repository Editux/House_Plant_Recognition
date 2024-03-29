package com.example.indoorplantcare.utils


import android.graphics.Bitmap
import java.lang.IllegalArgumentException

/**
 *
 * @param outputWidth - desired width of image
 * @param outputHeight - desired height of image
 *
 * @return cropper bitmap of desired size
 */
fun Bitmap.centerCrop(
    outputWidth: Int,
    outputHeight: Int
): Bitmap {
    val xStart = (width - outputWidth) / 2
    val yStart = (height - outputHeight) / 2

    if (xStart < 0 || yStart < 0 || outputWidth > width || outputHeight > height) {
        throw IllegalArgumentException("Invalid arguments for Center Cropping!")
    }
    return Bitmap.createBitmap(
        this,
        xStart,
        yStart,
        outputWidth,
        outputHeight
    )
}