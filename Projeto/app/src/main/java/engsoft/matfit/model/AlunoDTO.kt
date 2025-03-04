package engsoft.matfit.model

data class AlunoDTO(
    val cpf: String,
    var nome: String,
    var esporte: String,
    var dataPagamento: String = "",
    var pagamentoAtrasado: Boolean = false,
)