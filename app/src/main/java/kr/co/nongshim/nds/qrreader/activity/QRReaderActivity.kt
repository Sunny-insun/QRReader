package kr.co.nongshim.nds.qrreader.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Camera
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.android.BeepManager
import com.google.zxing.client.android.Intents
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.*
import com.journeyapps.barcodescanner.camera.CameraSettings
import kr.co.nongshim.nds.qrreader.GlobalApplication
import kr.co.nongshim.nds.qrreader.R
import kr.co.nongshim.nds.qrreader.customview.CustomDialog
import kr.co.nongshim.nds.qrreader.utils.Common.getCurrentTimeFormat
import kr.co.nongshim.nds.qrreader.utils.Common.speech
import kr.co.nongshim.nds.qrreader.utils.Logger
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.timer


class QRReaderActivity() : CaptureActivity(){

    lateinit var webView : WebView
    lateinit var convertButton : Button
    lateinit var closeButton : ImageView

    var isFront = true
    companion object{
        lateinit var readerView : DecoratedBarcodeView
        lateinit var textToSpeech : TextToSpeech
        lateinit var beepManager: BeepManager
    }

    var callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            result?.let {
                //null이 아닐 경우
                it.text?.let {
                    try{
                        beepManager.playBeepSound()
                        //정상 인식
                        Toast.makeText(GlobalApplication.instance.mContext, "scanned : ${it}", Toast.LENGTH_SHORT).show()
                        //인식 소리
                        Logger.i("인식한 QR코드 데이터 === ${it}")
//                        {
//                            "CVD_RESULT_NO": "474227",
//                            "VISITOR_NM": "이인선",
//                            "HP_NO": "01031874792",
//                            "VISITOR_TYPE_NM": "employee",
//                            "RESULT_YN": "Y",
//                            "QR_GENESIS_DATE": "2021-03-30"
//                        }
                        var obj = JSONObject(it)
                        obj.put("QR_CHECK_DATE", getCurrentTimeFormat())
                        if(obj.getString("RESULT_YN").equals("N")){
                            speech(textToSpeech,"출입이 불가능합니다")
                        }else{
                            speech(textToSpeech,"인식되었습니다")
                        }
                        runOnUiThread {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                webView.evaluateJavascript(
                                        "javascript:fn_updatePaperResult(${obj})", null
                                )
                                webView.visibility = WebView.GONE
                                readerView.pauseAndWait()
                            } else {
                                webView.loadUrl("javascript:fn_updatePaperResult(${obj})")
                                webView.visibility = WebView.GONE
                            }
                        }
                    }catch (e : Exception) {
                        //올바른 데이터 인식실패
                        e.printStackTrace()
                        Toast.makeText(GlobalApplication.instance.mContext, "QR코드 인식 오류", Toast.LENGTH_SHORT).show()
                    }
                } ?: {
                    //취소버튼 클릭 후 인식 하지 않고 돌아왔을 경우.
                    Toast.makeText(GlobalApplication.instance.mContext, "오류", Toast.LENGTH_SHORT).show()
                }()
            } ?: {
                //result 가 없을 경우
                Toast.makeText(GlobalApplication.instance.mContext, "다시한번 인식 해주세요.", Toast.LENGTH_SHORT).show()
            } ()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_reader)
        webView = GlobalApplication.instance.mActivity.findViewById<WebView>(R.id.webView)
        initView()
        initTextToSpeech()
    }


    private fun initView(){
        readerView = findViewById(R.id.readerView)
        closeButton = findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            CustomDialog.CustomDialogBuilder(this)
                .setTitle("확인")
                .setDialogType(CustomDialog.DIALOG_CONFIRM)
                .setOkButtonText("로그아웃")
                .setCancelButtonText("취소")
                .setContent("로그아웃 하시겠습니까?")
                .setOkButtonClick(object : CustomDialog.CustomDialogOkClickListener{
                    override fun onOkClicked() {
                       var intent = Intent(GlobalApplication.instance.mContext, MainActivity::class.java)
                        startActivity(intent)
                    }
                })
                .setCancelButtonClick(object: CustomDialog.CustomDialogCancelClickListener{
                    override fun onCancelClicked() {
                    }
                })
                .create()
        }

        var formats : Collection<BarcodeFormat> = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        readerView.barcodeView.decoderFactory= DefaultDecoderFactory(formats)
        readerView.initializeFromIntent(intent)
        readerView.decodeContinuous(callback)
        readerView.isSoundEffectsEnabled = true

        convertButton = findViewById(R.id.convertButton)
        convertButton.setOnClickListener {
            switchCamera()
        }
        beepManager = BeepManager(this)
    }

    /**음성 출력 초기화**/
    private fun initTextToSpeech(){
        textToSpeech = TextToSpeech(GlobalApplication.instance.mContext, object : TextToSpeech.OnInitListener{
            override fun onInit(status: Int) {
                if (status == TextToSpeech.SUCCESS) {
                    var result = textToSpeech.setLanguage(Locale.KOREA)
                    textToSpeech.setPitch(0.7f)
                    textToSpeech.setSpeechRate(1.2f)
                }
            }
        })
    }

    /** 카메라 후면, 전면 변경 **/
    private fun switchCamera(){
        readerView.pauseAndWait()
        val s = CameraSettings()
        try {
            if (isFront) {
                s.requestedCameraId = 0
            } else {
                s.requestedCameraId = 1
            }
            readerView.barcodeView.cameraSettings = s
            isFront = !isFront
            readerView.resume()
        } catch (e: java.lang.Exception) {
            Log.i("TAG", "onClick: " + e.message)
        }
    }

    /** 메모리 누출 방지 위해 tts를 중지**/
    override fun onStop() {
        super.onStop()
        textToSpeech?.let {
            textToSpeech.stop()
            textToSpeech.shutdown()
        } ?: {

        }()
    }

    override fun onResume() {
        super.onResume()
        GlobalApplication.instance.mActivity = this
        var cameraSettings = CameraSettings()
        //카메라 전면으로 세팅
        cameraSettings.requestedCameraId = 1
        readerView.barcodeView.cameraSettings = cameraSettings
        readerView.resume()
    }

    override fun onPause() {
        super.onPause()
        readerView.pause()
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return readerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        CustomDialog.CustomDialogBuilder(this)
            .setTitle("종료")
            .setDialogType(CustomDialog.DIALOG_DANGER)
            .setOkButtonText("종료")
            .setCancelButtonText("취소")
            .setContent("앱을 종료 하시겠습니까?")
            .setOkButtonClick(object : CustomDialog.CustomDialogOkClickListener{
                override fun onOkClicked() {
                    finishAffinity();
                    System.runFinalization();
                    System.exit(0);
                }
            })
            .setCancelButtonClick(object: CustomDialog.CustomDialogCancelClickListener{
                override fun onCancelClicked() {
                }
            })
            .create()
    }
}