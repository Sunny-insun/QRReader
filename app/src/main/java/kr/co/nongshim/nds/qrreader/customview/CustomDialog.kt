package kr.co.nongshim.nds.qrreader.customview

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import kr.co.nongshim.nds.qrreader.GlobalApplication
import kr.co.nongshim.nds.qrreader.R

/**
 * QRReader
 * Class: CustomDialog
 * Created by 이인선 on 2021-03-25.
 *
 * Description: 커스텀 다이얼로그
 */
class CustomDialog(context: Context) : Dialog(context){
    lateinit var title : String
    lateinit var content : String
    lateinit var okButtonText: String
    lateinit var cancelButtonText: String
    var themeType = 0 //default
    private lateinit var okClickListener : CustomDialogOkClickListener
    private lateinit var cancelClickListener : CustomDialogCancelClickListener

    companion object {
        const val DIALOG_DANGER = 100
        const val DIALOG_DANGER_ONE_BUTTON = 101
        const val DIALOG_CONFIRM = 200

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(themeType == DIALOG_DANGER || themeType == DIALOG_DANGER_ONE_BUTTON) {
            setContentView(R.layout.custom_dialog_danger)
            if(themeType == DIALOG_DANGER_ONE_BUTTON){
                findViewById<Button>(R.id.okButton)?.visibility = ViewGroup.GONE
            }
        }else if(themeType == DIALOG_CONFIRM){
            setContentView(R.layout.custom_dialog)
        }
        findViewById<TextView>(R.id.titleTextView)?.text = title;
        findViewById<TextView>(R.id.contentTextView)?.text = content;
        findViewById<Button>(R.id.okButton)?.text = okButtonText;
        findViewById<Button>(R.id.cancelButton)?.text = cancelButtonText
        findViewById<Button>(R.id.okButton)?.setOnClickListener {
            dismiss()
            okClickListener?.onOkClicked()
        }
        findViewById<Button>(R.id.cancelButton)?.setOnClickListener {
            dismiss()
            cancelClickListener?.onCancelClicked()
        }
    }

    interface CustomDialogOkClickListener {
        fun onOkClicked()
    }
    interface CustomDialogCancelClickListener{
        fun onCancelClicked()
    }

    class CustomDialogBuilder(context: Context){
        private val dialog = CustomDialog(context)

        fun setTitle(title: String) : CustomDialogBuilder {
            dialog.title = title
            return this
        }

        fun setContent(content: String) : CustomDialogBuilder {
            dialog.content = content
            return this
        }

        fun setOkButtonText(text : String) : CustomDialogBuilder {
            dialog.okButtonText = text
            return this
        }

        fun setCancelButtonText(text: String) : CustomDialogBuilder {
            dialog.cancelButtonText = text
            return this
        }

        fun setOkButtonClick(listener :CustomDialogOkClickListener) : CustomDialogBuilder{
            dialog.okClickListener = listener
            return this
        }

        fun setCancelButtonClick(listener: CustomDialogCancelClickListener) : CustomDialogBuilder {
            dialog.cancelClickListener = listener
            return this
        }

        fun create() : CustomDialog {
            dialog.show()
            return dialog
        }

        fun setDialogType(type : Int) : CustomDialogBuilder{
            dialog.themeType = type
            return this
        }
    }
}