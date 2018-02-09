package com.phoenix.budget.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.phoenix.budget.model.Record
import kotlinx.android.synthetic.main.dashboard_card_view.view.*

/**
 * Created by Pushpan on 09/02/18.
 */

class UpcomingRemindersDashboardView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : DashboardCardView(context, attrs, defStyleAttr) {


    override fun setCardList(list: MutableList<Record>) {
        recycleView.adapter = RemindersAdapter(context, presenter, list)
        listEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
    }

    override fun onSwipeRemove(position: Int){
//        presenter.removeDashboardReminder((recycleView.adapter as RemindersAdapter).getDataAtPosition(position))
    }
}

