package com.phoenix.budget.dialog

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phoenix.budget.R
import com.phoenix.budget.databinding.LayoutDialogBinding
import com.phoenix.budget.utils.Fragments
import kotlinx.android.synthetic.main.layout_dialog.*

/**
 * Created by Pushpan on 18/02/18.
 */

class ConfirmationDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       val view = View.inflate(context, R.layout.layout_dialog, null)
        var binding = DataBindingUtil.bind<LayoutDialogBinding>(view)

        binding.txtTitle.text = arguments.getString(TITLE)
        binding.txtMessage.text = arguments.getString(MESSAGE)
        binding.btnPositive.setOnClickListener({
            val callback = Fragments.resolveListener(this, ConfirmationCallback::class.java)
            callback?.onPositiveResponse(arguments.getInt(TYPE, 0))
            dismiss()
        })
        binding.btnNegative.setOnClickListener({
            val callback = Fragments.resolveListener(this, ConfirmationCallback::class.java)
            callback?.onNegativeResponse(arguments.getInt(TYPE, 0))
            dismiss()
        })
        binding.btnCancel.setOnClickListener({
            val callback = Fragments.resolveListener(this, ConfirmationCallback::class.java)
            callback?.onRespondingCancel(arguments.getInt(TYPE, 0))
            dismiss()
        })
        return view
    }

    companion object {
        @JvmStatic
        val TAG = "ConfirmationDialogFragment"
        @JvmStatic
        val TYPE = "Type"
        @JvmStatic
        val TITLE = "Title"
        @JvmStatic
        val MESSAGE = "Message"

        @JvmStatic
        fun create(type: Int, title: String, message: String): ConfirmationDialogFragment {
            val instance = ConfirmationDialogFragment()
            val bundle = Bundle()
            bundle.putInt(TYPE, type)
            bundle.putString(TITLE, title)
            bundle.putString(MESSAGE, message)
            instance.arguments = bundle
            return instance
        }
    }

    interface ConfirmationCallback {
        fun onPositiveResponse(type: Int)
        fun onNegativeResponse(type: Int)
        fun onRespondingCancel(type: Int)
    }
}
