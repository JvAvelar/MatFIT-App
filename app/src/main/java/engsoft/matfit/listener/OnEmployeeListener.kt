package engsoft.matfit.listener

interface OnEmployeeListener {
    fun onUpdate(cpf: String)
    fun onDelete(cpf: String)
}