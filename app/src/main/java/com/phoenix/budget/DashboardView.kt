package com.phoenix.budget

/**
 * Created by Pushpan on 26/01/18.
 */
interface DashboardView {
    fun cancelMenu()
    fun showMenu()
    fun showError(text: String)
}