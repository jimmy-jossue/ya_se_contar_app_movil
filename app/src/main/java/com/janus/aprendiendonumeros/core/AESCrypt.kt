package com.janus.aprendiendonumeros.core

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class AESCrypt {
    companion object {
        private const val ENCRYPTION_AES = "AES"
        private const val ENCRYPTION_KEY = "affDhd1HEJrb67867"

        fun encrypt(pass: String): String {
            val secretKey: SecretKeySpec = generateKey(ENCRYPTION_KEY)
            val cipher: Cipher = Cipher.getInstance(ENCRYPTION_AES)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val bytes = cipher.doFinal(pass.toByteArray())
            return Base64.encodeToString(bytes, Base64.DEFAULT)
        }

        private fun generateKey(key: String): SecretKeySpec {
            val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
            digest.update(key.toByteArray(), 0, key.toByteArray().size)
            val bytes = digest.digest()
            return SecretKeySpec(bytes, "AES")
        }
    }
}