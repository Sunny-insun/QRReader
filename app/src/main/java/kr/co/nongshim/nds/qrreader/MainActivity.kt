package kr.co.nongshim.nds.qrreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        IntentIntegrator(this).initiateScan()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        result?.let {
            //null이 아닐 경우
                it.contents?.let {
                    Toast.makeText(this, "scanned : ${result?.contents}", Toast.LENGTH_SHORT).show()
                } ?: {
                    Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show()
                }()
        } ?: {
            //null일 경우
            Toast.makeText(this, "result is null", Toast.LENGTH_SHORT).show()
        } ()
    }
}