package bankingapp.securityutils

object DataObfuscation {
    private const val OBSCURE_CHAR = '*'

    fun obfuscateData(data: String): String {
        val visiblePart = data.takeLast(4)
        val obscuredPart = OBSCURE_CHAR.toString().repeat(data.length - 4)
        return obscuredPart + visiblePart
    }

    fun deobfuscateData(obfuscatedData: String): String {
        // For demonstration purposes, return the obfuscatedData as-is
        return obfuscatedData
    }
}