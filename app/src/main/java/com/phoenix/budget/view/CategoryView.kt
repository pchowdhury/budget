package com.phoenix.budget.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.phoenix.budget.R
import com.phoenix.budget.model.Category
import com.phoenix.budget.persistence.BudgetApp
import kotlinx.android.synthetic.main.category_view.view.*
import java.sql.Date


/**
 * TODO: document your custom view class.
 */
class CategoryView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        val view: View = View.inflate(context, R.layout.category_view, this)
        val recycleView = view.findViewById<RecyclerView>(R.id.recycleView)
        recycleView.layoutManager = GridLayoutManager(context, 5)

        recycleView.setHasFixedSize(true)
        loadCategories()
    }

    val RESOURCE: MutableList<Int> = mutableListOf(
            R.drawable.add_items,
            R.drawable.check_ok,
            R.drawable.dial_pad,
            R.drawable.dollar,
            R.drawable.new_entry,
            R.drawable.note,
            R.drawable.right_cheveron,
            R.drawable.time
    )

    fun loadCategories() {
        val handler = Handler()
        Thread({
//            BudgetApp.database.categoryDao().deleteAllCategories()
            var list = BudgetApp.database.categoryDao().getAllCategory()
            if (list.isEmpty()) {
                addCategories()
                 list = BudgetApp.database.categoryDao().getAllCategory()
            }
            handler.post { recycleView.adapter = CategoryAdapter(list) }
        }).start()

    }

    private fun addCategories() {
        // FIXME category with 0 id doesn't get inserted for some reason
        var categories = context.resources.getStringArray(R.array.category_items)
        for (i in 0..(categories.size - 1)) {
            val item = Category(i, categories[i], Date(System.currentTimeMillis()), Date(System.currentTimeMillis()))
            BudgetApp.database.categoryDao().insertTask(item)
        }
    }


    inner abstract class CategoryBaseAdapter : RecyclerView.Adapter<CategoryBaseAdapter.CategoryViewHolder>() {
        inner class CategoryViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
            // each data item is just a string in this case
            val imgView = binding.root.findViewById<ImageView>(R.id.imgView)

            fun bind(category: Category, position: Int) {
                imgView.setColorFilter(Color.WHITE)
                imgView.setImageResource(if (category.categoryId<RESOURCE.size) RESOURCE[category.categoryId] else RESOURCE[RESOURCE.size - 1])
                imgView.isSelected = position == 1
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            // create a new view
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, getLayoutIdForType(viewType), parent, false)
            // set the view's size, margins, paddings and layout parameters
            return CategoryViewHolder(binding)
        }

        // Replace the contents of a view (invoked by the layout manager)
        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            holder.bind(getDataAtPosition(position), position)
        }


        abstract fun getDataAtPosition(position: Int): Category

        abstract fun getLayoutIdForType(viewType: Int): Int

    }


    inner class CategoryAdapter(val category: List<Category>) : CategoryBaseAdapter() {
        override fun getDataAtPosition(position: Int): Category {
            return category[position]
        }

        override fun getLayoutIdForType(viewType: Int): Int {
            return R.layout.category_item
        }

        override fun getItemCount(): Int {
            return category.size
        }
    }
}
