package com.phoenix.budget.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.phoenix.budget.BR
import com.phoenix.budget.R
import com.phoenix.budget.model.Record
import com.phoenix.budget.presenter.RecordPresenter


/**
 * Created by Pushpan on 05/02/18.
 */


class RemindersAdapter(val context: Context, val presenter: RecordPresenter, val reports: MutableList<Record>) : RecyclerView.Adapter<RemindersAdapter.RecordViewHolder>() {

    inner class RecordViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Record) {
            binding.setVariable(BR.report, item)
            binding.setVariable(BR.presenter, presenter)
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        // create a new view
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.record_reminder_item, parent, false)
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