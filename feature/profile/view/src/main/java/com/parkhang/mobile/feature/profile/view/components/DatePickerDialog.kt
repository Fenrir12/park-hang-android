package com.parkhang.mobile.feature.profile.view.components

import android.app.DatePickerDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onCancelled: () -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            // Update calendar to selected date
            calendar.set(year, month, dayOfMonth)

            // Format to YYYY-MM-DD
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = format.format(calendar.time)

            // Pass formatted date to parent
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
    ).apply {
        setOnCancelListener {
            onCancelled()
        }
    }.show()
}
