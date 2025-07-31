package engsoft.matfit.model

data class StudentResponseDTO(
    val cpf: String,
    val nome: String,
    val esporte: String,
    val dataPagamento: String,
    val pagamentoAtrasado: Boolean
)