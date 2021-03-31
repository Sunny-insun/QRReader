package kr.co.nongshim.nds.qrreader

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.*
import kr.co.nongshim.nds.qrreader.utils.Logger
import kr.co.nongshim.nds.qrreader.utils.Logger.i

/**
 * QRReader
 * Class: GlobalApplication
 * Created by in040 on 2021-03-24.
 *
 * Description:
 */
class GlobalApplication : Application() , LifecycleObserver {

    lateinit var mActivity : Activity
    lateinit var mContext : Context
    var isActivityState = false

    companion object {
        var DEBUG: Boolean = false
        lateinit var instance : GlobalApplication
    }

    override fun onCreate() {
        super.onCreate()

        //라이프사이클 observer 추가
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        DEBUG = isDebuggable(this)
        instance = this
        mContext = this
        Logger.i("$mContext")

    }

    /** 디버그 모드 확인 **/
    fun isDebuggable(context: Context) : Boolean{
        var debuggable : Boolean = false
        val pm: PackageManager = context.packageManager

        try{
            val appInfo : ApplicationInfo = pm.getApplicationInfo(context.packageName, 0)
            debuggable = (0 != (appInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return debuggable
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        isActivityState = false
        Logger.i("onActivityBackgrounded : ${isActivityState}")
        Logger.i("onActivityBackgrounded")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        isActivityState = true
        Logger.i("onActivityForegrounded : ${isActivityState}")
        Logger.i("onActivityForegrounded")
    }



}