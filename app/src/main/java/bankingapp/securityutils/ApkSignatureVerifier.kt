package bankingapp.securityutils

import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object ApkSignatureVerifier {

    private const val TAG = "ApkSignatureVerifier"

    fun isSignatureValid(context: Context, expectedSignature: String): Boolean {
        Log.d(TAG, "Expected Signature: $expectedSignature")  // Log expected signature

        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNATURES)
            val signatures = packageInfo.signatures
            for (signature in signatures) {
                val signatureString = signatureToString(signature)
                Log.d(TAG, "Signature String: $signatureString")  // Log the actual signature string

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
