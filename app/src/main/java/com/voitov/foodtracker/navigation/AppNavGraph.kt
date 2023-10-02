package com.voitov.foodtracker.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.voitov.common.nav.TrackableFoodManagerSection
import com.voitov.onboarding_presentation.activity_level_screen.ActivityLevelScreen
import com.voitov.onboarding_presentation.age_screen.AgeScreen
import com.voitov.onboarding_presentation.gender_screen.GenderScreen
import com.voitov.onboarding_presentation.goal_screen.GoalScreen
import com.voitov.onboarding_presentation.height_screen.HeightScreen
import com.voitov.onboarding_presentation.nutrient_plan_screen.NutrientPlanScreen
import com.voitov.onboarding_presentation.weight_screen.WeightScreen
import com.voitov.onboarding_presentation.welcome.HelloScreen
import com.voitov.tracker_domain.model.MealTimeType
import com.voitov.tracker_presentation.custom_food_screen.CustomFoodScreen
import com.voitov.tracker_presentation.health_tracker_screen.HealthTrackerScreen
import com.voitov.tracker_presentation.searching_for_food_screen.SearchScreen
import com.voitov.tracker_presentation.trackable_food_manager_screen.TrackableFoodManagerScreen

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppNavGraph(
    onScreenIsReady: (Boolean) -> Unit,
    startDestination: String
) {
    val scaffoldState = rememberScaffoldState()
    val navHostController = rememberNavController()

    Scaffold(scaffoldState = scaffoldState, modifier = Modifier.fillMaxSize()) {
        NavHost(
            startDestination = startDestination,
            navController = navHostController
        ) {
            composable(route = AppNavState.Welcome.route) {
                HelloScreen {
                    navHostController.navigateTo(AppNavState.Gender) {
                        launchSingleTop = true
                    }
                }
                LaunchedEffect(key1 = Unit) {
                    onScreenIsReady(true)
                }
            }
            composable(route = AppNavState.Gender.route) {
                GenderScreen(onNavigate = {
                    navHostController.navigateTo(AppNavState.Age) {
                        launchSingleTop = true
                    }
                })
            }
            composable(route = AppNavState.Age.route) {
                AgeScreen(
                    snackBarState = scaffoldState.snackbarHostState,
                    onNavigate = {
                        navHostController.navigateTo(AppNavState.Height) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = AppNavState.Height.route) {
                HeightScreen(
                    snackBarState = scaffoldState.snackbarHostState,
                    onNavigate = {
                        navHostController.navigateTo(AppNavState.Weight) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = AppNavState.Weight.route) {
                WeightScreen(
                    snackBarState = scaffoldState.snackbarHostState,
                    onNavigate = {
                        navHostController.navigateTo(AppNavState.Activity) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = AppNavState.Activity.route) {
                ActivityLevelScreen(onNavigate = {
                    navHostController.navigateTo(AppNavState.Goal) {
                        launchSingleTop = true
                    }
                })
            }

            composable(route = AppNavState.Goal.route) {
                GoalScreen(onNavigate = {
                    navHostController.navigateTo(AppNavState.NutrientGoal) {
                        launchSingleTop = true
                    }
                })
            }
            composable(route = AppNavState.NutrientGoal.route) {
                NutrientPlanScreen(
                    snackBarState = scaffoldState.snackbarHostState,
                    onNavigate = {
                        navHostController.navigate(AppNavState.TrackerOverview.route) {
                            popUpTo(AppNavState.Welcome.route) {
                                inclusive = true
                            }
                        }
                    })
            }
            composable(route = AppNavState.TrackerOverview.route) {
                HealthTrackerScreen(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    onNavigate = { mealType, year, month, day ->
                        navHostController.navigateTo(
                            AppNavState.TrackableFoodManager.createRoute(
                                mealType.name,
                                year,
                                month,
                                day
                            )
                        )
                    },
                    onDoReonboarding = {
                        navHostController.navigateTo(AppNavState.Welcome.route) {
                            popUpTo(AppNavState.TrackerOverview.route) {
                                inclusive = true
                            }
                        }
                    }
                )
                LaunchedEffect(key1 = Unit) {
                    onScreenIsReady(true)
                }
            }

            composable(
                route = AppNavState.TRACKABLE_FOOD_MANAGER_ROUTE,
                arguments = listOf(
                    navArgument(AppNavState.TrackableFoodManager.MEAL_TYPE_KEY) {
                        type = NavType.StringType
                    },
                    navArgument(AppNavState.TrackableFoodManager.YEAR_KEY) {
                        type = NavType.IntType
                    },
                    navArgument(AppNavState.TrackableFoodManager.MONTH_KEY) {
                        type = NavType.IntType
                    },
                    navArgument(AppNavState.TrackableFoodManager.DAY_OF_WEEK_KEY) {
                        type = NavType.IntType
                    })
            ) { backStackEntry ->
                val mealType =
                    backStackEntry.arguments?.getString(AppNavState.Search.MEAL_TYPE_KEY)!!
                val year = backStackEntry.arguments?.getInt(AppNavState.Search.YEAR_KEY)!!
                val month = backStackEntry.arguments?.getInt(AppNavState.Search.MONTH_KEY)!!
                val day = backStackEntry.arguments?.getInt(AppNavState.Search.DAY_OF_WEEK_KEY)!!

                TrackableFoodManagerScreen(onNavigate = { section ->
                    when (section) {
                        TrackableFoodManagerSection.ADDING_CUSTOM_FOOD_SECTION -> {
                            navHostController.navigateTo(
                                AppNavState.CustomFoodAdder.route
                            )
                        }

                        TrackableFoodManagerSection.SEARCHING_FROM_EXTERNAL_OR_INTERNAL_FOOD_SECTION -> {
                            navHostController.navigateTo(
                                AppNavState.Search.createRoute(
                                    mealType,
                                    year,
                                    month,
                                    day
                                )
                            )
                        }
                    }
                })
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
                    mealTimeType = MealTimeType.valueOf(mealType),
                    day = day,
                    month = month,
                    year = year,
                    onNavigateUp = {
                        navHostController.navigateUp()
                    }
                )
            }

            composable(route = AppNavState.CustomFoodAdder.route) {
                CustomFoodScreen(
                    snackBarState = scaffoldState.snackbarHostState,
                    onNavigateUp = {
                        navHostController.popBackStack()
                    }
                )
            }
        }
    }
}
