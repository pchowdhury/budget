package com.phoenix.budget

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.phoenix.budget.databinding.ActivityReportsBinding
import com.phoenix.budget.model.Record
import com.phoenix.budget.model.viewmodel.ModelResponse
import com.phoenix.budget.model.viewmodel.RecordViewModel
import com.phoenix.budget.view.RecordsAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class RecordsActivity : BudgetBaseActivity(), RecordCallback {
    lateinit var binding: ActivityReportsBinding
    lateinit var viewModel: RecordViewModel

    var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//            presenter.removeDashboardRecentRecord((binding.contentReports?.recycleView?.adapter as RecordsAdapter).getDataAtPosition(viewHolder.adapterPosition))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reports)
        setSupportActionBar(toolbar)
        configureToolBar()
        binding.contentReports?.recycleView?.layoutManager = LinearLayoutManager(this)
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView( binding.contentReports?.recycleView)
        viewModel = ViewModelProviders.of(this).get(RecordViewModel::class.java)
        viewModel.response().observe(this, Observer{
            response-> onBindRecords(response)
        })
        loadRecords()
    }

    private fun onBindRecords(modelResponse: ModelResponse?) {
        when (modelResponse?.status) {
            ModelResponse.Loading -> {

            }
            ModelResponse.Error -> {

            }
            ModelResponse.Success -> {
                val list = modelResponse.value as MutableList<Record>
                binding.contentReports?.recycleView?.adapter = RecordsAdapter(this, this, list)
            }
        }
    }

    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_records)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun markReminderDone(record: Record) {
    }


    fun loadRecords() {
        val categoryId = intent.getIntExtra(INTENT_REQUEST_VIEW, -1)
        if (categoryId == -1) {
            viewModel.loadRecentRecord()
        } else {
            viewModel.loadRecordsByCategoryId(categoryId)
        }
    }

    override fun showReport(categoryId: Int) {
    }
//
//    override fun updateRecentRecords(list: MutableList<Record>) {
//        binding.contentReports?.recycleView?.adapter = RecordsAdapter(this, presenter, list)
//    }
//
//    override fun updateReminders(list: MutableList<Record>) {
//    }
//
//    override fun showError(text: String) {
//        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
//    }
//
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRemoveRecord(record: Record) {
    }

    companion object {
        @JvmStatic
        val INTENT_REQUEST_VIEW: String = "INTENT_REQUEST_VIEW"
        @JvmStatic
        val INTENT_REQUEST_IS_RESTRICTED: String = "INTENT_REQUEST_IS_RESTRICTED"
    }
}
