package com.phoenix.budget.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.phoenix.budget.R
import com.phoenix.budget.model.Category

/**
 * Created by Pushpan on 05/02/18.
 */

class ReportsAdapter(val category: List<Category>) : RecyclerView.Adapter<ReportsAdapter.ReportViewHolder>() {
    inner class ReportViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        // each data item is just a string in this case
//        val imgView = binding.root.findViewById<ImageView>(R.id.imgView)

        fun bind(category: Category, position: Int) {
//            imgView.setColorFilter(Color.WHITE)
//            imgView.setOnClickListener(
//                    {
//                        val deselecting = selectedCategorizedRecord?.categoryId == position
//                        selectedCategorizedRecord?.categoryId = if (deselecting) -1 else position
//                        notifyDataSetChanged()
//                    })
//            imgView.setImageResource(iconArr[iconArr.size - 1])
//
//            imgView.setImageResource(if ((category.id < iconArr.size) && category.id != -1) iconArr[(category.id)] else iconArr[iconArr.size - 1])
//            imgView.isSelected = position == selectedCategorizedRecord?.categoryId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        // create a new view
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, getLayoutIdForType(viewType), parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ReportViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(getDataAtPosition(position), position)
    }

    fun getDataAtPosition(position: Int): Category {
        return category[position]
    }

    fun getLayoutIdForType(viewType: Int): Int {
        return R.layout.category_item
    }

    override fun getItemCount(): Int {
        return category.size
    }
}