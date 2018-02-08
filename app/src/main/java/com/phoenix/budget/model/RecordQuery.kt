package com.phoenix.budget.model

import android.support.annotation.IntDef

/**
 * Created by Pushpan on 08/02/18.
 */

class RecordQuery {
    @IntDef(ALL, CATEGORY)
    @Retention(AnnotationRetention.SOURCE)
    annotation class RecordQuery

    companion object {
        const val ALL = 0L
        const val CATEGORY = 1L
    }
}