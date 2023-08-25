package com.voitov.common.domain.use_cases

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FilterOutDigitsUseCase @Inject constructor() {
    operator fun invoke(value: String): String {
        return value.filter { it.isDigit() }
    }
}