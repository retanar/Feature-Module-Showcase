package com.featuremodule.data.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ThemePreferences @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    fun setLightTheme(theme: String?) = preferences.edit { putString(KEY_THEME_LIGHT, theme) }

    fun setDarkTheme(theme: String?) = preferences.edit { putString(KEY_THEME_DARK, theme) }

    fun setUseSystemDark(theme: String?) =
        preferences.edit { putString(KEY_USE_SYSTEM_DARK, theme) }

    val themeModelFlow = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            // trySendBlocking is used just in case, trySend should be enough too
            trySendBlocking(getCurrentPreferences())
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.onStart {
        emit(getCurrentPreferences())
    }

    fun getCurrentPreferences() = ThemeModel(
        lightTheme = preferences.getString(KEY_THEME_LIGHT, null),
        darkTheme = preferences.getString(KEY_THEME_DARK, null),
        useSystemDarkTheme = preferences.getBoolean(KEY_USE_SYSTEM_DARK, true),
    )

    data class ThemeModel(
        val lightTheme: String?,
        val darkTheme: String?,
        val useSystemDarkTheme: Boolean,
    )

    companion object {
        private const val FILE_NAME = "theme_preferences"
        private const val KEY_THEME_LIGHT = "key_theme_light"
        private const val KEY_THEME_DARK = "key_theme_dark"
        private const val KEY_USE_SYSTEM_DARK = "key_use_system_dark"
    }
}
