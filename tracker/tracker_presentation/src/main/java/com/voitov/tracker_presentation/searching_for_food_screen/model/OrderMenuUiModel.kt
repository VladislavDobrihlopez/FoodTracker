package com.voitov.tracker_presentation.searching_for_food_screen.model

import android.os.Parcelable
import com.voitov.tracker_domain.model.MealPhysicsType
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderMenuUiModel(
    val amount: String = "",
    val littleSteps: Int = 0,
    val middleSteps: Int = 0,
    val littleGlasses: Int = 0,
    val middleGlasses: Int = 0,
    val largeGlasses: Int = 0,
) : Parcelable {
    val liquids
        get() = listOf(
            Pair(MealPhysicsType.Liquid.CupOfSizeS, littleGlasses),
            Pair(MealPhysicsType.Liquid.CupOfSizeM, middleGlasses),
            Pair(MealPhysicsType.Liquid.CupOfSizeL, largeGlasses)
        )

    val solids
        get() =
            listOf(
                Pair(MealPhysicsType.Solid.Little, littleSteps),
                Pair(MealPhysicsType.Solid.Middle, middleSteps)
            )

    companion object {
        private var instance = OrderMenuUiModel()
        fun reset(toStartFrom: OrderMenuUiModel = OrderMenuUiModel()): Companion {
            instance = toStartFrom
            return this
        }

        fun setupTookCount(
            mealPhysicsType: MealPhysicsType,
            shouldBeAdded: Boolean
        ): Companion {
            val summand = getOneWithSign(shouldBeAdded)
            instance = when (mealPhysicsType) {
                MealPhysicsType.Liquid.CupOfSizeL -> {
                    instance.copy(largeGlasses = (instance.largeGlasses + summand).coerceAtLeast(0))
                }

                MealPhysicsType.Liquid.CupOfSizeM -> {
                    instance.copy(middleGlasses = (instance.middleGlasses + summand).coerceAtLeast(0))
                }

                MealPhysicsType.Liquid.CupOfSizeS -> {
                    instance.copy(littleGlasses = (instance.littleGlasses + summand).coerceAtLeast(0))
                }

                MealPhysicsType.Solid.Little -> {
                    instance.copy(littleSteps = (instance.littleSteps + summand).coerceAtLeast(0))
                }

                MealPhysicsType.Solid.Middle -> {
                    instance.copy(middleSteps = (instance.middleSteps + summand).coerceAtLeast(0))
                }
            }
            return this
        }

        fun setupAmount(): Companion {
            instance = instance.copy(amount = with(instance) {
                (littleGlasses * MealPhysicsType.Liquid.SIZE_S_IN_GRAMS +
                        middleGlasses * MealPhysicsType.Liquid.SIZE_M_IN_GRAMS +
                        largeGlasses * MealPhysicsType.Liquid.SIZE_L_IN_GRAMS) +
                        (littleSteps * MealPhysicsType.Solid.LITTLE_PACK_IN_GRAMS +
                                middleSteps * MealPhysicsType.Solid.MIDDLE_PACK_IN_GRAMS)
            }.toString())
            return this
        }

        fun build(): OrderMenuUiModel {
            return instance
        }

        private fun getOneWithSign(isPlus: Boolean): Int {
            return if (isPlus) 1 else -1
        }
    }
}
