package engsoft.matfit.model

import com.google.gson.annotations.SerializedName

data class Equipament(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("nome")
    val nome: String,
    @SerializedName("quantidade")
    val quantidade: Int,
)
