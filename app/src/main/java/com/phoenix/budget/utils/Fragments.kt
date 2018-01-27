package com.phoenix.budget.utils

import android.support.v4.app.Fragment

/**
 * Created by Pushpan on 27/01/18.
 */

object Fragments {

    /**
     * Resolves a listener implicitly attached to this fragment by either being set as the target fragment,
     * parent fragment or the host activity of the given fragment. The order of precedence in this lookup
     * is target fragment, followed by parent fragment, followed by activity.
     */
    // We are actually checking these casts
    fun <T> resolveListener(fragment: Fragment, listenerType: Class<T>): T? {
        return if (listenerType.isInstance(fragment.targetFragment)) {
            fragment.targetFragment as T
        } else if (listenerType.isInstance(fragment.parentFragment)) {
            fragment.parentFragment as T
        } else if (listenerType.isInstance(fragment.activity)) {
            fragment.activity as T
        } else {
            null
        }
    }
}
