package com.hiennv.flutter_callkit_incoming.widgets

import android.graphics.*
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.min

class CircleTransform : Transformation {

    override fun key(): String {
        return "circle"
    }

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        // Calculate the size of the square bitmap (smallest dimension of input)
        val sizeImage = min(input.width, input.height)
        val x = (input.width - sizeImage) / 2
        val y = (input.height - sizeImage) / 2

        // Create a square bitmap from the input, centered
        val squaredBitmap = Bitmap.createBitmap(input, x, y, sizeImage, sizeImage)
        if (squaredBitmap != input) {
            input.recycle() // Recycle input bitmap if a new one was created
        }

        // Handle nullable Bitmap.Config with a default value
        val config = input.config ?: Bitmap.Config.ARGB_8888
        // Create output bitmap with non-null config
        val bitmap = Bitmap.createBitmap(sizeImage, sizeImage, config)

        // Set up canvas and paint for circular transformation
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(
            squaredBitmap,
            Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        paint.shader = shader
        paint.isAntiAlias = true

        // Draw a circle
        val radius = sizeImage / 2f
        canvas.drawCircle(radius, radius, radius, paint)

        // Recycle the squared bitmap
        squaredBitmap.recycle()

        return bitmap
    }
}