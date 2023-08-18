package com.voitov.foodtracker.navigation

import androidx.navigation.NavController

fun NavController.navigateTo(destinationRouteState: AppNavState) {
    this.navigate(destinationRouteState.route)
}

fun NavController.navigateTo(destinationRoute: String) {
    this.navigate(destinationRoute)
}