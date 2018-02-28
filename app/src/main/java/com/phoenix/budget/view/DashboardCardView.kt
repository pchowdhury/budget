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
import com.phoenix.budget.model.BudgetFilter
import com.phoenix.budget.model.Record
import kotlinx.android.synthetic.main.dashboard_card_view.view.*


/**
 * Created by Pushpan on 08/02/18.
 */

open class DashboardCardView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    lateinit var recordCallback: RecordCallback
    var filterName: String = ""

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
        val showMore = a.getBoolean(R.styleable.DashboardCardStyle_showMore, true)
        val filter = a.getString(R.styleable.DashboardCardStyle_filterName)
        filterName = filter ?: ""
        a?.recycle()
        setLabel(label)
        setEmptyLabel(emptyLabel)
        showMore(showMore)
        recycleView.layoutManager = LinearLayoutManager(context)
        listMore.setOnClickListener({ onMoreClick() })
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recycleView)
    }

    fun setLabel(title: String?) {
        listRecordsTitle.text = title
    }

    fun setEmptyLabel(emptyLabel: String?) {
        listEmpty.text = emptyLabel
    }

    fun showMore(show:Boolean?){
        listMore.visibility = if (show!=null && show) View.VISIBLE else View.GONE
    }

    fun setCardList(type: BudgetFilter.BudgetFilterType, list: MutableList<Record>) {
        recycleView.adapter =
                if (type == BudgetFilter.BudgetFilterType.Record)
                    RecordsAdapter(context, recordCallback, list)
                else
                    RemindersAdapter(context, recordCallback, list)
        listEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    open fun onSwipeRemove(position: Int) {
        if (recycleView.adapter != null) {
            recordCallback.onRemoveRecord(filterName, (recycleView.adapter as DataFetcher).getDataAtPosition(position))
        }
    }

    fun onMoreClick() {
    }

    fun setOnMoreClick(onClick: () -> Unit) {
        listMore.setOnClickListener({ onClick })
    }
}