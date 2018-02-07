package com.phoenix.budget

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.phoenix.budget.databinding.ActivityDashboardBinding
import com.phoenix.budget.fragment.MenuCallback
import com.phoenix.budget.fragment.MenuFragment
import com.phoenix.budget.fragment.PopMenuItemType
import com.phoenix.budget.model.Record
import com.phoenix.budget.presenter.DashboardPresenter
import com.phoenix.budget.view.RecordsAdapter
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), DashboardCallback, MenuCallback {
    lateinit var binding:ActivityDashboardBinding
    lateinit var presenter: DashboardPresenter
    val  menuFragment = MenuFragment()
    var iconArr: IntArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        iconArr = loadIcons(this)
        presenter = DashboardPresenter(this)
        binding.presenter = presenter
        supportFragmentManager.beginTransaction().replace(R.id.menu_container, menuFragment, MenuFragment.TAG).commit()
        binding.scrollview!!.recycleView.layoutManager = LinearLayoutManager(this)
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

    override fun updateRecords(list: List<Record>){
        binding.scrollview?.recycleView?.adapter = RecordsAdapter(this, presenter,  list)
    }

    override fun getIconId(catogoryId: Int): Int = iconArr!!.get(catogoryId)

    override fun showError(text: String) {
       Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onSelectMenuItem(menuItem: PopMenuItemType) = startRecord(menuItem)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings ->
                return true
            else -> super.onOptionsItemSelected(item)
        }
    }

     fun startRecord(menuItem: PopMenuItemType){
        val intent = Intent(this, RecordActivity::class.java)
         intent.putExtra(RecordActivity.MODE, menuItem.ordinal)
         intent.putExtra(RecordActivity.RECORD_ID, "")
         startActivity(intent)
    }
}
