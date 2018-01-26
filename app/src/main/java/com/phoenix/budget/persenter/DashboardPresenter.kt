package com.phoenix.budget.persenter

import com.phoenix.budget.DashboardView

/**
 * Created by Pushpan on 26/01/18.
 */
class DashboardPresenter(thisDashboard: DashboardView) {
    var dashboard: DashboardView = thisDashboard
    var isShowingMenu: Boolean = false

    fun showOrHideMenu() {
        if (isShowingMenu) {
            dashboard.cancelMenu()
        } else {
            dashboard.showMenu()
        }
        isShowingMenu = !isShowingMenu
    }



}