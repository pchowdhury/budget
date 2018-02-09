package com.phoenix.budget

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.phoenix.budget.databinding.ActivityDashboardBinding
import com.phoenix.budget.fragment.MenuCallback
import com.phoenix.budget.fragment.MenuFragment
import com.phoenix.budget.fragment.PopMenuItemType
import com.phoenix.budget.model.Record
import com.phoenix.budget.presenter.ReportPresenter
import com.phoenix.budget.view.DashboardCardView
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BudgetBaseActivity(), ReportCallback, MenuCallback {
    lateinit var binding: ActivityDashboardBinding
    lateinit var presenter: ReportPresenter
    val menuFragment = MenuFragment()


    private val REQUEST_ADD = 1
    private val REQUEST_VIEW = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        presenter = ReportPresenter(this)
        binding.presenter = presenter
        supportFragmentManager.beginTransaction().replace(R.id.menu_container, menuFragment, MenuFragment.TAG).commit()
        binding.contentDashboard?.cardViewRecentRecords?.presenter = presenter
        loadDashboardRecords()
    }

    fun loadDashboardRecords(){
        presenter.loadRecords(DashboardCardView.MAX_ROWS)
    }

    override fun updateRecords(list: MutableList<Record>) {
        binding.contentDashboard?.cardViewRecentRecords?.setCardList(list)
    }

    override fun reloadRecords() {
        loadDashboardRecords()
    }

    override fun showReport(categoryId: Int) {
        starViewRecords(categoryId)
    }

    override fun showError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onSelectMenuItem(menuItem: PopMenuItemType) = startModifyRecord(menuItem)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings ->
                return true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun startModifyRecord(menuItem: PopMenuItemType) {
        val intent = Intent(this, ModifyRecordActivity::class.java)
        intent.putExtra(ModifyRecordActivity.MODE, menuItem.ordinal)
        intent.putExtra(ModifyRecordActivity.RECORD_ID, "")
        startActivityForResult(intent, REQUEST_ADD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (resultCode) {
                REQUEST_ADD ->  loadDashboardRecords()
            }
        }
    }

    fun starViewRecords(category: Int) {
        val intent = Intent(this, RecordsActivity::class.java)
        intent.putExtra(RecordsActivity.INTENT_REQUEST_VIEW, category)
        intent.putExtra(RecordsActivity.INTENT_REQUEST_IS_RESTRICTED, true)
        startActivityForResult(intent, REQUEST_VIEW)
    }
}
