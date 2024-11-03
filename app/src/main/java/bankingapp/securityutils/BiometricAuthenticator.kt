package bankingapp.securityutils
import android.app.Activity
import android.content.Context
import android.os.CancellationSignal
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricAuthenticator {

    private var cancellationSignal: CancellationSignal? = null

    // Function to check if biometric authentication is available
    fun isBiometricAvailable(context: Context): Boolean {
        val biometricManager = androidx.biometric.BiometricManager.from(context)
        return biometricManager.canAuthenticate() == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
    }

    // Function to initiate biometric authentication
    fun authenticate(
        activity: Activity,
        title: String = "Biometric Authentication",
        subtitle: String = "Authenticate using your biometrics",
        description: String = "Place your finger on the sensor",
        onAuthSuccess: () -> Unit,
        onAuthError: (error: String) -> Unit
    ) {
        if (!isBiometricAvailable(activity)) {
            onAuthError("Biometric authentication is not available")
            return
        }

        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(activity as FragmentActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onAuthSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onAuthError(errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                onAuthError("Authentication failed. Try again.")
            }
        })


        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setNegativeButtonText("Cancel")
            .build()

        cancellationSignal = CancellationSignal()
        biometricPrompt.authenticate(promptInfo)
    }

    // Cancel authentication if needed
    fun cancelAuthentication() {
        cancellationSignal?.cancel()
        cancellationSignal = null
    }
}

