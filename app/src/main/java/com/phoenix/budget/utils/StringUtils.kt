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

        @JvmStatic
        fun getActualDatefrom(date: Date?): String {
            val myFormat = "dd MMM YY" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            return  sdf.format(date)
        }

        @JvmStatic
        fun getTimeTimeUntil(date: Date): String {
            val now = System.currentTimeMillis()
            var passed = (now - date.time) / 1000
            val buff =  StringBuffer()


            val duration = arrayOf(
                    (12 * 30 * 24 * 60 * 60),//years
                    (30 * 24 * 60 * 60),//months
                    (24 * 60 * 60), //days
                    (60 * 60), //hour
                    (60)//min
            )
            val label = arrayListOf(
                    "year",
                    "month",
                    "day",
                    "hour",
                    "min"
            )

            for( i in 0 until duration.size){
                val t = passed / duration[i]
                passed %= duration[i]
                if (t != 0L) {
                    buff.append(t).append(" ").append(label[i])
                    if (t > 1) {
                        buff.append("s")
                    }
                    return buff.append(" ago").toString()
                }
            }
            return "now"
        }
    }
}