package com.phoenix.budget.utils

import android.databinding.BindingAdapter
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import com.phoenix.budget.R
import com.phoenix.budget.RecordCallback
import com.phoenix.budget.model.Record
import com.phoenix.budget.model.RecurringRecord

/**
 * Created by Pushpan on 15/02/18.
 */

@BindingAdapter("bind:record", "bind:recordCallback", "bind:checkable")
fun setImageIcon(view: ImageView, record: Record, recordCallback: RecordCallback, checkable: Boolean) {
    if (checkable) {
        view.setOnClickListener {
            recordCallback.markReminderDone(record)
        }
        view.setColorFilter(ContextCompat.getColor(view.context, if (record.hasPassedDeadline()) R.color.color_red else R.color.color_green))
    } else {
        view.setImageResource(recordCallback.getIconId(record.categoryId))
        view.setOnClickListener { recordCallback.showReport(record.categoryId.toString()) }
    }
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean) {
    view.visibility = (if (value) View.VISIBLE else View.GONE)
}

@BindingAdapter("bind:selectedPosition")
fun setSelectedPosition(view: Spinner, value: RecurringRecord.RepeatType) {
    view.setSelection(value.ordinal)
}
