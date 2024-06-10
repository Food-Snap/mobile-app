package com.foodsnap.app.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.foodsnap.app.data.model.Session
import com.foodsnap.app.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    suspend fun saveSession(session: Session) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = session.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    fun getSession(): Flow<Session> {
        return dataStore.data.map { preferences ->
            Session(
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[AGE_KEY] = user.age
            preferences[WEIGHT_KEY] = user.weight
            preferences[HEIGHT_KEY] = user.height
            preferences[GENDER_KEY] = user.gender
            preferences[BMI_KEY] = user.bmi
        }
    }

    fun getUser(): Flow<User> {
        return dataStore.data.map { preferences ->
            User(
                preferences[NAME_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[AGE_KEY] ?: 0,
                preferences[WEIGHT_KEY] ?: 0F,
                preferences[HEIGHT_KEY] ?: 0F,
                preferences[GENDER_KEY] ?: "",
                preferences[BMI_KEY] ?: 0F
            )
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val AGE_KEY = intPreferencesKey("age")
        private val GENDER_KEY = stringPreferencesKey("gender")
        private val WEIGHT_KEY = floatPreferencesKey("weight")
        private val HEIGHT_KEY = floatPreferencesKey("height")
        private val BMI_KEY = floatPreferencesKey("bmi")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this){
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}