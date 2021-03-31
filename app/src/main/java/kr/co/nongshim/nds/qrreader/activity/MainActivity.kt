package kr.co.nongshim.nds.qrreader.activity
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kr.co.nongshim.nds.qrreader.GlobalApplication
import kr.co.nongshim.nds.qrreader.R
import kr.co.nongshim.nds.qrreader.config.UrlConfig
import kr.co.nongshim.nds.qrreader.customview.CustomDialog
import kr.co.nongshim.nds.qrreader.javascript.WebViewInterface
import kr.co.nongshim.nds.qrreader.utils.Common.showProgressBar
import kr.co.nongshim.nds.qrreader.utils.Logger

class MainActivity : AppCompatActivity() {

    lateinit var webView : WebView
    lateinit var webSettings : WebSettings


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        GlobalApplication.instance.mActivity = this
        init()
    }

    private fun init(){
        webView = findViewById(R.id.webView)
        webSettings = webView.settings
        webSettings.apply {
            javaScriptEnabled = true // 웹페이지 자바스크립트 허용 여부
            textZoom = 100 // 하드웨어 글자크기에 영향 받지 않게
            javaScriptCanOpenWindowsAutomatically = true // 멀티뷰 허용 여부
            loadWithOverviewMode = true // 메타태그 허용 여부
            useWideViewPort = true // 화면 사이즈 맞추기 여부
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN // Deprecated
            domStorageEnabled = true // 로컬 저장소 허용 여부

            //main activity
            supportMultipleWindows()
            cacheMode = WebSettings.LOAD_NO_CACHE // 브라우저 캐시 허용 여부
            databaseEnabled = true
            pluginState = WebSettings.PluginState.ON
            webViewInit()
        }
    }

    /** 웹뷰 셋팅**/
    private fun webViewInit(){
        webView.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setLayerType(View.LAYER_TYPE_HARDWARE, null)
            } else {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            }
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient(){
                var dialog = showProgressBar(this@MainActivity,"로딩중입니다.")
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    dialog.show()
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    dialog.dismiss()
                }
            }
            overScrollMode = View.OVER_SCROLL_NEVER //웹뷰 오버스크롤 이펙트 삭제
            // URl
            webView.addJavascriptInterface(WebViewInterface(), "webViewInterface")

            if (GlobalApplication.DEBUG) {
                Logger.i("DEBUG 일 때")
                Logger.d(UrlConfig.BASE_URL)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WebView.setWebContentsDebuggingEnabled(true)
                }
                webView.loadUrl(UrlConfig.BASE_URL)
            } else {
                //릴리즈
            }
        }
    }

    override fun onResume() {
        super.onResume()
        GlobalApplication.instance.mActivity = this
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