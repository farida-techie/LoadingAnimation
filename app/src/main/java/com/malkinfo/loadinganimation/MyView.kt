package com.malkinfo.loadinganimation

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Nullable


/**
 *
 * Title: com.example.zqd.testbezial
 *
 * Description:
 *
 * Copyright: Copyright (c) 2017
 *
 * Company:
 *
 * @author zhangqingdong
 * @date 2018/6/22 10:30
 */
class MyView @JvmOverloads constructor(
    context: Context?,
    @Nullable attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) :
    View(context, attrs, defStyleAttr, defStyleRes) {
    /**
     * 波浪画笔
     */
    private var wavePaint: Paint? = null

    /**
     * 圆圈画笔
     */
    private var circlePaint: Paint? = null

    /**
     * 文字画笔
     */
    private var textPaint: Paint? = null

    /**
     * 控件宽度
     */
    private var screenWidth = 0

    /**
     * 控件高度
     */
    private var screenHeignt = 0

    /**
     * 振幅
     */
    private val amplitude = 100
    private var path: Path? = null

    /**
     * 进度
     */
    private var progress = 0f
    private var textProgress = 0f

    /**
     * 起始点
     */
    private val startPoint = Point()

    /**
     * 设置进度
     *
     * @param progress
     */
    fun setProgress(progress: Float) {
        textProgress = progress
        if (progress == 100f) {
            this.progress = progress + amplitude
        } else {
            this.progress = progress
        }
    }

    private fun init() {
        wavePaint = Paint()
        wavePaint!!.isAntiAlias = true
        wavePaint!!.strokeWidth = 1f
        textPaint = Paint()
        textPaint!!.style = Paint.Style.STROKE
        textPaint!!.isAntiAlias = true
        textPaint!!.color = Color.parseColor("#000000")
        textPaint!!.textSize = 50f
        circlePaint = Paint()
        circlePaint!!.isAntiAlias = true
        circlePaint!!.color = Color.parseColor("#292929")
        circlePaint!!.strokeWidth = 10f
        circlePaint!!.style = Paint.Style.STROKE
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Math.min(measureSize(400, widthMeasureSpec), measureSize(400, heightMeasureSpec))
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        screenWidth = w
        screenHeignt = h
        startPoint.x = -screenWidth
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        /**
         * 剪裁圆形区域
         */
        clipCircle(canvas)
        /**
         * 画圆边线
         */
        drawCircle(canvas)
        /**
         * 画波浪线
         */
        drawWave(canvas)
        /**
         * 画进度文字
         */
        drawText(canvas)
        postInvalidateDelayed(10)
    }

    /**
     * 绘制文字
     *
     * @param canvas
     */
    private fun drawText(canvas: Canvas) {
        val targetRect = Rect(0, -screenHeignt, screenWidth, 0)
        val fontMetrics = textPaint!!.fontMetricsInt
        val baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2
        textPaint!!.textAlign = Paint.Align.CENTER
        canvas.drawText((textProgress).toString()+"%", targetRect.centerX().toFloat(), baseline.toFloat(), textPaint!!)
    }
    /**
     * 画波浪
     *
     * @param canvas
     */
    private fun drawWave(canvas: Canvas) {
        val height = (progress / 100 * screenHeignt).toInt()
        startPoint.y = -height
        canvas.translate(0f, screenHeignt.toFloat())
        path = Path()
        wavePaint!!.style = Paint.Style.FILL
        wavePaint!!.color = Color.parseColor("#FF0303")
        val wave = screenWidth / 4
        path!!.moveTo(startPoint.x.toFloat(), startPoint.y.toFloat())
        for (i in 0..3) {
            val startX = startPoint.x + i * wave * 2
            val endX = startX + 2 * wave
            if (i % 2 == 0) {
                path!!.quadTo(
                    ((startX + endX) / 2).toFloat(),
                    (startPoint.y + amplitude).toFloat(),
                    endX.toFloat(),
                    startPoint.y.toFloat()
                )
            } else {
                path!!.quadTo(
                    ((startX + endX) / 2).toFloat(),
                    (startPoint.y - amplitude).toFloat(),
                    endX.toFloat(),
                    startPoint.y.toFloat()
                )
            }
        }
        path!!.lineTo(screenWidth.toFloat(), (screenHeignt / 2).toFloat())
        path!!.lineTo(-screenWidth.toFloat(), (screenHeignt / 2).toFloat())
        path!!.lineTo(-screenWidth.toFloat(), 0f)
        path!!.close()
        canvas.drawPath(path!!, wavePaint!!)
        startPoint.x += 10
        if (startPoint.x > 0) {
            startPoint.x = -screenWidth
        }
        path!!.reset()
    }

    /**
     * 画圆形
     *
     * @param canvas
     */
    private fun drawCircle(canvas: Canvas) {
        canvas.drawCircle(
            (screenHeignt / 2).toFloat(),
            (screenHeignt / 2).toFloat(),
            (screenHeignt / 2).toFloat(),
            circlePaint!!
        )
    }

    /**
     * 剪裁画圆
     *
     * @param canvas
     */
    private fun clipCircle(canvas: Canvas) {
        val circlePath = Path()
        circlePath.addCircle(
            (screenWidth / 2).toFloat(),
            (screenHeignt / 2).toFloat(),
            (screenHeignt / 2).toFloat(),
            Path.Direction.CW
        )
        canvas.clipPath(circlePath)
    }

    private fun measureSize(defaultSize: Int, measureSpec: Int): Int {
        var result = defaultSize
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        when (mode) {
            MeasureSpec.UNSPECIFIED -> result = defaultSize
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> result = size
            else -> {}
        }
        return result
    }

    init {
        init()
    }
}