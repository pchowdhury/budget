package com.phoenix.budget

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.phoenix.budget.databinding.ActivityTransactionBinding
import com.phoenix.budget.fragment.PopMenuItemType
import com.phoenix.budget.model.CategorizedRecord
import com.phoenix.budget.presenter.TransactionPresenter
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.text.SimpleDateFormat
import java.util.*

class TransactionActivity : AppCompatActivity(), TransactionCallback {
    lateinit var binding: ActivityTransactionBinding
    lateinit var presenter: TransactionPresenter
    val pickDate: AppCompatTextView by lazy { findViewById<AppCompatTextView>(R.id.txtPickDate) }
    var cal = Calendar.getInstance()
    lateinit var menu: PopMenuItemType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)
        presenter = TransactionPresenter(this)
        binding.presenter = presenter
        setUpUI()
        presenter.setCategorizedRecord(intent.getStringExtra(RECORD_ID), intent.getBooleanExtra(IS_INCOME, false))
    }

    private fun setUpUI() {
        menu = PopMenuItemType.values()[intent.getIntExtra(MODE, 0)]
        configureToolBar()
        pickDate.setOnClickListener({onSelectDate()})
    }

    private fun onSelectDate() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
            onSelectTime()
        }
        DatePickerDialog(this,
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()

    }

    private fun onSelectTime() {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
            cal.set(Calendar.MINUTE, minute)
            updateDateInView()
        }
        TimePickerDialog(this,
                timeSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE), true).show()

    }


    private fun updateDateInView() {
        val myFormat = "dd/MM/yyyy hh:mm" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        pickDate.text = sdf.format(cal.getTime())
    }

    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = menu.label.replace("\n", " ")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun showError(text: String) {
       Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onBindRecord(categorizedRecord: CategorizedRecord) {
        binding.categorizedRecord = categorizedRecord
        binding.categoryView.setCategorizedReport(categorizedRecord)
        binding.executePendingBindings()
    }

    override fun closeRecord() {
        finish()
    }

    override fun addAnotherRecord() {
        presenter.setCategorizedRecord("", intent.getBooleanExtra(IS_INCOME, false))
    }

    override fun onDestroy() {
        presenter.cleanUp()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_transaction, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ok -> {
                presenter.save()
                finish()
                return true
            }
            android.R.id.home -> {
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        @JvmStatic
        val MODE: String = "MODE"
        @JvmStatic
        val RECORD_ID: String = "RECORD_ID"
        @JvmStatic
        val IS_INCOME: String = "IS_INCOME"
    }
}
