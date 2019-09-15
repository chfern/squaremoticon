package com.fernandochristyanto.squaremoticon

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import kotlin.math.min

class CheerfulSquareEmoticon(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    companion object {
        // Dimens
        private var actualLength = 0

        private val STROKE_WIDTH = 0
        private val GUTTER_LENGTH = 15
        private val BORDER_RADIUS = 15f
        private val DEFAULT_WIDTH = 50
        private val DEFAULT_HEIGHT = 50


        // Colors
        private val DEFAULT_EMOT_COLOR = R.color.defaultEmoticonColor
        private val EMOT_STROKE_COLOR = R.color.defaultStroke
        private val EMOT_EYES_COLOR = R.color.defaultDark
        private val EMOT_MOUTH_COLOR = R.color.defaultDark
        private val EMOT_GUTTER_COLOR = R.color.defaultWhite

        // States
        const val EMOT_STATE_HAPPY = 1
        const val EMOT_STATE_SAD = -1


        // Mouth Y Positions
        private val HAPPY_MOUTH_Y_TOP_MULTIPLIER = 0.6f
        private val HAPPY_MOUTH_Y_BOTTOM_MULTIPLIER = 1f
        private val SAD_MOUTH_Y_TOP_MULTIPLIER = 0.50f
        private val SAD_MOUTH_Y_BOTTOM_MULTIPLIER = 0.60f

        private val HAPPY_MOUTH_Y_CORNER_MULTIPLIER = 0.6f
        private val SAD_MOUTH_Y_CORNER_MULTIPLIER = 0.7f
    }

    private var currState = EMOT_STATE_HAPPY
    private var currMouthYTopMultiplier = 0f
    private var currMouthYBottomMultiplier = 0f
    private var currMouthYCornerMultiplier = 0f

    // Colors
    private var emotBackgroundColor = DEFAULT_EMOT_COLOR
        set(@ColorRes color) {
            field = color
            invalidate()
        }

    // Paints
    private val emotContainerPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, EMOT_STROKE_COLOR)
        style = Paint.Style.FILL
    }
    private val emotGutterPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, EMOT_GUTTER_COLOR)
        style = Paint.Style.FILL
    }
    private val emotBackgroundPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, emotBackgroundColor)
        style = Paint.Style.FILL
    }
    private val emotEyesMouthPaint = Paint(ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, EMOT_STROKE_COLOR)
        style = Paint.Style.FILL
    }

    // Shapes
    private val emotContainerRect = Rect()
    private val emotGutterRect = Rect()
    private val emotFaceRect = Rect()

    // Face Paths
    private val mouthPath = Path()

    fun setEmoticonState(state: Int) {
        if (state != EMOT_STATE_HAPPY && state != EMOT_STATE_SAD)
            return

        currState = state
        invalidate()
    }

    fun animateSad(durationMillis: Long) {
        val propertyYTop = PropertyValuesHolder.ofFloat(
            "top",
            HAPPY_MOUTH_Y_TOP_MULTIPLIER,
            SAD_MOUTH_Y_TOP_MULTIPLIER
        )
        val propertyYBottom = PropertyValuesHolder.ofFloat(
            "bottom",
            HAPPY_MOUTH_Y_BOTTOM_MULTIPLIER,
            SAD_MOUTH_Y_BOTTOM_MULTIPLIER
        )
        val propertyYCorner = PropertyValuesHolder.ofFloat(
            "corner",
            HAPPY_MOUTH_Y_CORNER_MULTIPLIER,
            SAD_MOUTH_Y_CORNER_MULTIPLIER
        )
        val animator = ValueAnimator()
        animator.setValues(propertyYTop, propertyYBottom, propertyYCorner)
        animator.duration = durationMillis
        animator.addUpdateListener {
            currMouthYTopMultiplier = it.getAnimatedValue("top") as Float
            currMouthYBottomMultiplier = it.getAnimatedValue("bottom") as Float
            currMouthYCornerMultiplier = it.getAnimatedValue("corner") as Float
            invalidate()
        }
        animator.start()
    }

    fun animateHappy(durationMillis: Long) {
        val propertyYTop = PropertyValuesHolder.ofFloat(
            "top",
            SAD_MOUTH_Y_TOP_MULTIPLIER,
            HAPPY_MOUTH_Y_TOP_MULTIPLIER
        )
        val propertyYBottom = PropertyValuesHolder.ofFloat(
            "bottom",
            SAD_MOUTH_Y_BOTTOM_MULTIPLIER,
            HAPPY_MOUTH_Y_BOTTOM_MULTIPLIER
        )
        val propertyYCorner = PropertyValuesHolder.ofFloat(
            "corner",
            SAD_MOUTH_Y_CORNER_MULTIPLIER,
            HAPPY_MOUTH_Y_CORNER_MULTIPLIER
        )
        val animator = ValueAnimator()
        animator.setValues(propertyYTop, propertyYBottom, propertyYCorner)
        animator.duration = durationMillis
        animator.addUpdateListener {
            currMouthYTopMultiplier = it.getAnimatedValue("top") as Float
            currMouthYBottomMultiplier = it.getAnimatedValue("bottom") as Float
            currMouthYCornerMultiplier = it.getAnimatedValue("corner") as Float
            invalidate()
        }
        animator.start()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var actualWidth: Int = DEFAULT_WIDTH
        var actualHeight: Int = DEFAULT_HEIGHT

        when (widthMode) {
            MeasureSpec.EXACTLY -> actualWidth = widthSize
            MeasureSpec.AT_MOST -> actualWidth = min(DEFAULT_WIDTH, widthSize)
            MeasureSpec.UNSPECIFIED -> actualWidth = DEFAULT_WIDTH
        }
        when (heightMode) {
            MeasureSpec.EXACTLY -> actualHeight = heightSize
            MeasureSpec.AT_MOST -> actualHeight = min(DEFAULT_HEIGHT, heightSize)
            MeasureSpec.UNSPECIFIED -> actualHeight = DEFAULT_HEIGHT
        }

        val actualLength = min(actualWidth, actualHeight)
        setMeasuredDimension(actualLength, actualLength)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val contentWidth = w - paddingLeft - paddingRight
        val contentHeight = h - paddingTop - paddingBottom
        actualLength = min(contentWidth, contentHeight)
        emotContainerRect.set(0, 0, actualLength, actualLength)
        emotGutterRect.set(
            STROKE_WIDTH.toInt(),
            STROKE_WIDTH.toInt(),
            (actualLength - STROKE_WIDTH).toInt(),
            (actualLength - STROKE_WIDTH).toInt()
        )
        emotFaceRect.set(
            (STROKE_WIDTH + (0.015 * actualLength)).toInt(),
            (STROKE_WIDTH + (0.015 * actualLength)).toInt(),
            (actualLength - (0.015 * actualLength) - STROKE_WIDTH).toInt(),
            (actualLength - (0.015 * actualLength) - STROKE_WIDTH).toInt()
        )

        currMouthYTopMultiplier =
            (if (currState == EMOT_STATE_HAPPY) HAPPY_MOUTH_Y_TOP_MULTIPLIER else SAD_MOUTH_Y_TOP_MULTIPLIER)
        currMouthYBottomMultiplier =
            (if (currState == EMOT_STATE_HAPPY) HAPPY_MOUTH_Y_BOTTOM_MULTIPLIER else SAD_MOUTH_Y_BOTTOM_MULTIPLIER)
        currMouthYCornerMultiplier =
            (if (currState == EMOT_STATE_HAPPY) HAPPY_MOUTH_Y_CORNER_MULTIPLIER else SAD_MOUTH_Y_CORNER_MULTIPLIER)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawEmotContainer(canvas)
        drawEmotGutter(canvas)
        drawEmotFace(canvas)
        drawEmotMouth(canvas)
        drawEmotEyes(canvas)
    }

    private fun drawEmotContainer(canvas: Canvas) = canvas.drawRoundRect(
        RectF(emotContainerRect),
        BORDER_RADIUS,
        BORDER_RADIUS,
        emotContainerPaint
    )

    private fun drawEmotGutter(canvas: Canvas) =
        canvas.drawRoundRect(RectF(emotGutterRect), BORDER_RADIUS, BORDER_RADIUS, emotGutterPaint)

    private fun drawEmotFace(canvas: Canvas) =
        canvas.drawRoundRect(RectF(emotFaceRect), BORDER_RADIUS, BORDER_RADIUS, emotBackgroundPaint)

    private fun drawEmotMouth(canvas: Canvas) {


        mouthPath.reset()
        mouthPath.moveTo(
            actualLength * 0.22f,
            actualLength * currMouthYCornerMultiplier
        )
        mouthPath.quadTo(
            actualLength * 0.5f,
            actualLength * currMouthYTopMultiplier,
            actualLength * 0.78f,
            actualLength * currMouthYCornerMultiplier
        )
        mouthPath.quadTo(
            actualLength * 0.5f,
            actualLength * currMouthYBottomMultiplier,
            actualLength * 0.22f,
            actualLength * currMouthYCornerMultiplier
        )
        canvas.drawPath(mouthPath, emotEyesMouthPaint)
    }

    private fun drawEmotEyes(canvas: Canvas) {
        val top = actualLength * 0.22f
        val bottom = top + 0.21f * actualLength
        val eyesLength = 0.105f * actualLength

        // Left eye
        val left = actualLength * 0.22f
        canvas.drawOval(left, top, left + eyesLength, bottom, emotEyesMouthPaint)

        // Right eye
        val right = actualLength * 0.78f
        canvas.drawOval(right - eyesLength, top, right, bottom, emotEyesMouthPaint)
    }
}