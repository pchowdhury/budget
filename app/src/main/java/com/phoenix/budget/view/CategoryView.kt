package com.phoenix.budget.view

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Rect
import android.os.Handler
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
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
import android.support.annotation.DimenRes
import android.support.annotation.NonNull


/**
 * TODO: document your custom view class.
 */
class CategoryView @kotlin.jvm.JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        val view: View = View.inflate(context, R.layout.category_view, this)
        val recycleView = view.findViewById<RecyclerView>(R.id.recycleView)
        recycleView.layoutManager = GridLayoutManager(context, 6)

        val itemDecoration = ItemOffsetDecoration(context, R.dimen.category_item_margin)
        recycleView.addItemDecoration(itemDecoration)
        recycleView.setHasFixedSize(true)
        loadCategories()
    }

    fun loadCategories() {
        val handler = Handler()
        Thread({
            val list = BudgetApp.database.categoryDao().getAllCategory()
            if(list.isEmpty()){
                addCategories()
            }
            handler.post { recycleView.adapter = CategoryAdapter(list) }
        }).start()

    }

    private fun addCategories() {
        for(i in 0..7){
            val item = Category(i, i.toString(), Date(System.currentTimeMillis()), Date(System.currentTimeMillis()))
            BudgetApp.database.categoryDao().insertTask(item)
        }
    }


    abstract class CategoryBaseAdapter : RecyclerView.Adapter<CategoryBaseAdapter.CategoryViewHolder>() {
        class CategoryViewHolder( binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
            // each data item is just a string in this case
            val imgView = binding.root.findViewById<ImageView>(R.id.imgView)
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

            fun bind(category: Category) {
                imgView.setImageResource(RESOURCE[category.categoryId])
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
            holder.bind(getDataAtPosition(position))
        }


        abstract fun getDataAtPosition(position: Int): Category

        abstract fun getLayoutIdForType(viewType: Int): Int

    }


    class CategoryAdapter(val category: List<Category>) : CategoryBaseAdapter() {
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

     class ItemOffsetDecoration(private val mItemOffset: Int) : RecyclerView.ItemDecoration() {

        constructor(@NonNull context: Context, @DimenRes itemOffsetId: Int) : this(context.resources.getDimensionPixelSize(itemOffsetId)) {}

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                           state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset)
        }
    }


}
