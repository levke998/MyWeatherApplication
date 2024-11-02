package com.sl.myweatherapplication.ViewModel

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import androidx.core.graphics.alpha
import java.util.Calendar

class CountdownRingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val glowingPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        setShadowLayer(10f, 0f, 0f, Color.WHITE) // Állítsd be a ragyogó színt és méretet
        setAlpha(0.85f)
    }

    private var daysProgress = 0f
    private var hoursProgress = 0f
    private var minutesProgress = 0f
    private var secondsProgress = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()

        // Napok gyűrűje
        paint.color = Color.parseColor("#FF4081")
        paint.strokeWidth = 20f
        glowingPaint.color = Color.parseColor("#FF4081")
        glowingPaint.strokeWidth = 21f
        canvas.drawArc(40f, 40f, width - 40f, height - 40f, -90f, 360 * daysProgress, false, glowingPaint)
        canvas.drawArc(40f, 40f, width - 40f, height - 40f, -90f, 360 * daysProgress, false, paint)

        // Órák gyűrűje
        paint.color = Color.parseColor("#3F51B5")
        paint.strokeWidth = 16f
        glowingPaint.color = Color.parseColor("#3F51B5")
        glowingPaint.strokeWidth = 17f
        canvas.drawArc(70f, 70f, width - 70f, height - 70f, -90f, 360 * hoursProgress, false, glowingPaint)
        canvas.drawArc(70f, 70f, width - 70f, height - 70f, -90f, 360 * hoursProgress, false, paint)

        // Percek gyűrűje
        paint.color = Color.parseColor("#4CAF50")
        paint.strokeWidth = 12f
        glowingPaint.color = Color.parseColor("#4CAF50")
        glowingPaint.strokeWidth = 13f
        canvas.drawArc(100f, 100f, width - 100f, height - 100f, -90f, 360 * minutesProgress, false, glowingPaint)
        canvas.drawArc(100f, 100f, width - 100f, height - 100f, -90f, 360 * minutesProgress, false, paint)

        // Másodpercek gyűrűje
        paint.color = Color.parseColor("#FFEB3B")
        paint.strokeWidth = 8f
        glowingPaint.color = Color.parseColor("#FFEB3B")
        glowingPaint.strokeWidth = 9f
        canvas.drawArc(130f, 130f, width - 130f, height - 130f, -90f, 360 * secondsProgress, false, glowingPaint)
        canvas.drawArc(130f, 130f, width - 130f, height - 130f, -90f, 360 * secondsProgress, false, paint)

        canvas.restore()
    }

    fun updateCountdown(days: Long, hours: Long, minutes: Long, seconds: Long) {
        val currentDate = Calendar.getInstance()

        val daysSinceStart = 359 - currentDate.get(Calendar.DAY_OF_YEAR)
        val remainingHours = 23 - currentDate.get(Calendar.HOUR_OF_DAY)
        val remainingMinutes = 59 - currentDate.get(Calendar.MINUTE)
        val remainingSeconds = 59 - currentDate.get(Calendar.SECOND)

        daysProgress = daysSinceStart / 365f // Haladás az évben
        hoursProgress = remainingHours / 24f
        minutesProgress = remainingMinutes / 60f
        secondsProgress = remainingSeconds / 60f

        invalidate() // Újra megrajzolás
    }
}