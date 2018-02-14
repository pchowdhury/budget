package com.phoenix.budget.view

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.phoenix.budget.BR
import com.phoenix.budget.R
import com.phoenix.budget.RecordCallback
import com.phoenix.budget.model.Record
import com.phoenix.budget.presenter.RecordPresenter


/**
 * Created by Pushpan on 05/02/18.
 */

@BindingAdapter("bind:report", "bind:recordCallback", "bind:checkable")
fun setImageIcon(view: ImageView, record: Record, recordCallback: RecordCallback, checkable: Boolean) {
    if (checkable) {
        view.setOnClickListener {
//            presenter.removeDashboardReminder(record)
        }
    } else {
        view.setImageResource(recordCallback.getIconId(record.categoryId))
        view.setOnClickListener { recordCallback.showReport(record.categoryId) }
    }
}

class RecordsAdapter(val context: Context, val recordCallback: RecordCallback, val reports: MutableList<Record>) : RecyclerView.Adapter<RecordsAdapter.RecordViewHolder>() {

    inner class RecordViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Record) {
            binding.setVariable(BR.report, item)
            binding.setVariable(BR.recordCallback, recordCallback)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        // create a new view
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.record_item, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return RecordViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.bind(getDataAtPosition(position))
    }


    fun getDataAtPosition(position: Int): Record {
        return reports[position]
    }

    fun removeItem(position: Int) {
        if (reports.size > position) {
            reports.removeAt(position)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return reports.size
    }
}