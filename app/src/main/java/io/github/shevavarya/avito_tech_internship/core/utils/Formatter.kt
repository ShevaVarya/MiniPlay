package io.github.shevavarya.avito_tech_internship.core.utils

import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

fun msToMinute(sec: Long): String =
    SimpleDateFormat("mm:ss", Locale.getDefault()).format(sec)

fun dpToPx(dp: Float, context: Context): Int =
    TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()