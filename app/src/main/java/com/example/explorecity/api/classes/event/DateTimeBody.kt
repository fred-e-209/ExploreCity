package com.example.explorecity.api.classes.event

data class DateTimeBody(
    val day: Int,
    val hour: Int,
    val minute: Int,
    val month: Int,
    val year: Int
)

fun stringToDateTimeBody(inputDate: String, inputTime: String): DateTimeBody {
    val dateParts = inputDate.split("-")
    val timeParts = inputTime.split(":")

    if (dateParts.size == 3 && timeParts.size == 2) {
        try {
            val day = dateParts[0].toInt()
            val month = dateParts[1].toInt()
            val year = dateParts[2].toInt()

            val hour = timeParts[0].toInt()
            val min = timeParts[1].toInt()

            // You can add additional validation for day, month, and year if needed
            return DateTimeBody(day = day, month = month, year = year, hour = hour, minute = min)
        } catch (e: NumberFormatException) {
            // Handle the case where conversion to Int fails
            println("Invalid date format: $inputDate")
        }
    } else {
        // Handle the case where the input date format is not as expected
        println("Invalid date format: $inputDate")
    }

    return DateTimeBody(0, 0, 0, 0, 0)
}