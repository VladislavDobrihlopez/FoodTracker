package com.voitov.foodtracker.navigation

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.voitov.onboarding_presentation.welcome.HelloScreen
import com.voitov.onboarding_presentation.welcome.age_screen.AgeScreen
import com.voitov.onboarding_presentation.welcome.gender_screen.GenderScreen
import com.voitov.onboarding_presentation.welcome.height_screen.HeightScreen
import com.voitov.onboarding_presentation.welcome.weight_screen.WeightScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppNavGraph(
    navHostController: NavHostController = rememberNavController(),
    startDestination: String
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(scaffoldState = scaffoldState, content = {
        NavHost(startDestination = startDestination, navController = navHostController) {
            composable(route = AppNavState.Welcome.route) {
                HelloScreen {
                    navHostController.navigateTo(AppNavState.Gender)
                }
            }
            composable(route = AppNavState.Gender.route) {
                GenderScreen(onNavigate = {
                    navHostController.navigateTo(AppNavState.Age)
                })
            }
            composable(route = AppNavState.Age.route) {
                AgeScreen(
                    snackBarState = scaffoldState.snackbarHostState,
                    onNavigate = {
                        navHostController.navigateTo(AppNavState.HEIGHT_ROUTE)
                    }
                )
            }
            composable(route = AppNavState.Height.route) {
                HeightScreen(
                    snackBarState = scaffoldState.snackbarHostState,
                    onNavigate = {
                        navHostController.navigateTo(AppNavState.WEIGHT_ROUTE)
                    }
                )
            }
            composable(route = AppNavState.Weight.route) {
                WeightScreen(
                    snackBarState = scaffoldState.snackbarHostState,
                    onNavigate = {
                        navHostController.navigateTo(AppNavState.ACTIVITY_ROUTE)
                    }
                )
            }
            composable(route = AppNavState.NutrientGoal.route) {

            }
            composable(route = AppNavState.Goal.route) {

            }
            composable(route = AppNavState.TrackerOverview.route) {

            }
            composable(route = AppNavState.Activity.route) {

            }
            composable(route = AppNavState.Search.route) {

            }
        }
    })
}
