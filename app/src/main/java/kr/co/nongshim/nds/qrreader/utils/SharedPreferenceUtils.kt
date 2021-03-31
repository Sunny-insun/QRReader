package kr.co.nongshim.nds.qrreader.utils

import android.content.Context
import kr.co.nongshim.nds.qrreader.GlobalApplication

/**
 * QRReader
 * Class: SharedPreferenceUtils
 * Created by 이인선 on 2021-03-24.
 *
 * Description: SharedPreference 관련 유틸
 */
object SharedPreferenceUtils {

    fun getString(key: String): String? {
        val sharedPref = GlobalApplication.instance.mContext.getSharedPreferences("smart-go", Context.MODE_PRIVATE)
        return sharedPref?.getString(key, "")
    }

    fun setString(key: String, values: String) {
        val sharedPref = GlobalApplication.instance.mContext.getSharedPreferences("smart-go", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, values)
        editor.apply()
    }

    fun getInt(key: String): Int {
        val sharedPref = GlobalApplication.instance.mContext.getSharedPreferences("smart-go", Context.MODE_PRIVATE);
        return sharedPref.getInt(key, 0);
    }

    fun setInt(key: String, values: Int) {
        val sharedPref = GlobalApplication.instance.mContext.getSharedPreferences("smart-go", Context.MODE_PRIVATE);
        val editor = sharedPref.edit();
        editor.putInt(key, values);
        editor.apply();
    }

    fun getBoolean(key: String): Boolean {
        val sharedPref = GlobalApplication.instance.mContext.getSharedPreferences("smart-go", Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, false);
    }

    fun setBoolean(key: String, value: Boolean) {
        val sharedPref = GlobalApplication.instance.mContext.getSharedPreferences("smart-go", Context.MODE_PRIVATE);
        val editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
}