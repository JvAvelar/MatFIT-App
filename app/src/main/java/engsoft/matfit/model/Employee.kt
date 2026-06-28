package engsoft.matfit.model

import com.google.gson.annotations.SerializedName

data class Employee (
    @SerializedName("cpf")
    var cpf: String = "",

    @SerializedName("nome")
    var name: String = "",

    @SerializedName("funcao")
    var function: String = "",

    @SerializedName("cargaHoraria")
    var workload: Int = 0
)