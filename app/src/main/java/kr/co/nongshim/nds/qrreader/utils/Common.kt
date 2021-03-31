package kr.co.nongshim.nds.qrreader.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kr.co.nongshim.nds.qrreader.GlobalApplication
import kr.co.nongshim.nds.qrreader.R
import kr.co.nongshim.nds.qrreader.activity.QRReaderActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * QRReader
 * Class: Common
 * Created by in040 on 2021-03-30.
 *
 * Description:
 */
object Common {

    @SuppressLint("UseCompatLoadingForDrawables")
    fun showProgressBar(context: Context, text: String) : AlertDialog {

        val llPadding = 5
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        val llParam = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        ll.background = context.getDrawable(R.color.white)

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        progressBar.indeterminateDrawable = context.resources.getDrawable(R.drawable.common_progressbar)
     //   progressBar.indeterminateDrawable.setColorFilter(context.resources.getColor(R.color.white),android.graphics.PorterDuff.Mode.SRC_IN)
        val tvText = TextView(context)
        tvText.text = text
        tvText.setTextColor(context.getColor(R.color.black))
        tvText.textSize = 15f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        val dialog: AlertDialog = builder.create()
        if (context != null) {
            dialog.show()
        }

        val window: Window = dialog.window!!
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.window!!.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.window!!.attributes = layoutParams
        }

        dialog.setCancelable(false)
        return dialog
    }

    /** 현재시간 가져오기 **/
    fun getCurrentTimeFormat():String{
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdfNow = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdfNow.format(date)
    }

    /** 음성 출력 **/
    fun speech(textToSpeech: TextToSpeech, text : String){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }else {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null)
        }
    }
}