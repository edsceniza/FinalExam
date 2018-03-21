package edna.ceniza.com.finalexam

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private var albumName: String? = null
    private var malbumName: String? = null

    private val albumlist = ArrayList<Album>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener({

            albumName = editText.toString().toLowerCase()
            malbumName = albumName

            val url = "/2.0/?method=album.search&album=$malbumName&api_key=ab24caf5ec7887d2c7b5b858f47784de&format=json"
            val request = Request.Builder().url(url).build()
            val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    Log.e("LastFM Api", "Failed to get album", e)
                }

                override fun onResponse(call: Call?, response: Response?) {
                    if (response != null && response.isSuccessful) {
                        val json = response.body()?.string()
                        displayResult(json)
                    }
                }
            })
        })
    }

    private fun displayResult(json: String?) {

        runOnUiThread {
            val gson = GsonBuilder().create()
            val album = gson.fromJson(json, Album::class.java)

            val albumsearch_length = JSONObject(json).getJSONArray("results").length()
            var albumsearch_counter = 0
            for (i in 1..albumsearch_length) {
                var album_det = JSONObject(json).getJSONArray("albummatches").getJSONObject(albumsearch_counter).getJSONObject("album").getString("name")
                var album_det2 = JSONObject(json).getJSONArray("albummatches").getJSONObject(albumsearch_counter).getJSONObject("album").getString("singer")
                albumlist.add(Album(album_det, album_det2))
                recyclerView.adapter = AlbumAdapter(albumlist)
                albumsearch_counter++
            }
        }

    }
}
