package com.phoenix.budget.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.phoenix.budget.R
import com.phoenix.budget.model.Record
import com.phoenix.budget.presenter.ReportPresenter
import kotlinx.android.synthetic.main.dashboard_card_view.view.*


/**
 * Created by Pushpan on 08/02/18.
 */

class DashboardCardView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    lateinit var presenter: ReportPresenter

    var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            (recycleView.adapter as RecordsAdapter).removeItem(viewHolder.adapterPosition)
            presenter.removeDashboardRecord((recycleView.adapter as RecordsAdapter).getDataAtPosition(viewHolder.adapterPosition))
        }
    }

    init {
        View.inflate(context, R.layout.dashboard_card_view, this)
        val a = context.obtainStyledAttributes(attrs, R.styleable.DashboardCardStyle, defStyleAttr, 0)
        val label = a.getString(R.styleable.DashboardCardStyle_label)
        a?.recycle()
        listRecordsTitle.text = label
        recycleView.layoutManager = LinearLayoutManager(context)
        listMore.setOnClickListener({ onMoreClick() })
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recycleView)
    }

    fun setCardList(list: MutableList<Record>) {
        recycleView.adapter = RecordsAdapter(context, presenter, list)
        listEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    fun onMoreClick() {
        presenter.reportCallback.showReport(-1)
    }

    companion object {
        @JvmStatic
        val MAX_ROWS = 3
    }
}