package engsoft.matfit.model

import com.google.gson.annotations.SerializedName

data class Equipment(
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("nome")
    val name: String = "",
    @SerializedName("quantidade")
    val quantity: Int = 0,
)
