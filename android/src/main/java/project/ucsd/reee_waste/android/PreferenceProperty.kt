package project.ucsd.reee_waste.android

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferenceProperty<T>(
        private val key: String,
        private val defaultValue: T,
        private val getter: SharedPreferences.(String, T) -> T,
        private val setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
) : ReadWriteProperty<Fragment, T> {

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            thisRef.context!!.getPreferences()
                    .getter(key, defaultValue)

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) =
            thisRef.context!!.getPreferences()
                    .edit()
                    .setter(key, value)
                    .apply()

    private fun Context.getPreferences(): SharedPreferences =
            getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
}

fun stringPreference(
        key: String,
        defaultValue: String?
): ReadWriteProperty<Fragment, String?> = PreferenceProperty(
        key = key,
        defaultValue = defaultValue,
        getter = SharedPreferences::getString,
        setter = SharedPreferences.Editor::putString
)