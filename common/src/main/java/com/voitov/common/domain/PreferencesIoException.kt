package com.voitov.common.domain

abstract class PreferencesIoException(message: String) : Exception(message) {
    class EncounteredUnknownPreferenceEntity :
        PreferencesIoException("Unable to create class object by text representation")

}