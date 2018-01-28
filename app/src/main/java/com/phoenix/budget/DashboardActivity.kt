package com.phoenix.budget

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.phoenix.budget.databinding.ActivityDashboardBinding
import com.phoenix.budget.fragment.MenuCallback
import com.phoenix.budget.fragment.MenuFragment
import com.phoenix.budget.fragment.PopMenuItemType
import com.phoenix.budget.persenter.DashboardPresenter
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), DashboardCallback, MenuCallback {
    lateinit var binding:ActivityDashboardBinding
    lateinit var presenter: DashboardPresenter
    val  menuFragment = MenuFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        presenter = DashboardPresenter(this)
        binding.presenter = presenter
        supportFragmentManager.beginTransaction().replace(R.id.menu_container, menuFragment, MenuFragment.TAG).commit()
    }

    override fun showError(text: String) {
       Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onSelectMenuItem(menuItem: PopMenuItemType) = startTransaction(menuItem)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings ->
                return true
            else -> super.onOptionsItemSelected(item)
        }
    }

     fun startTransaction(menuItem: PopMenuItemType){
        val intent = Intent(this, TransactionActivity::class.java)
         intent.putExtra(TransactionActivity.MODE, menuItem.ordinal)
        startActivity(intent)
    }
}
