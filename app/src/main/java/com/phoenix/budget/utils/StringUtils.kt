package com.phoenix.budget.utils

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
    }
}