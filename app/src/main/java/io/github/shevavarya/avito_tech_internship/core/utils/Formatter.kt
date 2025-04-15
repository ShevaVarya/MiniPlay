package io.github.shevavarya.avito_tech_internship.core.utils

import java.text.SimpleDateFormat
import java.util.Locale

fun msToMinute(sec: Long): String =
    SimpleDateFormat("mm:ss", Locale.getDefault()).format(sec)