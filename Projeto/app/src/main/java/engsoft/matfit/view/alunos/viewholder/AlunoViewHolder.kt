package engsoft.matfit.view.alunos.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import engsoft.matfit.databinding.CardModelAlunoBinding
import engsoft.matfit.listener.OnAlunoListener
import engsoft.matfit.model.AlunoDTO

class AlunoViewHolder(private val bind: CardModelAlunoBinding, private val listener: OnAlunoListener) :
    RecyclerView.ViewHolder(bind.root) {

    /**
     * Atribui valores aos elementos de interface do card model
     */
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun bindData(aluno: AlunoDTO) {
        val cpf = formatarCpf(aluno.cpf)
        bind.textDoCpf.text = " $cpf"
        bind.textDoName.text = " ${aluno.nome}"
        bind.textDoSport.text = " ${aluno.esporte}"
        bind.textDoDay.text = " ${aluno.dataPagamento}"

        bind.iconEdit.setOnClickListener {
            listener.onUpdate(aluno.cpf)
        }

        bind.iconDelete.setOnClickListener{
            listener.onDelete(aluno.cpf)
        }

        bind.iconPayment.setOnClickListener{
            listener.OnPayment(aluno.cpf)
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