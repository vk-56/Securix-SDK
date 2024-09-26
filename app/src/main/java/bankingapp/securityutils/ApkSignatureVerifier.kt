package bankingapp.securityutils

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object ApkSignatureVerifier {

    private const val TAG = "ApkSignatureVerifier"
    private var expectedSignature: String? = null

    // Method to generate and store the expected signature
    fun generateExpectedSignature(context: Context) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            val signatures = packageInfo.signatures

            // Assuming you want the first signature
            if (signatures.isNotEmpty()) {
                expectedSignature = signatureToString(signatures[0])
                Log.d(TAG, "Generated Expected Signature: $expectedSignature")  // Log the expected signature
            } else {
                Log.e(TAG, "No signatures found.")
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    // Method to verify the current signature against the expected signature
    fun isSignatureValid(context: Context): Boolean {
        Log.d(TAG, "Expected Signature: $expectedSignature")  // Log expected signature

        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            val signatures = packageInfo.signatures
            for (signature in signatures) {
                val signatureString = signatureToString(signature)
                Log.d(TAG, "Current Signature String: $signatureString")  // Log the actual signature string

                // Compare with the expected signature
                if (signatureString == expectedSignature) {
                    return true
                }
            }
            false
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    private fun signatureToString(signature: Signature): String {
        return try {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            val digest = md.digest()
            digest.joinToString("") { String.format("%02x", it) }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            ""
        }
    }
}
