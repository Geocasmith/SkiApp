package com.example.project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.ui.theme.Project1Theme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private val items = listOf<String>(
        "Skiis","Helmet", "Poles","Gloves","Lunch","Water Bottle","Coffee","Jacket","Socks","Boots","Shirt","Hoodie","Goggles"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           Navigation()
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

/**
 * A list with each item in its own row with a checkbox
 */
@Composable()
fun SkiList(packing: List<String>) {

//    //Make a header
//    Text(
//        text = "Ski List",
//        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//        style = MaterialTheme.typography.headlineMedium
//    )
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
        items(packing) { item ->
            SkiItem(item)
            //divider with hex color #d3d3d3
            Divider(color = Color(0x11000100))
        }
    }
    
}


/**
 * An item with a checkbox and text in same row
 */
@Composable
fun SkiItem(item: String) {
    Row(

    ) {
        val checked = remember { mutableStateOf(false) }

        Checkbox(
            checked = checked.value,
            onCheckedChange = { checked.value = it },
            colors = CheckboxDefaults.colors(),
            modifier = Modifier
                .wrapContentHeight()
                .padding(vertical = 33.dp)
        )
        Text(text = item,
        style = MaterialTheme.typography.headlineMedium, modifier = Modifier
                .wrapContentHeight()
                .padding(vertical = 25.dp))
    }
}

interface VolleyCallback {
    fun onSuccess(result: JSONObject?)
}

@Composable
fun getMountainData(latitude:String, longitude:String,callback:VolleyCallback): JSONObject {

    val ctx = LocalContext.current
// Instantiate the RequestQueue.
    val queue = Volley.newRequestQueue(ctx)



    val url = "https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=-43.4717&lon=171.5264"
    var weatherDescription: JSONObject = JSONObject()
    val weather:String = " "
// Request a string response from the provided URL.
    val stringRequest = object: StringRequest(Request.Method.GET, url,
        Response.Listener<String> { response ->
//            println(response.J)
            //turn response into json
            val json = JSONObject(response)
            //get the data from the json
            val data = json.getJSONArray("data")
            //gets first json object
            val weather = data.getJSONObject(0)
            //get the weather description
            weatherDescription = JSONObject(weather.getString("weather"))
//            println(weatherDescription)
            callback.onSuccess(weatherDescription)
        },
        Response.ErrorListener { println("ERROR")})
    {
        override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["X-RapidAPI-Key"] = "eab9c740d1msh8d3458a2dfd02bep102430jsn0edf7ce8e907"
            headers["X-RapidAPI-Host"] = "weatherbit-v1-mashape.p.rapidapi.com"
            return headers
        }
    }

    queue.add(stringRequest)
    println(weatherDescription)

    return weatherDescription
//    val response: HttpResponse = client.get("https://ktor.io/docs/welcome.html")
//    val client = OkHttpClient()
//
//    val request = Request.Builder()
//        .url("https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=-43.4717&lon=171.5264")
//        .get()
//        .addHeader("X-RapidAPI-Key", "eab9c740d1msh8d3458a2dfd02bep102430jsn0edf7ce8e907")
//        .addHeader("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
//        .build()
//
//    val response = client.newCall(request).execute()
////    result = response.body?.string()
//    return "mountaindata"
}
