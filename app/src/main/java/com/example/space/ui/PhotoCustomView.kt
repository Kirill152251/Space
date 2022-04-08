package com.example.space.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.appcompat.widget.AppCompatImageView

class PhotoCustomView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView (context, attrs, defStyleAttr), View.OnTouchListener {
    var currentScale = 1.0f
    val minScale = 1.0f
    val maxScale = 6.0f
    private var scaleDetector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    var photoMatrix = Matrix()
    private var matrixValue = FloatArray(10)
    private var zoomMode = 0

    var originalWidth = 0f
    var originalHeight = 0f
    var mViewedWidth = 0f
    var mViewedHeight = 0f
    private var lastPoint = PointF()
    private var startPoint = PointF()

    init {
        super.setClickable(true)
        imageMatrix = photoMatrix
        scaleType = ScaleType.MATRIX
        setOnTouchListener(this)
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scale = detector.scaleFactor
            currentScale *= scale
            if (currentScale > maxScale) {
                return true
            } else if (currentScale < minScale) {
                currentScale = minScale
            }
            if (originalWidth * currentScale <= mViewedWidth
                || originalHeight * currentScale <= mViewedHeight
            ) {
                photoMatrix.postScale(
                    scale, scale, mViewedWidth / 2f,
                    mViewedHeight / 2f
                )
            } else {
                photoMatrix.postScale(
                    scale, scale,
                    detector.focusX, detector.focusY
                )
            }
            fittedTranslation()
            return true
        }
    }

    private fun fittedTranslation() {
        photoMatrix.getValues(matrixValue)
        val translationX = matrixValue[Matrix.MTRANS_X]
        val translationY = matrixValue[Matrix.MTRANS_Y]
        val fittedTransX =
            getFittedTranslation(translationX, mViewedWidth, originalWidth * currentScale)
        val fittedTransY =
            getFittedTranslation(translationY, mViewedHeight, originalHeight * currentScale)
        if (fittedTransX != 0f || fittedTransY != 0f) photoMatrix.postTranslate(
            fittedTransX,
            fittedTransY
        )
    }

    private fun getFittedTranslation(translation: Float, vSize: Float, currentSize: Float): Float {
        val minTranslation: Float
        val maxTranslation: Float
        if (currentSize <= vSize) {
            minTranslation = 0f
            maxTranslation = vSize - currentSize
        } else {
            minTranslation = vSize - currentSize
            maxTranslation = 0f
        }
        if (translation < minTranslation) {
            return -translation + minTranslation
        }
        if (translation > maxTranslation) {
            return -translation + maxTranslation
        }
        return 0f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mViewedWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        mViewedHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        if (currentScale == 1f) {
            putToScreen()
        }
    }

    private fun putToScreen() {
        currentScale = 1f
        val mDrawable = drawable
        if (mDrawable == null || mDrawable.intrinsicWidth == 0 || mDrawable.intrinsicHeight == 0) return
        val imageWidth = mDrawable.intrinsicWidth
        val imageHeight = mDrawable.intrinsicHeight
        val factorX = mViewedWidth / imageWidth.toFloat()
        val factorY = mViewedHeight / imageHeight.toFloat()
        val factor = factorX.coerceAtMost(factorY)
        photoMatrix.setScale(factor, factor)

        var repeatedYSpace = (mViewedHeight
                - factor * imageHeight.toFloat())
        var repeatedXSpace = (mViewedWidth
                - factor * imageWidth.toFloat())
        repeatedYSpace /= 2.toFloat()
        repeatedXSpace /= 2.toFloat()
        photoMatrix.postTranslate(repeatedXSpace, repeatedYSpace)
        originalWidth = mViewedWidth - 2 * repeatedXSpace
        originalHeight = mViewedHeight - 2 * repeatedYSpace
        imageMatrix = photoMatrix
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        event ?: return false
        scaleDetector.onTouchEvent(event)
        val currentPoint = PointF(event.x, event.y)

        val mDisplay = this.display
        val mLayoutParams = this.layoutParams
        mLayoutParams.width = mDisplay.width
        mLayoutParams.height = mDisplay.height
        this.layoutParams = mLayoutParams
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastPoint.set(currentPoint)
                startPoint.set(lastPoint)
                zoomMode = 1
            }
            MotionEvent.ACTION_MOVE -> if (zoomMode == 1) {
                val changeInX = currentPoint.x - lastPoint.x
                val changeInY = currentPoint.y - lastPoint.y
                val fixedTranslationX =
                    getFixDragTrans(changeInX, mViewedWidth, originalWidth * currentScale)
                val fixedTranslationY =
                    getFixDragTrans(changeInY, mViewedHeight, originalHeight * currentScale)
                photoMatrix.postTranslate(fixedTranslationX, fixedTranslationY)
                fittedTranslation()
                lastPoint[currentPoint.x] = currentPoint.y
            }
        }
        imageMatrix = photoMatrix
        return false
    }

    private fun getFixDragTrans(delta: Float, viewedSize: Float, detailSize: Float): Float {
        return if (detailSize <= viewedSize) {
            0f
        } else delta
    }
}