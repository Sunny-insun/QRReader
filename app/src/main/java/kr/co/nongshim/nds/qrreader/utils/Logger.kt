package kr.co.nongshim.nds.qrreader.utils

import android.util.Log
import kr.co.nongshim.nds.qrreader.GlobalApplication
/**
 * Smart-go
 * Class: Logger
 * Created by 이인선 on 2021-03-16.
 *
 * Description: 로그 사용 util
 */
object Logger {
    private val TAG ="symcs"

    fun e(message:String){
        if(GlobalApplication.DEBUG){
            Log.e(TAG, buildLogMsg(message))
        }
    }

    fun w(message:String){
        if(GlobalApplication.DEBUG){
            Log.w(TAG, buildLogMsg(message))
        }
    }

    fun i(message:String){
        if(GlobalApplication.DEBUG){
            Log.i(TAG, buildLogMsg(message))
        }
    }

    fun d(message:String){
        if(GlobalApplication.DEBUG){
            Log.d(TAG, buildLogMsg(message))
        }
    }

    fun v(message:String){
        if(GlobalApplication.DEBUG){
            Log.v(TAG, buildLogMsg(message))
        }
    }

    fun longLog(message:String){
        if (message.length > 4000) {
            Log.d(TAG, message.substring(0, 4000));
            longLog(message.substring(4000));
        } else {
            Log.d(TAG, message);
        }
    }

    private fun buildLogMsg (message : String ) : String {
        val ste : StackTraceElement = Thread.currentThread().stackTrace[4]
        val sb : StringBuilder= StringBuilder()
        sb.append("[")
        sb.append(ste.fileName.replace(".java", ""))
        sb.append("::")
        sb.append(ste.methodName)
        sb.append("]")
        sb.append(message);
        return sb.toString();
    }
}