package bankingapp.securityutils

import android.app.Activity
import android.content.Context
import android.view.Window
import android.view.WindowManager
import android.widget.Toast

object AntiScreenshot {
    fun preventScreenshots(context: Context) {
        val window = (context as Activity).window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
}
