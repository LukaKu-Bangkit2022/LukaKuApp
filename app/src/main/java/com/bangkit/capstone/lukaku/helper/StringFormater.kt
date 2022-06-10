package com.bangkit.capstone.lukaku.helper

import com.bangkit.capstone.lukaku.utils.Constants.DATE_FORMAT
import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.withFirstUpperCase(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
    }
}

fun String.withDateFormat(): String {
    val format = SimpleDateFormat(DATE_FORMAT, Locale.US)
    val date = format.parse(this) as Date
    val dateFormat = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT)

    return dateFormat.format(date)
}

fun String.withCurrencyFormat(): String {
    val rupiahExchangeRate = 12495.95
    val euroExchangeRate = 0.88

    var priceOnDollar = this.toDouble() / rupiahExchangeRate
    var currencyFormat = NumberFormat.getCurrencyInstance()
    val deviceLocale = Locale.getDefault().country

    when {
        deviceLocale.equals("ES") -> priceOnDollar *= euroExchangeRate
        deviceLocale.equals("ID") -> priceOnDollar *= rupiahExchangeRate
        else -> currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
    }
    return currencyFormat.format(priceOnDollar)
}