package com.phoenix.budget.model.viewmodel

import android.support.annotation.Nullable


/**
 * Created by Pushpan on 12/02/18.
 */
class ModelResponse(val status: Long, @Nullable var value: Any?, @Nullable var error: Throwable?) {

    companion object {
        @JvmStatic
        val Loading = 0L
        @JvmStatic
        val Success = 1L
        @JvmStatic
        val Error = 2L
        @JvmStatic
        val ACTION_1 = 3L
        @JvmStatic
        val ACTION_2 = 4L

        @JvmStatic
        fun success(value: Any?): ModelResponse = ModelResponse(Success, value,  null)

        @JvmStatic
        fun error(value: Throwable?): ModelResponse = ModelResponse(Error, null, value)

        @JvmStatic
        fun loading(): ModelResponse = ModelResponse(Loading, null, null)

        @JvmStatic
        fun action(action: Long): ModelResponse = ModelResponse(action, null, null)

    }
}