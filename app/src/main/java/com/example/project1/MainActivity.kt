package com.example.project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project1.ui.theme.Project1Theme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            getMountainData("-43.4717","171.5264")?.let { Text(it) }
            Text("World")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Project1Theme {
        Greeting("Android")
    }
}

fun getMountainData(latitude:String, longitude:String): String? {
    val url =
        "https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=$latitude&lon=$longitude";
    var result: String? = null
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(url)
        .get()
        .addHeader("X-RapidAPI-Key", "eab9c740d1msh8d3458a2dfd02bep102430jsn0edf7ce8e907")
        .addHeader("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            e.printStackTrace()
        }
        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            result = response.body?.string()


        }
    })
    return result
}
