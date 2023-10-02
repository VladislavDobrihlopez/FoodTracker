package com.voitov.tracker_domain.model

sealed class MealPhysicsType(val inGrams: Int) {
    sealed class Solid(inGrams: Int) : MealPhysicsType(inGrams) {
        object Little : Solid(LITTLE_PACK_IN_GRAMS)
        object Middle : Solid(MIDDLE_PACK_IN_GRAMS)

        companion object {
            const val LITTLE_PACK_IN_GRAMS = 5
            const val MIDDLE_PACK_IN_GRAMS = 30
        }
    }

    sealed class Liquid(inGrams: Int) : MealPhysicsType(inGrams) {
        object CupOfSizeS : Liquid(SIZE_S_IN_GRAMS)
        object CupOfSizeM : Liquid(SIZE_M_IN_GRAMS)
        object CupOfSizeL : Liquid(SIZE_L_IN_GRAMS)

        companion object {
            const val SIZE_S_IN_GRAMS = 225
            const val SIZE_M_IN_GRAMS = 270
            const val SIZE_L_IN_GRAMS = 350
        }
    }
}