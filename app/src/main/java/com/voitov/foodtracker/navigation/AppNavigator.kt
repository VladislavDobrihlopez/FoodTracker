package com.voitov.foodtracker.navigation

import androidx.navigation.NavController
import com.voitov.common.navigation.AppNavState

fun NavController.navigateTo(destinationRouteState: AppNavState) {
    this.navigate(destinationRouteState.route)
}

fun NavController.navigateTo(destinationRoute: String) {
    this.navigate(destinationRoute)
}

fun NavController.navigateUp() {
    this.popBackStack()
}