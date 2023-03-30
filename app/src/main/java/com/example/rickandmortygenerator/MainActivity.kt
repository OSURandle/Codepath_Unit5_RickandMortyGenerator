package com.example.rickandmortygenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.w3c.dom.Text
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var rickimageURL = ""
    var rickName = ""
    var rickSpecies = ""
    private lateinit var Name: TextView
    private lateinit var Race: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.rickButton)
        val imageView = findViewById<ImageView>(R.id.imageRick)
        val searchbutton = findViewById<Button>(R.id.searchbutton)
        val search = findViewById<EditText>(R.id.search)
        Name = findViewById<TextView>(R.id.textName)
        Race = findViewById<TextView>(R.id.textRace)
        getrickInfoURL()
        getNextInfo(button, imageView)
        searchbutton.setOnClickListener{
            var input = search.text.toString()
            searchRick(input, imageView)
        }
    }

    private fun searchRick(input: String, imageView: ImageView) {
        val client = AsyncHttpClient()
        client["https://rickandmortyapi.com/api/character/?name=$input", object: JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val results = json.jsonObject.getJSONArray("results").getJSONObject(0)

                rickimageURL = results.getString("image")
                rickName = results.getString("name")
                rickSpecies = results.getString("species")
                Log.d("rickinfoURL", "rick info URL set")
                Log.d("Rick", "response successful$json")

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Rick Error", errorResponse)
            }
        }]
        Glide.with(this)
            .load(rickimageURL)
            .fitCenter()
            .into(imageView)
        Name.text = rickName
        Race.text = rickSpecies
    }
    private fun getrickInfoURL() {
        val client = AsyncHttpClient()
        var characterID = Random.nextInt(826)
        client["https://rickandmortyapi.com/api/character/$characterID", object: JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                rickimageURL = json.jsonObject.getString("image")
                rickName = json.jsonObject.getString("name")
                rickSpecies = json.jsonObject.getString("species")
                Log.d("rickinfoURL", "rick info URL set")
                Log.d("Rick", "response successful$json")

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Rick Error", errorResponse)
            }
        }]
    }
    private fun getNextInfo(button: Button, imageView: ImageView){
        button.setOnClickListener{
            getrickInfoURL()
            Glide.with(this)
                .load(rickimageURL)
                .fitCenter()
                .into(imageView)
            Name.text = rickName
            Race.text = rickSpecies

        }
    }
}