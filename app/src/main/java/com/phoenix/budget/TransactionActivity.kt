package com.phoenix.budget

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.phoenix.budget.databinding.ActivityTransactionBinding
import com.phoenix.budget.persenter.TransactionPresenter
import kotlinx.android.synthetic.main.activity_dashboard.*

class TransactionActivity : AppCompatActivity(), TransactionCallback {
    lateinit var binding: ActivityTransactionBinding
    lateinit var presenter: TransactionPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction)
        setSupportActionBar(toolbar)
        presenter = TransactionPresenter(this)
        binding.presenter = presenter
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
            R.id.action_settings ->
                return true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
