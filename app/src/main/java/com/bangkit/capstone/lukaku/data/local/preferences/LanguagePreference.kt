package com.bangkit.capstone.lukaku.data.local.preferences

import android.content.Context

internal class LanguagePreference(context: Context) {
    companion object {
        const val LANGUAGE_PREF = "LANGUAGE_PREF"
    }

    private val preferences = context.getSharedPreferences(LANGUAGE_PREF, Context.MODE_PRIVATE)

    fun setLanguage(value: String) = preferences.edit().putString(LANGUAGE_PREF, value).apply()

    fun getLanguage(): String? = preferences.getString(LANGUAGE_PREF, "en")
}