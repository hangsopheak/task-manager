package kh.edu.rupp.taskmanagement.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kh.edu.rupp.taskmanagement.ui.SortOrder
import kh.edu.rupp.taskmanagement.ui.ThemeChoice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// one small file that survives a force stop, per user
private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPrefs(context: Context) {
    private val store = context.dataStore

    val sortOrder: Flow<SortOrder> = store.data
        .map { prefs -> SortOrder.valueOf(prefs[SORT_KEY] ?: SortOrder.DUE_DATE.name) }

    val themeChoice: Flow<ThemeChoice> = store.data
        .map { prefs -> ThemeChoice.valueOf(prefs[THEME_KEY] ?: ThemeChoice.SYSTEM.name) }

    suspend fun setSortOrder(order: SortOrder) {
        store.edit { it[SORT_KEY] = order.name }
    }

    suspend fun setThemeChoice(choice: ThemeChoice) {
        store.edit { it[THEME_KEY] = choice.name }
    }

    private companion object {
        val SORT_KEY = stringPreferencesKey("sort_order")
        val THEME_KEY = stringPreferencesKey("theme_choice")
    }
}
