package com.vinilos.misw4203.grupo6_202412.view.utils

import java.text.SimpleDateFormat
import java.util.Locale


fun formatDateString(inputDate: String, inputFormat: String, outputFormat: String): String {
    val originalFormat = SimpleDateFormat(inputFormat, Locale.US)
    val targetFormat = SimpleDateFormat(outputFormat, Locale.US)

    val date = originalFormat.parse(inputDate)
    val formattedDate = date?.let { targetFormat.format(it) }

    return formattedDate ?: ""
}