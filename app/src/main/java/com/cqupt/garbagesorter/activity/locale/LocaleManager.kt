package com.cqupt.garbagesorter.activity.locale

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import java.util.*

class LocaleManager(private val context: Context) {

    fun getCurrentLocale(): Locale {
        return context.resources.configuration.locales.get(0)
    }

    fun setLocale(locale: Locale) {
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)
        configuration.setLocale(locale)

        context.createConfigurationContext(configuration)

        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
    }

    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }
}
