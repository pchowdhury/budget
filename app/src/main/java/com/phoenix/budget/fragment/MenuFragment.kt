package com.phoenix.budget.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatTextView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout

import com.phoenix.budget.R
import com.phoenix.budget.databinding.FragmentMenuBinding
import com.phoenix.budget.utils.Fragments

/**
 * A fragment representing a list of Items.
 *
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 */
/**
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class MenuFragment : Fragment() {

    companion object {
        @JvmStatic
        val TAG: String = "MenuFragment"
    }

    private lateinit var binding: FragmentMenuBinding
    private var RADIUS: Int = 0
    private val ANIMATION_DELAY = 100L
    var isShowingMenu: Boolean = false

    private val menu: MutableList<PopMenuItem> = mutableListOf(
            PopMenuItem(PopMenuItemType.Expense),
            PopMenuItem(PopMenuItemType.FixedExpense),
            PopMenuItem(PopMenuItemType.Income),
            PopMenuItem(PopMenuItemType.FixedIncome)
    )

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.bind(View.inflate(context, R.layout.fragment_menu, null))
        val radius = context.resources.getDimension(R.dimen.menu_radius).toInt()
        val radiusIncr = context.resources.getDimension(R.dimen.menu_radius_incr).toInt()
        RADIUS = radius + (radiusIncr * menu.size)
        binding.container.postDelayed({ createMenus() }, 100)
        binding.fab.setOnClickListener({ showOrHideMenu() })
        return binding.root
    }

    private fun closeIfOpen() {
        if (isShowingMenu) {
            showOrHideMenu()
        }
    }

    private fun createMenus() {

        val centerX = (binding.container.width / 2).toFloat()
        val centerY = (binding.container.height).toFloat() - context.resources.getDimension(R.dimen.menu_offset).toInt()

        val variable = if (menu.size > 1) 30 else 90
        val eachAngle = ((180 - (variable * 2)) / if (menu.size > 1) (menu.size - 1) else menu.size)
        val startAngle = 180 + variable
        var longestText = ""
        for (item in menu) {
            if (item.menuId.label.length >= longestText.length) {
                longestText = item.menuId.label
            }
        }
        val width = context.resources.getDimension(R.dimen.menu_width).toInt()
        val height = context.resources.getDimension(R.dimen.menu_height).toInt()
        for (i in 0..(menu.size - 1)) {
            val item = menu[i]
            val textView = AppCompatTextView(context)
            textView.gravity = Gravity.CENTER
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.menu_font_size))
            ViewCompat.setBackground(textView, ContextCompat.getDrawable(context, R.drawable.menu_selector))
            textView.setTextColor(Color.WHITE)
            textView.setOnClickListener({ view ->
                onSelectMenu((view.tag as PopMenuItem).menuId)
            })
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(width, height)
            textView.text = item.menuId.label
            textView.tag = item
            binding.container.addView(textView, params)
            item.point.x = centerX - (width / 2)
            item.point.y = centerY - (height / 2)
            item.degrees = startAngle + ((eachAngle * i)).toFloat()
            item.toPoint.x = (RADIUS * Math.cos(Math.toRadians(item.degrees.toDouble()))).toFloat() + centerX - (width / 2)
            item.toPoint.y = (RADIUS * Math.sin(Math.toRadians(item.degrees.toDouble()))).toFloat() + centerY - (height / 2)

            textView.translationX = item.point.x
            textView.translationY = item.point.y
            textView.rotation = item.degrees + 90
        }
        binding.container.visibility = View.INVISIBLE
    }

    private fun onSelectMenu(menuId: PopMenuItemType) {
        val callback = Fragments.resolveListener(this, MenuCallback::class.java)
        callback?.onSelectMenuItem(menuId)
        showOrHideMenu()
    }


    private fun expand() {
        val objectAnimators = mutableListOf<ObjectAnimator>()
        val animationSet = AnimatorSet()
        animationSet.duration = ANIMATION_DELAY
        for (i in 0..(binding.container.childCount - 1)) {
            val text = binding.container.getChildAt(i)
            val item = text.tag as PopMenuItem
            objectAnimators.add(ObjectAnimator.ofFloat(text, "translationX", item.point.x, item.toPoint.x))
            objectAnimators.add(ObjectAnimator.ofFloat(text, "translationY", item.point.y, item.toPoint.y))
        }
        animationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                binding.container.visibility = View.VISIBLE
            }
        })
        animationSet.playTogether(objectAnimators.toList())
        animationSet.start()
    }

    private fun collapse() {
        val objectAnimators = mutableListOf<ObjectAnimator>()
        val animationSet = AnimatorSet()
        animationSet.duration = ANIMATION_DELAY

        for (i in 0..(binding.container.childCount - 1)) {
            val text = binding.container.getChildAt(i)
            val item = text.tag as PopMenuItem
            objectAnimators.add(ObjectAnimator.ofFloat(text, "translationX", item.toPoint.x, item.point.x))
            objectAnimators.add(ObjectAnimator.ofFloat(text, "translationY", item.toPoint.y, item.point.y))
        }
        animationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                binding.container.visibility = View.INVISIBLE
            }
        })
        animationSet.playTogether(objectAnimators.toList())
        animationSet.start()
    }

    fun cancelMenu() {
        binding.fab.rotation = 0f
        collapse()
    }

    fun showMenu() {
        binding.fab.rotation = 45f
        expand()
    }

    fun showOrHideMenu() {
        if (isShowingMenu) {
            cancelMenu()
        } else {
            showMenu()
        }
        isShowingMenu = !isShowingMenu
    }

    class PopMenuItem(menuId: PopMenuItemType) {
        var point: PointF = PointF()
        var toPoint: PointF = PointF()
        var degrees: Float = 0f
        var menuId: PopMenuItemType = menuId
    }
}
