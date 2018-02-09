package com.phoenix.budget

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.phoenix.budget.databinding.ActivityReportsBinding
import com.phoenix.budget.model.Record
import com.phoenix.budget.presenter.ReportPresenter
import com.phoenix.budget.view.DashboardCardView
import com.phoenix.budget.view.RecordsAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class RecordsActivity : BudgetBaseActivity(), ReportCallback {
    lateinit var binding: ActivityReportsBinding
    lateinit var presenter: ReportPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reports)
        setSupportActionBar(toolbar)
        configureToolBar()
        presenter = ReportPresenter(this)
        binding.presenter = presenter
        binding.contentReports?.recycleView?.layoutManager = LinearLayoutManager(this)
        loadRecords()
    }

    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.title_records)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    fun loadRecords() {
        val categoryId = intent.getIntExtra(INTENT_REQUEST_VIEW, -1)
        val isRestricted = intent.getBooleanExtra(INTENT_REQUEST_IS_RESTRICTED, false)

        if (categoryId == -1) {
            presenter.loadRecords(-1)
        } else {
            presenter.loadRecordsByCategoryId(categoryId, if (isRestricted) DashboardCardView.MAX_ROWS else -1)
        }

    }

    override fun showReport(categoryId: Int) {
    }

    override fun updateRecords(list: List<Record>) {
        binding.contentReports?.recycleView?.adapter = RecordsAdapter(this, presenter, list)
    }

    override fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

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

    companion object {
        @JvmStatic
        val INTENT_REQUEST_VIEW: String = "INTENT_REQUEST_VIEW"
        @JvmStatic
        val INTENT_REQUEST_IS_RESTRICTED: String = "INTENT_REQUEST_IS_RESTRICTED"
    }
}
