package engsoft.matfit.listener

interface OnStudentListener {
    fun onUpdate(cpf: String)
    fun onDelete(cpf: String)
    fun OnPayment(cpf: String)
}