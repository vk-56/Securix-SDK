package bankingapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import bankingapp.securityutils.AntiScreenshot
import bankingapp.securityutils.ApkSignatureVerifier
import bankingapp.securityutils.RootChecker
import com.astroanastariq.bankingapp.R
import com.astroanastariq.bankingapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
    private lateinit var appSettingPrefs : SharedPreferences
    private lateinit var sharedPrefsEdit : SharedPreferences.Editor
    private var isNightModeOn by Delegates.notNull<Boolean>()
    private val expectedSignature = "5ad4c6a33a4337e7b010a9fbc9dfe1051fdde035" // SHA-1 Signature Generated through keytool
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.topAppBar)
        //I've used .getFragment instead of findNavController to prevent an error
        val navController = binding.navHostFragmentContentMain
            .getFragment<NavHostFragment>().navController

        val config = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, config)

        setContentView(binding.root)

        AntiScreenshot.preventScreenshots(this)

        lifecycleScope.launch {
            val isRooted = withContext(Dispatchers.IO) {
                RootChecker.isDeviceRooted()
            }
            if (isRooted) {
                showCustomToast()
                finish()
            }
        }
        setupBiometricPrompt()

        // Check if biometrics are available and enrolled
        if (isBiometricAvailable()) {
            authenticateUser()
        } else {
            promptUserToSetUpBiometrics()
        }

        // Certificate verification
        val expectedHash = "56d2fc2ba60fbd5949e66d1c4a97dfaa35d4ca8c3116bf26eb70149830cfc290"
        val isValid = ApkSignatureVerifier.isCertificateValid(this, expectedHash, "SHA-256")
        if (isValid) {
            Log.d("Verification", "Certificate is valid.")
        } else {
            Log.d("Verification", "Certificate verification failed.")
        }

        appSettingPrefs = getSharedPreferences("AppSettingPrefs", 0)
        sharedPrefsEdit = appSettingPrefs.edit()
        isNightModeOn = appSettingPrefs.getBoolean("Night Mode", false)
        if (isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setupBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("BiometricAuth", "Authentication succeeded!")
                // Proceed with authenticated actions
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("BiometricAuth", "Authentication error: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("BiometricAuth", "Authentication failed. Try again.")
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate with your biometrics")
            .setDescription("Use face, fingerprint, or iris to authenticate.")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG) // Allows face, fingerprint, iris, etc.
            .build()
    }

    private fun authenticateUser() {
        biometricPrompt.authenticate(promptInfo)
    }

    private fun isBiometricAvailable(): Boolean {
        val biometricManager = BiometricManager.from(this)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("BiometricAuth", "No biometric hardware available.")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("BiometricAuth", "Biometric hardware currently unavailable.")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.d("BiometricAuth", "No biometrics enrolled.")
                false
            }
            else -> false
        }
    }

    private fun promptUserToSetUpBiometrics() {
        val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
        startActivity(intent)
    }


    private fun showCustomToast(message: String = "Default message from the app") {
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.toast_layout, null)

        // Assuming there is a TextView in your custom layout to show the message
        val textView = layout.findViewById<TextView>(R.id.toastTextView)
        textView.text = message

        val toast = Toast(applicationContext)
        toast.view = layout
        toast.duration = Toast.LENGTH_LONG
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    //Up Button
    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment_content_main)
            .navigateUp()

    //Creating an Options Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.dark_mode_menu, menu)
        return true
    }

    //Handling click events
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dark_mode -> {
                if (isNightModeOn){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPrefsEdit.putBoolean("Night Mode", false)
                    sharedPrefsEdit.apply()
                } else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPrefsEdit.putBoolean("Night Mode", true)
                    sharedPrefsEdit.apply()
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}