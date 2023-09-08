package com.voitov.common.domain

abstract class FoodTrackerDomainException(message: String) : Exception(message) {
    class EncounteredUnknownPreferenceEntity :
        FoodTrackerDomainException("Unable to create class object by text representation")

    class EnteredValueFormatException : FoodTrackerDomainException("Some values invalid")
}