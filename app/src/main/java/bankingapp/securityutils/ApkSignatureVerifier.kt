package bankingapp.securityutils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException

object ApkSignatureVerifier {

    private const val TAG = "ApkSignatureVerifier"

    // Method to verify the app's signature with a provided hash and algorithm
    @RequiresApi(Build.VERSION_CODES.P)
    fun isCertificateValid(
        context: Context,
        expectedHash: String,
        hashAlgorithm: String = "SHA-256"
    ): Boolean {
        return try {
            // Get the app's signing certificate
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName, PackageManager.GET_SIGNING_CERTIFICATES
            )
            val signingInfo = packageInfo.signingInfo
            val cert = signingInfo.apkContentsSigners.firstOrNull() ?: return false

            // Compute the hash of the certificate
            val certHash = computeHash(cert.toByteArray(), hashAlgorithm)
            Log.d(TAG, "Computed Certificate Hash: $certHash")

            // Compare the computed hash with the expected hash
            certHash.equals(expectedHash, ignoreCase = true)
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Package not found: ${e.message}")
            false
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "Unsupported hash algorithm: $hashAlgorithm")
            false
        } catch (e: CertificateException) {
            Log.e(TAG, "Certificate error: ${e.message}")
            false
        }
    }

    // Helper function to compute the hash of the certificate bytes
    private fun computeHash(certBytes: ByteArray, algorithm: String): String? {
        return try {
            val md = MessageDigest.getInstance(algorithm)
            val hashBytes = md.digest(certBytes)
            // Convert the hash bytes to a hexadecimal string
            hashBytes.joinToString("") { "%02x".format(it) }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "Hash algorithm not found: ${e.message}")
            null
        }
    }
}
