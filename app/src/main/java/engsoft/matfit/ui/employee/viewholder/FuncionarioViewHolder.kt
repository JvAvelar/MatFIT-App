package engsoft.matfit.view.funcionarios.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import engsoft.matfit.databinding.CardModelFuncionarioBinding
import engsoft.matfit.listener.OnEmployeeListener
import engsoft.matfit.model.Employee

class FuncionarioViewHolder(
    private val bind: CardModelFuncionarioBinding,
    private val listener: OnEmployeeListener, ) : RecyclerView.ViewHolder(bind.root) {

    /**
     * Atribui valores aos elementos de interface do card model
     */
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun bindData(func: Employee) {
        val cpf = formatarCpf(func.cpf)
        bind.textDoCpf.text = " $cpf"
        bind.textDoName.text = " ${func.nome}"
        bind.textDoSport.text = " ${func.funcao}"
        bind.textDoDay.text = " ${func.cargaHoraria}"

        bind.iconEdit.setOnClickListener {
            listener.onUpdate(func.cpf)
        }

        bind.iconDelete.setOnClickListener {
            listener.onDelete(func.cpf)
        }
    }

    private fun formatarCpf(cpf: String): String {
        val cpfNumerico = cpf.replace("[^0-9]".toRegex(), "")
        return if (cpfNumerico.length == 11) {
            "%s.%s.%s-%s".format(
                cpfNumerico.substring(0, 3),
                cpfNumerico.substring(3, 6),
                cpfNumerico.substring(6, 9),
                cpfNumerico.substring(9, 11)
            )
        } else cpf
    }

}