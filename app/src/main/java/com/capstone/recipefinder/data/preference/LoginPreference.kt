package com.capstone.recipefinder.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import com.capstone.recipefinder.data.user.UserSessions
import com.capstone.recipefinder.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map



class LoginPreference constructor(context: Context) {
    val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setUser(user: UserSessions) {
        val editor = preferences.edit()
        editor.putString(NAME, user.name)
        editor.putString(TOKEN, user.token)
        editor.putString(USER_ID, user.userId)
        editor.putBoolean(LOGIN_STATE, user.isLogin)
        editor.apply()
    }

       fun logout() {
        val editor = preferences.edit()
        editor.remove(NAME)
        editor.remove(TOKEN)
        editor.putBoolean(LOGIN_STATE, false)
        editor.apply()
    }

    fun getUser(): UserSessions {
        return UserSessions(
            preferences.getString(NAME, "") ?: "",
            preferences.getString(TOKEN, "")?: "",
            preferences.getString(USER_ID, "")?: "",
            preferences.getBoolean(LOGIN_STATE, false)
        )
    }


}