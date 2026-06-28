package engsoft.matfit.model

data class StudentResponseDTO(
    val cpf: String = "",
    val name: String = "",
    val sport: String = "",
    val paymentDate: String = "",
    val latePayment: Boolean = false
)