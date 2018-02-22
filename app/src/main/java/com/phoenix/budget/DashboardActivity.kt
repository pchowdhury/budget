package com.phoenix.budget

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.phoenix.budget.databinding.ActivityDashboardBinding
import com.phoenix.budget.dialog.ConfirmationDialogFragment
import com.phoenix.budget.fragment.MenuCallback
import com.phoenix.budget.fragment.MenuFragment
import com.phoenix.budget.fragment.PopMenuItemType
import com.phoenix.budget.model.Record
import com.phoenix.budget.model.viewmodel.DashboardViewModel
import com.phoenix.budget.model.viewmodel.ModelResponse
import com.phoenix.budget.model.viewmodel.ViewRequestID
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BudgetBaseActivity(), RecordCallback, MenuCallback, ConfirmationDialogFragment.ConfirmationCallback {
    lateinit var binding: ActivityDashboardBinding
    lateinit var viewModel: DashboardViewModel
    val menuFragment = MenuFragment()


    private val DIALOG_REMOVE_RECORD = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        viewModel.recentRecordsResponse().observe(this, Observer<ModelResponse> { response ->
            onBindRecentRecords(response)
        })
        viewModel.reminderRecordsResponse().observe(this, Observer<ModelResponse> { response ->
            onBindReminderRecords(response)
        })

        viewModel.addRemindersResponse().observe(this, Observer<ModelResponse> { response ->
            onFinishUpdatingReminders(response)
        })

        viewModel.updateRemindersResponse().observe(this, Observer<ModelResponse> { response ->
            onFinishUpdatingReminders(response)
        })

        supportFragmentManager.beginTransaction().replace(R.id.menu_container, menuFragment, MenuFragment.TAG).commit()
        viewModel.loadData(false)
    }

    private fun onFinishUpdatingReminders(modelResponse: ModelResponse?) {
        when (modelResponse?.status) {
            ModelResponse.Loading -> {

            }
            ModelResponse.Error -> {
                viewModel.loadData(true)
            }
            ModelResponse.Success -> {
                viewModel.loadData(true)
            }
        }
    }

    private fun onBindReminderRecords(modelResponse: ModelResponse?) {
        when (modelResponse?.status) {
            ModelResponse.Loading -> {

            }
            ModelResponse.Error -> {

            }
            ModelResponse.Success -> {
                val list = modelResponse.value as MutableList<Record>
                binding.contentDashboard?.cardViewUpcomingReminders?.recordCallback = this
                binding.executePendingBindings()
                binding.contentDashboard?.cardViewUpcomingReminders?.setCardList(list)
            }
        }
    }

    private fun onBindRecentRecords(modelResponse: ModelResponse?) {
        when (modelResponse?.status) {
            ModelResponse.Loading -> {

            }
            ModelResponse.Error -> {

            }
            ModelResponse.Success -> {
                val list = modelResponse.value as MutableList<Record>
                binding.contentDashboard?.cardViewRecentRecords?.recordCallback = this
                binding.executePendingBindings()
                binding.contentDashboard?.cardViewRecentRecords?.setCardList(list)
            }
        }
    }

    override fun showReport(categoryId: Int) {
        starViewRecords(categoryId)
    }

    override fun markReminderDone(record: Record) {
        viewModel.markReminderDone(record)
    }

    override fun onRemoveRecord(record: Record) {
        viewModel.recordTobeDeleted = record
        if (!record.done) {
            val f = ConfirmationDialogFragment.create(DIALOG_REMOVE_RECORD, getString(R.string.dialog_title_confirmation), getString(R.string.dialog_recurring_confirmation))
            f.show(supportFragmentManager, ConfirmationDialogFragment.TAG)
        } else {
            viewModel.removeDashboardSingleRecord()
        }
    }

    override fun onPositiveResponse(type: Int) {
        when (type) {
            DIALOG_REMOVE_RECORD -> viewModel.removeDashboardRecurringRecord()
        }
    }

    override fun onNegativeResponse(type: Int) {
        when (type) {
            DIALOG_REMOVE_RECORD -> viewModel.removeDashboardSingleRecord()
        }
    }

    override fun onRespondingCancel(type: Int) {
        when (type) {
            DIALOG_REMOVE_RECORD ->   viewModel.clearRecordTobeDeleted()
        }
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
        startActivityForResult(intent, ViewRequestID.ModifyRecordScreen.ordinal)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                ViewRequestID.ModifyRecordScreen.ordinal -> {
                    viewModel.updateReminders()
                }
            }
        }
    }
}
