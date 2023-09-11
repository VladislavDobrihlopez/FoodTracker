package com.voitov.tracker_data.remote

fun String.query(queriedKey: String, queriedValue: Any): String {
    return this.plus("&$queriedKey=$queriedValue")
}