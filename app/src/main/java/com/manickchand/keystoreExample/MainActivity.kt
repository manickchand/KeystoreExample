package com.manickchand.keystoreExample

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val crypto = Crypto()
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = this.getSharedPreferences("com.manickchand.keystoreExample", 0)

        setListAdapter()

        //clica e decripta
        listAlias.setOnItemClickListener{ parent, view, position, id->
            toDecrypt(listAlias.adapter.getItem(position).toString())
        }
    }

    //pega todos aliases e mostra na lista
    fun setListAdapter() {
        listAlias.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,crypto.listAll());
    }

    fun toEncrypt(view: View){

        if(hasTextToEncrypt()){
            //encripta
            val encryptMap = crypto.encryptText(et_alias.text.toString(),et_text.text.toString())

            Log.i("KeystoreExample", "iv "+encryptMap.get("iv"))
            Log.i("KeystoreExample", "encrypted "+encryptMap.get("encrypted"))

            preferences.edit().putString(et_alias.text.toString(),encryptMap.toString()).apply()

            tv_response.text = encryptMap.get("encrypted").toString()

            setListAdapter()
        }
    }

    fun toDecrypt(alias:String){

        // alias existe ?
        val encData = preferences.getString(alias, null)

        if(encData != null) {
            //string to json
            val map = JSONObject(encData.toString())

            //decripta
            tv_response.text = crypto.decryptText(
                alias,
                map.getString("encrypted"),
                map.getString("iv")
            )
        }
        else Toast.makeText(this,"Alias not found",Toast.LENGTH_SHORT).show()


    }

    //verifica se tem texto e alias escrito para encriptar
    fun hasTextToEncrypt() = if(et_text.text.isNotEmpty() && et_alias.text.isNotEmpty()) true else false

}
