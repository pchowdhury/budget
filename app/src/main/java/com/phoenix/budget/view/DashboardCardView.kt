package com.phoenix.budget.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.phoenix.budget.R
import com.phoenix.budget.RecordCallback
import com.phoenix.budget.model.Record
import kotlinx.android.synthetic.main.dashboard_card_view.view.*


/**
 * Created by Pushpan on 08/02/18.
 */

open class DashboardCardView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    lateinit var recordCallback: RecordCallback

    var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            onSwipeRemove(viewHolder.adapterPosition)
        }
    }

    init {
        View.inflate(context, R.layout.dashboard_card_view, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.DashboardCardStyle, defStyleAttr, 0)
        val label = a.getString(R.styleable.DashboardCardStyle_label)
        val emptyLabel= a.getString(R.styleable.DashboardCardStyle_empty)
        a?.recycle()
        listRecordsTitle.text = label
        listEmpty.text = emptyLabel
        recycleView.layoutManager = LinearLayoutManager(context)
        listMore.setOnClickListener({ onMoreClick() })
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recycleView)
    }

    open fun setCardList(list: MutableList<Record>) {
    }

    open fun onSwipeRemove(position: Int){
    }

    fun onMoreClick() {
        recordCallback.showReport(-1)
    }

    companion object {
        @JvmStatic
        val MAX_ROWS = 20
    }
}