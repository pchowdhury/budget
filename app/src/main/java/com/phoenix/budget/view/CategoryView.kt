package com.phoenix.budget.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.phoenix.budget.BuildConfig
import com.phoenix.budget.R
import com.phoenix.budget.model.CategorizedRecord
import com.phoenix.budget.model.Category
import com.phoenix.budget.persistence.BudgetApp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.category_view.view.*


/**
 * TODO: document your custom view class.
 */
class CategoryView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    val compositeDisposable = CompositeDisposable()

    init {
        View.inflate(context, R.layout.category_view, this)
        recycleView.layoutManager = GridLayoutManager(context, 5)
        loadIcons()
        loadCategories()
    }

    val iconArr = loadIcons()
    private fun loadIcons(): IntArray {
        val ar = context.resources.obtainTypedArray(R.array.category_icons)
        val len = ar.length()
        val resIds = IntArray(len)
        for (i in 0 until len)
            resIds[i] = ar.getResourceId(i, 0)
        ar.recycle()
        return resIds
    }

    private fun loadCategories() {
        compositeDisposable.add(getAllCategoryDisposable())
    }

    private fun getAllCategoryDisposable() = BudgetApp.database.categoryDao().getAllCategory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ list -> showData(list) }, { error -> showError(error) })

    private fun showError(error: Throwable) {
        txtError.visibility = View.VISIBLE
        if (BuildConfig.DEBUG) {
            Log.e(javaClass.name, error.localizedMessage)
        }
    }

    private fun showData(list: List<Category>) {
        recycleView.adapter = CategoryAdapter(list)
    }

    private var selectedCategorizedRecord: CategorizedRecord? = null

    fun setCategorizedReport(categorizedRecord: CategorizedRecord){
        this.selectedCategorizedRecord =  categorizedRecord
    }

    override fun onDetachedFromWindow() {
        doCleanup()
        super.onDetachedFromWindow()
    }

    private fun doCleanup() {
        compositeDisposable.clear()
    }

    inner class CategoryAdapter(val category: List<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
        inner class CategoryViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
            // each data item is just a string in this case
            val imgView = binding.root.findViewById<ImageView>(R.id.imgView)

            fun bind(category: Category, position: Int) {
                imgView.setColorFilter(Color.WHITE)
                imgView.setOnClickListener(
                        {
                            val deselecting = selectedCategorizedRecord?.categoryId == position
                            selectedCategorizedRecord?.categoryId = if (deselecting) -1 else position
                            notifyDataSetChanged()
                        })
                imgView.setImageResource(iconArr[iconArr.size - 1])

                imgView.setImageResource(if ((category.id < iconArr.size) && category.id != -1) iconArr[(category.id)] else iconArr[iconArr.size - 1])
                imgView.isSelected = position == selectedCategorizedRecord?.categoryId
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            // create a new view
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.category_item, parent, false)
            // set the view's size, margins, paddings and layout parameters
            return CategoryViewHolder(binding)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.bind(getDataAtPosition(position), position)
        }

         private fun getDataAtPosition(position: Int): Category {
            return category[position]
        }

         override fun getItemCount(): Int {
            return category.size
        }
    }
}
