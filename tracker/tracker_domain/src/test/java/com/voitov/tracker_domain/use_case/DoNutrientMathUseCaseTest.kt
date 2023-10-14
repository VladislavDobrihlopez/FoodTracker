package com.voitov.tracker_domain.use_case

import com.google.common.truth.Truth.assertThat
import com.voitov.common.domain.entities.Gender
import com.voitov.common.domain.entities.GoalType
import com.voitov.common.domain.entities.PhysicalActivityLevel
import com.voitov.common.domain.entities.UserProfile
import com.voitov.common.domain.interfaces.UserInfoKeyValueStorage
import com.voitov.tracker_domain.model.MealTimeType
import com.voitov.tracker_domain.model.TrackedFood
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime
import kotlin.random.Random


class DoNutrientMathUseCaseTest {
    private lateinit var useCase: DoNutrientMathUseCase
    private lateinit var dummyDate: List<TrackedFood>

    @Before
    fun before() {
        val keyValueStorage = mockk<UserInfoKeyValueStorage>(relaxed = true)
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
        useCase = DoNutrientMathUseCase(keyValueStorage = keyValueStorage)
        dummyDate = buildList {
            repeat(1000) { index ->
                val carbs = Random.nextInt(100, 2000)
                val fat = Random.nextInt(100, 2000)
                val protein = Random.nextInt(100, 2000)
                val calories = fat * 9 + carbs * 4 + protein * 4
                add(
                    TrackedFood(
                        id = index,
                        name = "Joaquin Moore",
                        imageUrl = null,
                        calories = calories,
                        carbs = carbs,
                        proteins = protein,
                        fats = fat,
                        mealTimeType = listOf(
                            MealTimeType.SNACK,
                            MealTimeType.SUPPER,
                            MealTimeType.BRUNCH,
                            MealTimeType.BREAKFAST,
                            MealTimeType.LUNCH,
                            MealTimeType.DINNER,
                        ).random(),
                        amount = 100,
                        date = LocalDateTime.now(),
                    )
                )
            }
        }
    }

    @Test
    fun `Sum of calories after nutrient calculations mustn't change`() {
        val result = useCase(dummyDate)
        val mealTimeType = listOf(
            MealTimeType.SNACK,
            MealTimeType.SUPPER,
            MealTimeType.BRUNCH,
            MealTimeType.BREAKFAST,
            MealTimeType.LUNCH,
            MealTimeType.DINNER,
        ).random()

        val caloriesBefore = dummyDate
            .filter { it.mealTimeType == mealTimeType }
            .sumOf { it.calories }

        val caloriesAfter = result.mealTimeToNutrients.values
            .filter { it.mealTimeType == mealTimeType }
            .sumOf { it.calories }

        assertThat(caloriesBefore).isEqualTo(caloriesAfter)
    }

    @Test
    fun `Sum of carbs after nutrient calculations mustn't change`() {
        val result = useCase(dummyDate)
        val mealTimeType = listOf(
            MealTimeType.SNACK,
            MealTimeType.SUPPER,
            MealTimeType.BRUNCH,
            MealTimeType.BREAKFAST,
            MealTimeType.LUNCH,
            MealTimeType.DINNER,
        ).random()

        val carbsBefore = dummyDate
            .filter { it.mealTimeType == mealTimeType }
            .sumOf { it.carbs }

        val carbsAfter = result.mealTimeToNutrients.values
            .filter { it.mealTimeType == mealTimeType }
            .sumOf { it.carbs }

        assertThat(carbsBefore).isEqualTo(carbsAfter)
    }

    @Test
    fun `Sum of fat after nutrient calculations mustn't change`() {
        val result = useCase(dummyDate)
        val mealTimeType = listOf(
            MealTimeType.SNACK,
            MealTimeType.SUPPER,
            MealTimeType.BRUNCH,
            MealTimeType.BREAKFAST,
            MealTimeType.LUNCH,
            MealTimeType.DINNER,
        ).random()

        val fatBefore = dummyDate
            .filter { it.mealTimeType == mealTimeType }
            .sumOf { it.fats }

        val fatAfter = result.mealTimeToNutrients.values
            .filter { it.mealTimeType == mealTimeType }
            .sumOf { it.fat }

        assertThat(fatBefore).isEqualTo(fatAfter)
    }

    @Test
    fun `Sum of protein after nutrient calculations mustn't change`() {
        val result = useCase(dummyDate)
        val mealTimeType = listOf(
            MealTimeType.SNACK,
            MealTimeType.SUPPER,
            MealTimeType.BRUNCH,
            MealTimeType.BREAKFAST,
            MealTimeType.LUNCH,
            MealTimeType.DINNER,
        ).random()

        val proteinBefore = dummyDate
            .filter { it.mealTimeType == mealTimeType }
            .sumOf { it.proteins }

        val proteinAfter = result.mealTimeToNutrients.values
            .filter { it.mealTimeType == mealTimeType }
            .sumOf { it.proteins }

        assertThat(proteinBefore).isEqualTo(proteinAfter)
    }
}