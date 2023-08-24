package com.voitov.foodtracker.navigation

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.voitov.common.navigation.AppNavState
import com.voitov.onboarding_presentation.activity_level_screen.ActivityLevelScreen
import com.voitov.onboarding_presentation.age_screen.AgeScreen
import com.voitov.onboarding_presentation.gender_screen.GenderScreen
import com.voitov.onboarding_presentation.goal_screen.GoalScreen
import com.voitov.onboarding_presentation.height_screen.HeightScreen
import com.voitov.onboarding_presentation.nutrient_plan_screen.NutrientPlanScreen
import com.voitov.onboarding_presentation.weight_screen.WeightScreen
import com.voitov.onboarding_presentation.welcome.HelloScreen
import com.voitov.tracker_domain.model.MealType
import com.voitov.tracker_presentation.health_tracker_screen.HealthTrackerScreen
import com.voitov.tracker_presentation.searching_for_food_screen.SearchScreen

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
                    navHostController.navigateTo(AppNavState.AGE_ROUTE)
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
            composable(route = AppNavState.Activity.route) {
                ActivityLevelScreen(onNavigate = {
                    navHostController.navigateTo(AppNavState.GOAL_ROUTE)
                })
            }

            composable(route = AppNavState.Goal.route) {
                GoalScreen(onNavigate = {
                    navHostController.navigateTo(AppNavState.NutrientGoal)
                })
            }
            composable(route = AppNavState.NutrientGoal.route) {
                NutrientPlanScreen(snackBarState = scaffoldState.snackbarHostState, onNavigate = {
                    navHostController.navigateTo(AppNavState.TrackerOverview)
                })
            }
            composable(route = AppNavState.TrackerOverview.route) {
//                val viewModel: HealthTrackerOverviewViewModel = hiltViewModel()
//                val state = viewModel.screenState.copy(
//                    caloriesPerDayGoal = 3000,
//                    caloriesPerDayInFact = 1000,
//                    carbsPerDayGoal = 100,
//                    carbsPerDayInFact = 50,
//                    fatPerDayGoal = 50,
//                    fatPerDayInFact = 30,
//                    proteinsPerDayGoal = 20,
//                    proteinsPerDayInFact = 19,
//                )
                HealthTrackerScreen(
                    scaffoldState = scaffoldState,
                    onNavigateTo = {
                        navHostController.navigateTo(it)
                    }
                )
            }

            composable(
                route = AppNavState.SEARCH_ROUTE,
                arguments = listOf(
                    navArgument(AppNavState.Search.MEAL_TYPE_KEY) {
                        type = NavType.StringType
                    },
                    navArgument(AppNavState.Search.YEAR_KEY) {
                        type = NavType.IntType
                    },
                    navArgument(AppNavState.Search.MONTH_KEY) {
                        type = NavType.IntType
                    },
                    navArgument(AppNavState.Search.DAY_OF_WEEK_KEY) {
                        type = NavType.IntType
                    })
            ) { backStackEntry ->
                val mealType =
                    backStackEntry.arguments?.getString(AppNavState.Search.MEAL_TYPE_KEY)!!
                val year = backStackEntry.arguments?.getInt(AppNavState.Search.YEAR_KEY)!!
                val month = backStackEntry.arguments?.getInt(AppNavState.Search.MONTH_KEY)!!
                val day = backStackEntry.arguments?.getInt(AppNavState.Search.DAY_OF_WEEK_KEY)!!
                SearchScreen(
                    scaffoldState = scaffoldState,
                    mealType = MealType.valueOf(mealType),
                    day = day,
                    month = month,
                    year = year,
                    onNavigateUp = {
                        navHostController.navigateUp()
                    }
                )
            }
        }
    })
}
