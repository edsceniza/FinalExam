package edna.ceniza.com.finalexam

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL


class MainActivity : AppCompatActivity() {

    private var albumName: String? = null
    private var malbumName: String? = null
    private val albumlist = ArrayList<Album>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager = LinearLayoutManager(this)

        button.setOnClickListener({

            albumName = editText.toString().toLowerCase()
            malbumName = albumName

            progressBar.visibility = View.VISIBLE
            textView.visibility = View.GONE

            val url = "http://ws.audioscrobbler.com/2.0/?method=album.search&album=$malbumName&api_key=ab24caf5ec7887d2c7b5b858f47784de&format=json"
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

        progressBar.visibility = View.GONE
        val url = "http://ws.audioscrobbler.com/2.0/?method=album.search&album=$malbumName&api_key=ab24caf5ec7887d2c7b5b858f47784de&format=json"

        for (i in 1..50) {
            doAsync{
//                val gson = GsonBuilder().create()
//                val album = gson.fromJson(json, Album::class.java)
                val resultJson = URL(url).readText()
                val jsonObject = JSONObject(resultJson)

                //val albumsearch_length = JSONObject(json).getJSONArray("results").length()
                //var albumsearch_counter = 0


                var album_det = jsonObject.getJSONObject("results").getJSONObject("albummatches").getJSONArray("album").getJSONObject(i).getString("name")
                var album_det2 = jsonObject.getJSONObject("results").getJSONObject("albummatches").getJSONArray("album").getJSONObject(i).getString("artist")
                var album_det3 = " "
                album_det3 = if(jsonObject.getJSONObject("results").getJSONObject("albummatches").getJSONArray("album")
                            .getJSONObject(i).getJSONArray("image").getJSONObject(2).getString("#text") == ""){
                jsonObject.getJSONObject("results").getJSONObject("albummatches").getJSONArray("album")
                        .getJSONObject(i).getJSONArray("image").getJSONObject(2).getString("")
            }else{
                jsonObject.getJSONObject("results").getJSONObject("albummatches").getJSONArray("album")
                        .getJSONObject(i).getJSONArray("image").getJSONObject(2).getString("#text")
            }

                uiThread{
                    recyclerView.adapter = AlbumAdapter(albumlist)
                    albumlist.add(Album(album_det, album_det2, album_det3))
                }

            }
        }

    }
}
