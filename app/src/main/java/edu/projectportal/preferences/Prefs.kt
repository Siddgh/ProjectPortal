package edu.projectportal.preferences

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val favSharedPreference: SharedPreferences = context.getSharedPreferences(
        "fav_prefs",
        Context.MODE_PRIVATE
    )

    fun onlyFav(): Boolean {
        return favSharedPreference.getBoolean("onlyFav", false)
    }

    fun onlyFavValueChange(isChecked: Boolean) {
        val editor = favSharedPreference.edit()
        editor.putBoolean("onlyFav", isChecked)
        editor.apply()

        favSharedPreference.edit().putInt("FontSize", 10).apply();

    }
}