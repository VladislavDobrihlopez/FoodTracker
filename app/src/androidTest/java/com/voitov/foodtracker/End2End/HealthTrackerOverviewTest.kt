package com.voitov.foodtracker.End2End

import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.common.truth.Truth.assertThat
import com.voitov.common.domain.entities.Gender
import com.voitov.common.domain.entities.GoalType
import com.voitov.common.domain.entities.PhysicalActivityLevel
import com.voitov.common.domain.entities.UserProfile
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.common.domain.use_cases.FilterOutDigitsUseCase
import com.voitov.foodtracker.MainActivity
import com.voitov.foodtracker.navigation.AppNavState
import com.voitov.foodtracker.navigation.navigateTo
import com.voitov.foodtracker.repository.FakeTrackerRepository
import com.voitov.foodtracker.ui.theme.FoodTrackerTheme
import com.voitov.tracker_domain.model.MealType
import com.voitov.tracker_domain.model.TrackableFood
import com.voitov.tracker_domain.use_case.DeleteFoodUseCase
import com.voitov.tracker_domain.use_case.DoNutrientMathUseCase
import com.voitov.tracker_domain.use_case.InsertFoodUseCase
import com.voitov.tracker_domain.use_case.NutrientStuffUseCasesWrapper
import com.voitov.tracker_domain.use_case.RestoreFoodUseCase
import com.voitov.tracker_domain.use_case.RetrieveAllFoodOnDateUseCase
import com.voitov.tracker_domain.use_case.SearchFoodUseCase
import com.voitov.tracker_presentation.health_tracker_screen.HealthTrackerOverviewViewModel
import com.voitov.tracker_presentation.health_tracker_screen.HealthTrackerScreen
import com.voitov.tracker_presentation.searching_for_food_screen.SearchFoodViewModel
import com.voitov.tracker_presentation.searching_for_food_screen.SearchScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt
import kotlin.random.Random

@HiltAndroidTest
class HealthTrackerOverviewTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composableRule = createAndroidComposeRule<MainActivity>()

    private lateinit var keyValueStorage: UserInfoKeyValueStorage
    private lateinit var repository: FakeTrackerRepository
    private lateinit var useCasesWrapper: NutrientStuffUseCasesWrapper
    private lateinit var filterOutUseCase: FilterOutDigitsUseCase
    private lateinit var searchViewModel: SearchFoodViewModel
    private lateinit var overviewViewModel: HealthTrackerOverviewViewModel
    private lateinit var navHostController: NavHostController

    @Before
    fun setUp() {
        repository = FakeTrackerRepository()
        keyValueStorage = mockk<UserInfoKeyValueStorage>(relaxed = true)
        every { keyValueStorage.readAllUserInfo() } returns UserProfile(
            gender = listOf(Gender.Male, Gender.Female).random(),
            goalType = listOf(
                GoalType.GainWeight,
                GoalType.KeepWeight,
                GoalType.LoseWeight
            ).random(),
            physicalActivityLevel = listOf(
                PhysicalActivityLevel.High,
                PhysicalActivityLevel.Medium,
                PhysicalActivityLevel.Low,
            ).random(),
            age = 20,
            weight = Random.nextInt(50, 120).toFloat(),
            height = Random.nextInt(140, 210),
            fatRatio = 0.6f,
            proteinRatio = 0.3f,
            carbRatio = 0.1f
        )

        useCasesWrapper = NutrientStuffUseCasesWrapper(
            deleteFoodUseCase = DeleteFoodUseCase(repository = repository),
            doNutrientMathUseCase = DoNutrientMathUseCase(
                keyValueStorage = keyValueStorage
            ),
            insertFoodUseCase = InsertFoodUseCase(repository = repository),
            restoreFoodUseCase = RestoreFoodUseCase(repository = repository),
            retrieveAllFoodOnDateUseCase = RetrieveAllFoodOnDateUseCase(
                repository = repository
            ),
            searchFoodUseCase = SearchFoodUseCase(repository = repository)
        )
        filterOutUseCase = FilterOutDigitsUseCase()
        searchViewModel = SearchFoodViewModel(
            trackerUseCases = useCasesWrapper,
            filterOutDigitsUseCase = filterOutUseCase
        )
        overviewViewModel = HealthTrackerOverviewViewModel(
            useCase = useCasesWrapper,
            keyValueStorage = keyValueStorage
        )

        composableRule.activity.setContent {
            FoodTrackerTheme {
                navHostController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(scaffoldState = scaffoldState) { it ->
                    NavHost(
                        startDestination = AppNavState.TrackerOverview.route,
                        navController = navHostController
                    ) {
                        composable(route = AppNavState.TrackerOverview.route) {
                            HealthTrackerScreen(
                                scaffoldState = scaffoldState,
                                onNavigate = { mealType, year, month, day ->
                                    navHostController.navigateTo(
                                        AppNavState.Search.createRoute(
                                            mealType = mealType.name,
                                            year = year,
                                            month = month,
                                            dayOfWeek = day
                                        )
                                    )
                                },
                                viewModel = overviewViewModel
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
                            val year =
                                backStackEntry.arguments?.getInt(AppNavState.Search.YEAR_KEY)!!
                            val month =
                                backStackEntry.arguments?.getInt(AppNavState.Search.MONTH_KEY)!!
                            val day =
                                backStackEntry.arguments?.getInt(AppNavState.Search.DAY_OF_WEEK_KEY)!!
                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealType = MealType.valueOf(mealType),
                                day = day,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navHostController.navigateUp()
                                },
                                viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addToLunch_appearsUnder_nutrientsAreCorrectlyCalculated() {
        repository.apiSearchResults = listOf<TrackableFood>(
            TrackableFood(
                name = "Belarusian draniki",
                imageUrl = null,
                caloriesPer100g = 4100,
                carbsPer100g = 400,
                proteinPer100g = 400,
                fatProteinPer100g = 100
            )
        )

        val addedAmount = 150
        val expectedCalories = ((addedAmount / 100f) * 4100).roundToInt()
        val expectedCarbs = ((addedAmount / 100f) * 400).roundToInt()
        val expectedFat = ((addedAmount / 100f) * 100).roundToInt()
        val expectedProteins = ((addedAmount / 100f) * 400).roundToInt()

        composableRule.onNodeWithText("Add Lunch")
            .assertDoesNotExist()
        composableRule.onNodeWithText("Lunch")
            .performClick()

        composableRule.onNodeWithText("Add Lunch")
            .assertExists()
        composableRule.onNodeWithText("Add Lunch")
            .performClick()

        assertThat(
            navHostController.currentDestination
                ?.route?.startsWith("search/")
        ).isTrue()

        composableRule.onNodeWithTag("searchbar_textfield")
            .performTextInput("цыбуля")

        composableRule.onNodeWithContentDescription("Type here to search")
            .performClick()

        composableRule.onNodeWithTag("trackablefood_textfield")
            .assertDoesNotExist()

        composableRule.onNodeWithText("Carbs")
            .performClick()

        composableRule.onNodeWithTag("trackablefood_textfield")
            .performTextInput(addedAmount.toString())

        composableRule.onNodeWithContentDescription("Track")
            .performClick()

        assertThat(
            navHostController.currentDestination
                ?.route
                ?.startsWith(AppNavState.TrackerOverview.route)
        ).isTrue()

        composableRule.onAllNodesWithText(expectedCalories.toString())
            .onFirst()
            .assertIsDisplayed()

        composableRule.onAllNodesWithText(expectedCarbs.toString())
            .onFirst()
            .assertIsDisplayed()

        composableRule.onAllNodesWithText(expectedFat.toString())
            .onFirst()
            .assertIsDisplayed()

        composableRule.onAllNodesWithText(expectedProteins.toString())
            .onFirst()
            .assertIsDisplayed()
    }

    @After
    fun tearDown() {

    }
}