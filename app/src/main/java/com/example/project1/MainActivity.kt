package com.example.project1

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.Contacts.SettingsColumns.KEY
import android.util.JsonReader
import android.util.JsonWriter
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
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.project1.ui.theme.Project1Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

class MainActivity : ComponentActivity() {
    private val items = listOf<String>(
        "Skiis",
        "Helmet",
        "Poles",
        "Gloves",
        "Lunch",
        "Water Bottle",
        "Coffee",
        "Jacket",
        "Socks",
        "Boots",
        "Shirt",
        "Hoodie",
        "Goggles"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InitPackingList()
            Navigation()
//            MapView()
//            VisibilityAnimationSample()
//            val parameters = mapOf("sources" to "bbc-news", "apiKey" to KEY)
//            val url = parameterizeUrl("https://newsapi.org/v2/top-headlines", parameters)
//
//            //use LaunchedEffect to launch a coroutine on the main thread
//            LaunchedEffect(url) {
//                val result = getJson(url)
//                val articles = result.getJSONArray("articles")
//                println(articles)
//            }


//            val coroutineScope = rememberCoroutineScope()
//
//            val (location, setLocation) = remember { mutableStateOf<Location?>(null) }
//
//            val getLocationOnClick: () -> Unit = {
//                coroutineScope.launch {
//                    val result = getJson(url)
//                }
//            }

//            val url =
//                URL("https://weatherbit-v1-mashape.p.rapidapi.com/forecast/3hourly?lat=1&lon=1")
//
////            val coroutineScope = rememberCoroutineScope()
//
//            lifecycleScope.launch {
//                val result = getJson(url)
//                val articles = result.getJSONObject("response").getJSONArray("results")
//                println(result)
//            }

        }
    }

    /**
     * Saves initial packing list to local storage
     */
    @Composable
    fun InitPackingList(){
        val ctx = LocalContext.current
        val packing = listOf<String>(
            "Skiis",
            "Helmet",
            "Poles",
            "Gloves",
            "Lunch",
            "Water Bottle",
            "Coffee",
            "Jacket",
            "Socks",
            "Boots",
            "Shirt",
            "Hoodie",
            "Goggles"
        )
        val fileWrite = ctx.openFileOutput("packing.json", Context.MODE_PRIVATE)
        val writer = JsonWriter(OutputStreamWriter(fileWrite))
        writer.setIndent("  ")
        write(packing, writer)
        writer.close()
    }

    /**
     * A list with each item in its own row with a checkbox
     */
    @Composable()
    fun SkiList(packing: List<String>) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp)
        ) {
            items(packing) { item ->
                SkiItem(item)
                //divider with hex color #d3d3d3
                Divider(color = Color(0x11000100))
            }
        }
        //text box to add an item to the list
        Text(
            text = "Add an item",
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
            style = MaterialTheme.typography.bodyMedium
        )
//        TextField(
//            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
//            label = { Text(text = "Item") },
//            onTextChanged = { }
//        )

    }

}
