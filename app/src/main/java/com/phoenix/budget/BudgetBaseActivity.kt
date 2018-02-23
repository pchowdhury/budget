package com.phoenix.budget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.phoenix.budget.model.ViewRequestID

open class BudgetBaseActivity : AppCompatActivity(), CategoryCallback {

    var iconArr: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iconArr = loadIcons(this)
    }

    private fun loadIcons(context: Context): IntArray {
        val ar = context.resources.obtainTypedArray(R.array.category_icons)
        val len = ar.length()
        val resIds = IntArray(len)
        for (i in 0 until len)
            resIds[i] = ar.getResourceId(i, 0)
        ar.recycle()
        return resIds
    }

    override fun getIconId(catogoryId: Int): Int = if (catogoryId == -1) iconArr!![iconArr!!.size - 1] else iconArr!![catogoryId]

    fun starViewRecords(category: Int) {
        val intent = Intent(this, RecordsActivity::class.java)
        intent.putExtra(RecordsActivity.INTENT_REQUEST_VIEW, category)
        intent.putExtra(RecordsActivity.INTENT_REQUEST_IS_RESTRICTED, true)
        startActivityForResult(intent, ViewRequestID.RecordScreen.ordinal)
    }
}
