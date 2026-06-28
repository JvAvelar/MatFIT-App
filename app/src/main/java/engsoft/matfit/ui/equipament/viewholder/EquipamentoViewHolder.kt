package engsoft.matfit.view.equipamentos.viewholder

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import engsoft.matfit.databinding.CardModelEquipamentoBinding
import engsoft.matfit.listener.OnEquipamentListener
import engsoft.matfit.model.Equipment

class EquipamentoViewHolder(private val bind: CardModelEquipamentoBinding, private val listener: OnEquipamentListener) :
    RecyclerView.ViewHolder(bind.root) {

     // Atribui valores aos elementos de interface do card model
    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    fun bindData(equipamento: Equipment) {
        bind.textDoCpf.text = " ${equipamento.name}"
        bind.textDoName.text = " ${equipamento.quantity}"

        bind.iconEdit.setOnClickListener {
            listener.onUpdate(equipamento.id)
        }

        bind.iconDelete.setOnClickListener{
            listener.onDelete(equipamento.id)
        }
    }
 }