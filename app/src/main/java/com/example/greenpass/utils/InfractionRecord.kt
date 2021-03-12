package com.example.greenpass.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class InfractionRecord(
    val locationName: String,
    val date: LocalDateTime
){
    constructor(locationName: String, date: String) :
            this(locationName, LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    fun getReadableDate(): String{
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).format(date)
    }
    fun getISODate(): String{
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

}