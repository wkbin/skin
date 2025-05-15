package top.wkbin.skin

import android.content.Context
import android.content.SharedPreferences

class SkinPreference(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences(
        PREF_NAME,
        Context.MODE_PRIVATE
    )

    fun setSkin(name: String, md5: String, path: String) {
        preferences.edit().apply {
            putString(KEY_SKIN_NAME, name)
            putString(KEY_SKIN_MD5, md5)
            putString(KEY_SKIN_PATH, path)
            apply()
        }
    }

    fun setSkinPath(path: String) {
        preferences.edit().apply {
            putString(KEY_SKIN_PATH, path)
            apply()
        }
    }

    fun getSkinName(): String {
        return preferences.getString(KEY_SKIN_NAME, "") ?: ""
    }

    fun getSkinMd5(): String {
        return preferences.getString(KEY_SKIN_MD5, "") ?: ""
    }

    fun getSkinPath(): String {
        return preferences.getString(KEY_SKIN_PATH, "") ?: ""
    }

    companion object {
        private const val PREF_NAME = "skin_preference"
        private const val KEY_SKIN_NAME = "skin_name"
        private const val KEY_SKIN_MD5 = "skin_md5"
        private const val KEY_SKIN_PATH = "skin_path"
    }
} 