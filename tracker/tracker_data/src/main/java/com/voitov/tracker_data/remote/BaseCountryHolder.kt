package com.voitov.tracker_data.remote

import com.voitov.tracker_domain.model.Country

sealed class BaseCountryHolder(private val countryCode: String) {
    abstract fun isRelativeType(country: Country): Boolean
    fun replaceCountry(url: String): String {
        return url.replace(OpenFoodApiService.COUNTRY_PATH, countryCode)
    }

    class Russia : BaseCountryHolder("ru") {
        override fun isRelativeType(country: Country): Boolean {
            return country == Country.RUSSIA
        }
    }

    class Belarus : BaseCountryHolder("by") {
        override fun isRelativeType(country: Country): Boolean {
            return country == Country.BELARUS
        }
    }

    class Ukraine : BaseCountryHolder("ua") {
        override fun isRelativeType(country: Country): Boolean {
            return country == Country.UKRAINE
        }
    }

    class Poland : BaseCountryHolder("pl") {
        override fun isRelativeType(country: Country): Boolean {
            return country == Country.POLAND
        }
    }

    class Germany : BaseCountryHolder("de") {
        override fun isRelativeType(country: Country): Boolean {
            return country == Country.GERMANY
        }
    }

    class USA : BaseCountryHolder("us") {
        override fun isRelativeType(country: Country): Boolean {
            return country == Country.USA
        }
    }

    class UK : BaseCountryHolder("uk") {
        override fun isRelativeType(country: Country): Boolean {
            return country == Country.UK
        }
    }

    class World : BaseCountryHolder("world") {
        override fun isRelativeType(country: Country): Boolean {
            return country == Country.WORLD
        }
    }
}