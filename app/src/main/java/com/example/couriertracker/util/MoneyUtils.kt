package com.example.couriertracker.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale


private val ruLocale = Locale.forLanguageTag("ru-RU")

fun String.toKopecksOrNull(): Long? {
    val value = trim()
        .replace(" ", "")
        .replace('\u00A0', ' ')
        .replace(",", ".")

    if (value.isEmpty()) return null

    return try {
        BigDecimal(value)
            .movePointRight(2)
            .longValueExact()
    } catch (_: NumberFormatException) {
        null
    } catch (_: ArithmeticException) {
        null
    }
}


fun Long.toRublesString(): String =
    BigDecimal.valueOf(this, 2)
        .stripTrailingZeros()
        .toPlainString()

fun Long.toMoneyString(): String {
    val formatter = NumberFormat.getNumberInstance(ruLocale).apply {
        minimumFractionDigits = 0
        maximumFractionDigits = 2
    }

    return "${formatter.format(BigDecimal.valueOf(this, 2))} ₽"
}

fun Long.toLongMoneyString(): String {
    val formatter = NumberFormat.getNumberInstance(ruLocale).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }

    return "${formatter.format(BigDecimal.valueOf(this, 2))} ₽"
}