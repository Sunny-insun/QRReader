package kr.co.nongshim.nds.qrreader.javascript

import android.app.Activity
import android.content.Intent
import android.speech.tts.TextToSpeech
import android.webkit.JavascriptInterface
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.co.nongshim.nds.qrreader.GlobalApplication
import kr.co.nongshim.nds.qrreader.activity.MainActivity
import kr.co.nongshim.nds.qrreader.activity.QRReaderActivity
import kr.co.nongshim.nds.qrreader.activity.QRReaderActivity.Companion.readerView
import kr.co.nongshim.nds.qrreader.activity.QRReaderActivity.Companion.textToSpeech
import kr.co.nongshim.nds.qrreader.customview.CustomDialog
import kr.co.nongshim.nds.qrreader.utils.Common.speech
import kr.co.nongshim.nds.qrreader.utils.Logger
import kr.co.nongshim.nds.qrreader.utils.SharedPreferenceUtils

/**
 * QRReader
 * Class: WebViewInterface
 * Created by 이인선 on 2021-03-29.
 *
 * Description: web과 연동된 interface
 */
class WebViewInterface {

    @JavascriptInterface
    fun moveToQrReader(type : String, id : String){
        Logger.i("moveToQrReader called")
        val intent = Intent(GlobalApplication.instance.mActivity, QRReaderActivity::class.java)
        GlobalApplication.instance.mActivity.startActivityForResult(intent, 0)
        SharedPreferenceUtils.setString("id", id)
        var id = SharedPreferenceUtils.getString("id")
        Logger.i("saved successfully id is ${id}")
    }

    @JavascriptInterface
    fun sendResult(type: String) {
        Logger.i("sendResult called.")
        GlobalScope.launch(Dispatchers.Main) {
            readerView.resume()
        }
     //   speech(textToSpeech, type)
        if(type == "fail") {
            CustomDialog.CustomDialogBuilder(GlobalApplication.instance.mActivity)
                .setTitle("오류")
                .setDialogType(CustomDialog.DIALOG_DANGER_ONE_BUTTON)
                .setCancelButtonText("확인")
                .setContent("서버 오류입니다. 다시 한번 확인 해주세요.")
                .setCancelButtonClick(object: CustomDialog.CustomDialogCancelClickListener{
                    override fun onCancelClicked() {
                    }
                })
                .setOkButtonText("")
                .setOkButtonClick(object : CustomDialog.CustomDialogOkClickListener{
                    override fun onOkClicked() {

                    }
                })
                .create()
        }
    }
}