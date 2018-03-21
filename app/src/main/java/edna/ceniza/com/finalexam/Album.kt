package edna.ceniza.com.finalexam

import com.google.gson.annotations.SerializedName

/**
 * Created by Edna Ceniza on 21/03/2018.
 */
data class Album (
        val name: String,
        val singer: String,
        @SerializedName ("#text") val text: String
)
