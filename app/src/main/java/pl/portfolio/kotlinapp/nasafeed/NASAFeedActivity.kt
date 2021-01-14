package pl.portfolio.kotlinapp.nasafeed

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.portfolio.kotlinapp.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class NASAFeedActivity: AppCompatActivity() {

    private val feedUrl = "https://www.nasa.gov/rss/dyn/solar_system.rss"
    private val list: ArrayList<FeedEntry> = arrayListOf()
    private val adapter = FeedAdapter(list)
    private val TAG = "NASA Feed"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nasafeed_activity)
        title = "NASA Feed"
        val feedListView = findViewById<RecyclerView>(R.id.nasaFeedListView)
        val linearManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(feedListView.getContext(),linearManager.getOrientation())
        feedListView.adapter = adapter
        feedListView.layoutManager = linearManager
        feedListView.addItemDecoration(dividerItemDecoration);
        feedListView.setHasFixedSize(true)

        val url:URL? = try {
            URL(feedUrl)
        }catch (e: MalformedURLException){
            Log.e("Exception", e.toString())
            null
        }

        //url?.let { adapter.notifyDataSetChanged() }

        // io dispatcher for networking operation
        lifecycleScope.launch(Dispatchers.IO) {
            url?.getString()?.apply {

                // default dispatcher for json parsing, cpu intensive work
                withContext(Dispatchers.Default) {
                    val parser = ParseApplication()
                    parser.parse(this@apply)
                    list.clear()
                    parser.getNasaNews().removeAt(0)
                    list.addAll(0,parser.getNasaNews())

                    // main dispatcher for interaction with ui objects
                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()

                    }
                }
            }
        }
    }
}

// extension function to get string data from url
fun URL.getString(): String {
    val stream = openStream()
    return try {
        val r = BufferedReader(InputStreamReader(stream))
        val result = StringBuilder()
        var line: String?
        while (r.readLine().also { line = it } != null) {
            result.append(line).appendln()
        }
        result.toString()
    }catch (e: IOException){
        e.toString()
    }
}