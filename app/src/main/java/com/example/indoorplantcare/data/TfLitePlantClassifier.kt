package com.example.indoorplantcare.data

import android.content.Context
import android.graphics.Bitmap
import android.view.Surface
import com.example.indoorplantcare.domain.model.Classification
import com.example.indoorplantcare.domain.repository.PlantClassifier
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class TfLitePlantClassifier (
    private val context: Context,
    private val confidenceThreshold: Float = 0.75f,
    private val maxResults: Int = 1
): PlantClassifier {
    private var classifier: ImageClassifier? = null

    override fun classifyCurrentFrame(bitmap: Bitmap, rotation: Int): List<Classification> {
        val labels = loadLabelsFromAsset("labels.txt")
        if (classifier == null) {
            initializeClassifier()
        }

        val tfLiteImageProcessingOptions = initializeImageProcessingOptions(rotation)
        val tfLiteImageProcessor: ImageProcessor = ImageProcessor.Builder().build()
        val tensorImage: TensorImage = tfLiteImageProcessor.process(
            TensorImage.fromBitmap(bitmap)
        )

        val classificationResults = classifier?.classify(
            tensorImage,
            tfLiteImageProcessingOptions
        )


        return classificationResults?.flatMap { curClassification ->
            curClassification.categories.map { curCategory ->
                Classification(
                    detectedObjectName = labels.getOrDefault(curCategory.label.toInt(), "Unknown"),
                    confidenceScore = curCategory.score
                )
            }
        }?.distinctBy { it.detectedObjectName } ?: emptyList()
    }

    /**
     * Initializes the TfLite Image Classifier
     */
    private fun initializeClassifier() {

        val baseOptions = BaseOptions.builder()
            .setNumThreads(2)
            .build()

        val options = ImageClassifier.ImageClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .setMaxResults(maxResults)
            .setScoreThreshold(confidenceThreshold)
            .build()

        try {
            // Create the classifier from the TfLite File
            classifier = ImageClassifier.createFromFileAndOptions(
                context,
                "model.tflite",
                options
            )
        } catch (exception: IllegalStateException) {
            exception.printStackTrace()
        }
    }

    private fun loadLabelsFromAsset(fileName: String): Map<Int, String> {
        val labels = mutableMapOf<Int, String>()
        try {
            val inputStream: InputStream = context.assets.open(fileName)
            val reader = BufferedReader(InputStreamReader(inputStream))

            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val firstSpaceIndex = line?.indexOf(' ')
                if (firstSpaceIndex != null && firstSpaceIndex != -1) {
                    val index = line?.substring(0, firstSpaceIndex)?.toIntOrNull()
                    val label = line?.substring(firstSpaceIndex + 1)?.trim()
                    if (index != null && label != null) {
                        labels[index] = label
                    }
                }
            }

            reader.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return labels
    }
    /**
     * Builds and returns the TfLite ImageProcessingOptions
     *
     * @param rotation
     * @return ImageProcessingOptions
     */
    private fun initializeImageProcessingOptions(rotation: Int): ImageProcessingOptions {
        return ImageProcessingOptions.builder()
            .setOrientation(getOrientationFromRotation(rotation))
            .build()
    }

    /**
     * Returns the orientation of image from the rotation using the
     * TfLite ImageProcessingOptions.Orientation used to internally make use of image axes as an Image metadata
     *
     * @param rotation
     * @return ImageProcessingOptions.Orientation
     */
    private fun getOrientationFromRotation(rotation: Int): ImageProcessingOptions.Orientation {
        return when(rotation) {
            Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
            Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
            Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
            else -> ImageProcessingOptions.Orientation.RIGHT_TOP
        }
    }
}