package com.phoenix.budget.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Pushpan on 03/02/18.
 */
class StringUtils {
    companion object {
        @JvmStatic
        fun getValidCurrency(value: String): Double =
                if (value.isEmpty()) {
                    0f.toDouble()
                } else {
                    value.toDouble()
                }

        @JvmStatic
        fun getValidCurrencyString(double: Double?): String {
            if (double==null || double == 0f.toDouble()) {
                return ""
            } else {
                return double.toString()
            }
        }

        @JvmStatic
        fun getMonthfrom(date: Date?): String {
            val myFormat = "MMM" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            return  sdf.format(date)
        }

        @JvmStatic
        fun getDayfrom(date: Date?): String {
            val myFormat = "dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            return  sdf.format(date)
        }
    }
}