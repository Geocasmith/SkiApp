package com.example.project1

import android.os.Bundle
import android.os.Message
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.project1.ui.theme.Project1Theme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val items = listOf<String>(
        "Skiis","Helmet", "Poles","Gloves","Lunch","Water Bottle","Coffee","Jacket","Socks","Boots","Shirt","Hoodie","Goggles"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           Project1Theme {
                Surface {
//                    SkiList(packing = items)
                    Text(getMountainData("-43.471667","171.526444"))
                }
           }
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
@Composable
fun SkiList(packing: List<String>) {

//    //Make a header
//    Text(
//        text = "Ski List",
//        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//        style = MaterialTheme.typography.headlineMedium
//    )
    LazyColumn(modifier = Modifier.fillMaxSize()
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
            modifier = Modifier.wrapContentHeight().padding(vertical = 33.dp)
        )
        Text(text = item,
        style = MaterialTheme.typography.headlineMedium, modifier = Modifier.wrapContentHeight().padding(vertical = 25.dp))
    }
}


@Composable
fun getMountainData(latitude:String, longitude:String): String {
//    val url =
//        "https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=$latitude&lon=$longitude";
//    var result: String? = null
//    val client = OkHttpClient()
//
//    val request = Request.Builder()
//        .url(url)
//        .get()
//        .addHeader("X-RapidAPI-Key", "eab9c740d1msh8d3458a2dfd02bep102430jsn0edf7ce8e907")
//        .addHeader("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
//        .build()
//
//    val response = client. newCall(request).execute()
//    val queue = Volley.newRequestQueue(this)
//    val url = "https://www.google.com"

    val client = OkHttpClient()

    val request = Request.Builder()
        .url("https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=-43.4717&lon=171.5264")
        .get()
        .addHeader("X-RapidAPI-Key", "eab9c740d1msh8d3458a2dfd02bep102430jsn0edf7ce8e907")
        .addHeader("X-RapidAPI-Host", "weatherbit-v1-mashape.p.rapidapi.com")
        .build()

    val response = client.newCall(request).execute()
//    result = response.body?.string()
    return "mountaindata"
}
