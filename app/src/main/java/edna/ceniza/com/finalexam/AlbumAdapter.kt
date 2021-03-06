package edna.ceniza.com.finalexam

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by Edna Ceniza on 21/03/2018.
 */
class AlbumAdapter (val albumList: ArrayList<Album>): RecyclerView.Adapter<AlbumAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.album_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  albumList.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val album: Album = albumList[position]
        holder?.album_name?.setText(album.name)
        holder?.album_singer?.setText(album.singer)
        val album_image = holder?.album_image
        Picasso.with(holder?.view?.context).load(album.text).into(album_image)
    }

    class ViewHolder (val view: View): RecyclerView.ViewHolder(view){
        val album_name = itemView.findViewById<TextView>(R.id.album_name) as TextView
        val album_singer = itemView.findViewById<TextView>(R.id.album_singer) as TextView
        val album_image = itemView.findViewById<ImageView>(R.id.album_image) as ImageView
    }
}