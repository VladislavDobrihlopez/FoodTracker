package com.voitov.foodtracker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.voitov.onboarding_presentation.welcome.HelloScreen
import com.voitov.onboarding_presentation.welcome.gender_screen.GenderScreen

@Composable
fun AppNavGraph(navHostController: NavHostController = rememberNavController(), startDestination: String) {
    NavHost(startDestination = startDestination, navController = navHostController) {
        composable(route = AppNavState.Welcome.route) {
            HelloScreen {
                navHostController.navigateTo(AppNavState.Gender)
            }
        }
        composable(route = AppNavState.Gender.route) {
            GenderScreen {
                navHostController.navigateTo(AppNavState.Age)
            }
        }
        composable(route = AppNavState.Age.route) {

        }
        composable(route = AppNavState.Height.route) {

        }
        composable(route = AppNavState.Weight.route) {

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
}

//@Composable
//fun rememberNavState(navHostController: NavHostController = rememberNavController()) =
//    remember { AppNav(navHostController) }