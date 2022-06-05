package com.bangkit.capstone.lukaku.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.bangkit.capstone.lukaku.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OnboardingDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveOnboarding(isFinished: Boolean) {
        dataStore.edit {
            it[booleanPreferencesKey(Constants.ONBOARDING_PREF)] = isFinished
        }
    }

    fun getOnboarding(): Flow<Boolean?> = dataStore.data.map {
        it[booleanPreferencesKey(Constants.ONBOARDING_PREF)]
    }
}