package com.azhar.spinnerjson

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    var listVersionAndroid: MutableList<String> = ArrayList()
    var mProgress: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //get data JSON
        loadJSON()
    }

    private fun loadJSON() {
        mProgress = ProgressDialog.show(this@MainActivity, null,
                "Mohon Tunggu...", true, false)
        AndroidNetworking.get("https://api.learn2crack.com/android/jsonandroid/")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        try {
                            mProgress?.dismiss()
                            val jsonArray = response.getJSONArray("android")
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                listVersionAndroid.add(jsonObject.getString("name"))

                                val arrayAdapter = ArrayAdapter(
                                        this@MainActivity, android.R.layout.simple_list_item_1, listVersionAndroid)
                                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                inputVersi?.setAdapter(arrayAdapter)
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                            Toast.makeText(this@MainActivity, "Gagal menampilkan data!", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(anError: ANError) {
                        mProgress?.dismiss()
                        Toast.makeText(this@MainActivity, "Tidak ada jaringan internet!", Toast.LENGTH_SHORT).show()
                    }
                })
    }

}