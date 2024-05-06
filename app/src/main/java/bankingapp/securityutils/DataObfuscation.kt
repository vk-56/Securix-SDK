package bankingapp.securityutils

import kotlin.random.Random

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

    fun randomCharacterObfuscate(data: String): String {
        return buildString {
            for (char in data) {
                if (char.isLetterOrDigit()) {
                    append((Random.nextInt(26) + 'a'.toInt()).toChar())
                } else {
                    append(char)
                }
            }
        }
    }

    fun customCharacterObfuscate(data: String, charToReplace: Char): String {
        return data.map { if (it.isLetter()) charToReplace else it }.joinToString("")
    }

    fun shuffleDatabaseRecords(records: List<String>): List<String> {
        return records.shuffled()
    }

    fun maskOutData(data: String, start: Int, end: Int, maskChar: Char = '*'): String {
        return data.substring(0, start) + maskChar.toString().repeat(end - start) + data.substring(end)
    }

}
