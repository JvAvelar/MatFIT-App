package engsoft.matfit.model

import com.google.gson.annotations.SerializedName

data class Student(
    @SerializedName("cpf")
    val cpf: String,

    @SerializedName("nome")
    var name: String,

    @SerializedName("esporte")
    var sport: String,

    @SerializedName("dataPagamento")
    var paymentDate: String = "",

    @SerializedName("pagamentoAtrasado")
    var latePayment: Boolean = false,
)