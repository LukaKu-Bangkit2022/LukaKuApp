package com.bangkit.capstone.lukaku.helper

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import androidx.core.os.ConfigurationCompat.getLocales
import java.util.*

object Language {
    fun onSetLanguage(context: Context?, languageId: String): Configuration? {
        val localeSystem = getLocales(Resources.getSystem().configuration).get(0)

        val locale = localeSystem?.let {
            Locale(languageId, it.country, it.variant)
        }

        if (locale != null) Locale.setDefault(locale)
        val config = context?.resources?.configuration
        config?.setLocale(locale)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config?.let { context.createConfigurationContext(it) }
        }

        context?.resources?.updateConfiguration(config, context.resources?.displayMetrics)
        return config
    }
}