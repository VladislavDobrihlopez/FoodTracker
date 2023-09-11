package com.voitov.foodtracker.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

fun NavController.navigateTo(
    destinationRouteState: AppNavState,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    this.navigate(destinationRouteState.route, builder = builder)
}

fun NavController.navigateTo(
    destinationRoute: String,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    this.navigate(destinationRoute, builder = builder)
}

fun NavController.navigateUp() {
    this.popBackStack()
}