package com.phoenix.budget.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.phoenix.budget.R


/**
 * TODO: document your custom view class.
 */
class CategoryView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {


    init {
        val layoutInflater = LayoutInflater.from(context)
        val binding: ViewDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.category_view, this, true)
        val recycleView = binding.root.findViewById<RecyclerView>(R.id.recycleView)
        recycleView.layoutManager = GridLayoutManager(context, 5)
    }


}
