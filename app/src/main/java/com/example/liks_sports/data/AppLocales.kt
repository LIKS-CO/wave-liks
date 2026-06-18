package com.example.liks_sports.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

fun applyAppLanguage(language: SettingsStore.Language) {
    val locales = when (language) {
        SettingsStore.Language.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
        SettingsStore.Language.ENGLISH -> LocaleListCompat.forLanguageTags("en")
        SettingsStore.Language.SPANISH -> LocaleListCompat.forLanguageTags("es")
    }
    AppCompatDelegate.setApplicationLocales(locales)
}
