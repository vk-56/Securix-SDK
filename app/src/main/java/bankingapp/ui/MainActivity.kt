package bankingapp.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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
        // Check APK signature
        val isSignatureValid = ApkSignatureVerifier.isSignatureValid(this, expectedSignature)
        if (!isSignatureValid) {
            showCustomToast("Invalid APK signature!")
            finish()
        }
        else {
            showCustomToast("Valid APK signature!")
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