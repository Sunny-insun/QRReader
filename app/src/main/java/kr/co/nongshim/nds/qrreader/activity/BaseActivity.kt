package kr.co.nongshim.nds.qrreader.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.nongshim.nds.qrreader.GlobalApplication
import kr.co.nongshim.nds.qrreader.R
import kr.co.nongshim.nds.qrreader.utils.Logger

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d("$this : onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Logger.d("$this : onDestroy")
    }

    override fun onResume() {
        super.onResume()
        Logger.d("$this : onResume")
    }

    override fun onPause() {
        super.onPause()
        Logger.d("$this : onPause")
    }

    override fun onStart() {
        super.onStart()
        Logger.d("$this : onStart")
    }

    override fun onStop() {
        super.onStop()
        Logger.d("$this : onStop")
    }
}