package com.phoenix.budget.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.FrameLayout
import android.widget.RelativeLayout

import com.phoenix.budget.R
import com.phoenix.budget.fragment.dummy.DummyContent
import com.phoenix.budget.fragment.dummy.DummyContent.DummyItem

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

    companion object{
        @JvmStatic
         val TAG: String = "Entity.byName"
    }
    private var  RADIUS: Int = 0
    private val ANIMATION_DELAY = 100L
    private lateinit var menuContainer : RelativeLayout

    private val menu: MutableList<PopMenuItem> = mutableListOf(
            PopMenuItem(0, "Expense"),
            PopMenuItem(1, "Fixed\nExpense"),
            PopMenuItem(2, "Income"),
            PopMenuItem(3, "Fixed\nIncome")
    )

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        menuContainer = RelativeLayout(context)
        val radius= context.resources.getDimension(R.dimen.menu_radius).toInt()
        val radiusIncr= context.resources.getDimension(R.dimen.menu_radius_incr).toInt()
        RADIUS = radius + (radiusIncr * menu.size)
        menuContainer.postDelayed( { createMenus() }, 100)
        return menuContainer
    }

    private fun createMenus() {

        val centerX = (menuContainer.width/2).toFloat()
        val centerY = (menuContainer.height).toFloat() - context.resources.getDimension(R.dimen.menu_offset).toInt()

        val variable = if (menu.size > 1) 30 else 90
        val eachAngle = ((180 - (variable * 2)) / if (menu.size > 1) (menu.size - 1) else menu.size)
        val startAngle = 180 + variable
        var longestText = ""
        for (item in menu) {
            if (item.menuText.length >= longestText.length) {
                longestText = item.menuText
            }
        }
        val width = context.resources.getDimension(R.dimen.menu_width).toInt()
        val height= context.resources.getDimension(R.dimen.menu_height).toInt()
        for (i in 0..(menu.size-1)) {
            val item = menu[i]
            val textView = AppCompatTextView(context)
            textView.gravity = Gravity.CENTER
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.menu_font_size))
            ViewCompat.setBackground(textView, ContextCompat.getDrawable(context, R.drawable.border_bg))
            textView.setTextColor(Color.WHITE)
            val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(width, height)
            textView.text = item.menuText
            textView.tag = item
            menuContainer.addView(textView, params)
            item.point.x = centerX - (width / 2)
            item.point.y = centerY - (height / 2)
            item.degrees = startAngle +  ((eachAngle * i)).toFloat()
            item.toPoint.x = (RADIUS * Math.cos(Math.toRadians(item.degrees.toDouble()))).toFloat() + centerX- (width / 2)
            item.toPoint.y = (RADIUS * Math.sin(Math.toRadians(item.degrees.toDouble()))).toFloat() + centerY- (height / 2)

            textView.translationX = item.point.x
            textView.translationY = item.point.y
            textView.rotation = item.degrees + 90
        }
        menuContainer.visibility = View.INVISIBLE
    }


    fun expand(){
        val objectAnimators = mutableListOf<ObjectAnimator>()
        val animationSet = AnimatorSet()
        animationSet.duration = ANIMATION_DELAY
         for(i in 0..(menuContainer.childCount-1)){
             val text = menuContainer.getChildAt(i)
             val item = text.tag as PopMenuItem
             objectAnimators.add( ObjectAnimator.ofFloat(text, "translationX", item.point.x, item.toPoint.x))
             objectAnimators.add( ObjectAnimator.ofFloat(text, "translationY", item.point.y, item.toPoint.y))
        }
        animationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                menuContainer.visibility = View.VISIBLE
            }
        })
        animationSet.playTogether(objectAnimators.toList())
        animationSet.start()
    }

    fun collapse() {
        val objectAnimators = mutableListOf<ObjectAnimator>()
        val animationSet = AnimatorSet()
        animationSet.duration=ANIMATION_DELAY

        for(i in 0..(menuContainer.childCount-1)){
            val text = menuContainer.getChildAt(i)
            val item = text.tag as PopMenuItem
            objectAnimators.add( ObjectAnimator.ofFloat(text, "translationX", item.toPoint.x, item.point.x))
            objectAnimators.add( ObjectAnimator.ofFloat(text, "translationY", item.toPoint.y, item.point.y))
        }
        animationSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                menuContainer.visibility = View.INVISIBLE
            }
        })
        animationSet.playTogether(objectAnimators.toList())
        animationSet.start()
    }



//    override fun onAttach(context: Context?) {
//        super.onAttach(context)
//        if (context is OnListFragmentInteractionListener) {
//            mListener = context
//        } else {
//            throw RuntimeException(context!!.toString() + " must implement OnListFragmentInteractionListener")
//        }
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        mListener = null
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: DummyItem)
    }

//    companion object {
//
//        // TODO: Customize parameter argument names
//        private val ARG_COLUMN_COUNT = "column-count"
//
//        // TODO: Customize parameter initialization
//        fun newInstance(columnCount: Int): MenuFragment {
//            val fragment = MenuFragment()
//            val args = Bundle()
//            args.putInt(ARG_COLUMN_COUNT, columnCount)
//            fragment.arguments = args
//            return fragment
//        }
//    }

    class PopMenuItem(menuId: Int, menuText: String) {
        var point: PointF = PointF()
        var toPoint: PointF = PointF()
        var degrees: Float = 0f
        var menuId: Int = menuId
        var menuText: String = menuText
    }
}
