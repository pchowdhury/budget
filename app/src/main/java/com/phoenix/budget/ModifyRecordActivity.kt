package com.phoenix.budget

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.view.Menu
import android.view.MenuItem
import com.jakewharton.rxbinding.widget.RxTextView
import com.phoenix.budget.databinding.ActivityModifyRecordBinding
import com.phoenix.budget.fragment.PopMenuItemType
import com.phoenix.budget.model.CategorizedRecord
import com.phoenix.budget.model.viewmodel.ModelResponse
import com.phoenix.budget.model.viewmodel.ModifyRecordViewModel
import com.phoenix.budget.utils.StringUtils
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.text.SimpleDateFormat
import java.util.*


class ModifyRecordActivity : AppCompatActivity() {
    lateinit var binding: ActivityModifyRecordBinding
    val pickDate: AppCompatTextView by lazy { findViewById<AppCompatTextView>(R.id.txtPickDate) }
    var cal = Calendar.getInstance()
    lateinit var menu: PopMenuItemType
    var hasSaved = false
    lateinit var viewModel:ModifyRecordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_record)
        setUpUI()
        viewModel.response().observe(this, android.arch.lifecycle.Observer<ModelResponse> {
            response -> onBindRecord(response)
        })
        viewModel.setCategorizedRecord(intent.getStringExtra(RECORD_ID),
                intent.getBooleanExtra(IS_INCOME, false),
                menu == PopMenuItemType.RecurringExpense || menu == PopMenuItemType.RecurringIncome)
    }

    private fun setUpUI() {
        menu = PopMenuItemType.values()[intent.getIntExtra(MODE, 0)]
        configureToolBar()
        pickDate.setOnClickListener({ onSelectDate() })

        RxTextView.afterTextChangeEvents(binding.editTitle)
                .subscribe { textChangeEvent -> viewModel.editableCategorizedRecord().title = textChangeEvent.editable().toString() }

        RxTextView.afterTextChangeEvents(binding.editAmount)
                .subscribe { textChangeEvent -> viewModel.editableCategorizedRecord().amount = StringUtils.getValidCurrency(textChangeEvent.editable().toString()) }

        RxTextView.afterTextChangeEvents(binding.editNote)
                .subscribe { textChangeEvent -> viewModel.editableCategorizedRecord().note = textChangeEvent.editable().toString() }
    }


    private fun onSelectDate() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
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
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
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
        viewModel.editableCategorizedRecord().createdFor = Date(cal.timeInMillis)
    }

    private fun configureToolBar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = menu.label.replace("\n", " ")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun onBindRecord(modelResponse: ModelResponse?) {
        when(modelResponse?.status){
            ModelResponse.Loading ->{

            }
            ModelResponse.Error ->{

            }
            ModelResponse.Success ->{
                val categorizedRecord = modelResponse.value as CategorizedRecord
                binding.categorizedRecord = categorizedRecord
                binding.categoryView.setCategorizedReport(categorizedRecord)
                binding.executePendingBindings()
            }
            ModelResponse.ACTION_1 ->{//save
                validateResult()
                finish()
            }
            ModelResponse.ACTION_2 ->{//add another
                viewModel.openNewRecord()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_transaction, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_ok -> {
                viewModel.save(false)
                return true
            }
            android.R.id.home -> {
                validateResult()
                finish()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun validateResult(){
        setResult(if (hasSaved) Activity.RESULT_OK else Activity.RESULT_CANCELED)
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
