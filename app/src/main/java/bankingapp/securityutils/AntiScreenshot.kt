package bankingapp.securityutils

import android.view.Window
import android.view.WindowManager

object AntiScreenshot {
    fun preventScreenshots(window: Window) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
}