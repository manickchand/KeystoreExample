package com.manickchand.keystoreExample

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import org.json.JSONObject
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlin.collections.ArrayList

class Crypto {

    private val TRANSFORMATION = "AES/GCM/NoPadding" //Algoritimo utilizado
    private val ANDROID_KEY_STORE = "AndroidKeyStore"
    private var mKeyStore: KeyStore? = null

    //inicializa instancia da keyStore
    init{
        mKeyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        mKeyStore!!.load(null)
    }

    fun encryptText(alias: String, textToEncrypt: String): JSONObject {

        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(alias))

        val encryption = cipher.doFinal(textToEncrypt.toByteArray(charset("UTF-8")))
        var encryptData = JSONObject()
        encryptData.put("iv",byteArrayToString(cipher.getIV()))
        encryptData.put("encrypted",byteArrayToString(encryption!!))
        return encryptData
    }

    private fun getSecretKey(alias: String): SecretKey {

        //inicia gerador de chaves com o algoritmo escolhido
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)

        //configura parametros para gerar chave e retorna chave
        keyGenerator.init(
            KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build())

        return keyGenerator.generateKey()
    }

    fun decryptText(alias: String, encryptedData: String, iv: String): String {

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(128, stringToByteArray(iv))
        cipher.init(Cipher.DECRYPT_MODE, getKeyByAlias(alias), spec)

        val descryptoByte = cipher.doFinal(stringToByteArray(encryptedData))
        return String(descryptoByte)
    }

    private fun getKeyByAlias(alias: String): SecretKey {
        return (mKeyStore!!.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }


    fun removeByAlias(alias: String){
        mKeyStore!!.deleteEntry(alias)
    }

    // lista todos os alias da ks
    fun listAll(): ArrayList<String>? {

        var arr = ArrayList<String>()
        for(k in mKeyStore!!.aliases()){
            arr.add(k.toString())
        }
        return arr
    }

    fun byteArrayToString(ba:ByteArray):String{
        return Base64.encodeToString(ba, Base64.DEFAULT)
    }

    fun stringToByteArray(str:String):ByteArray{
        return Base64.decode(str, Base64.DEFAULT)
    }
}