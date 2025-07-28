package engsoft.matfit.view.alunos.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import engsoft.matfit.databinding.CardModelAlunoBinding
import engsoft.matfit.listener.OnAlunoListener
import engsoft.matfit.model.AlunoDTO
import engsoft.matfit.view.alunos.viewholder.AlunoViewHolder

class AlunoAdapter : RecyclerView.Adapter<AlunoViewHolder>() {

    private var listAlunos: List<AlunoDTO> = arrayListOf()
    private lateinit var listener: OnAlunoListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = CardModelAlunoBinding.inflate(inflater, parent, false)
        return AlunoViewHolder(itemBinding, listener)
    }

    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        holder.bindData(listAlunos[position])
    }

    override fun getItemCount(): Int {
        return listAlunos.count()
    }

    fun attachListener(taskListener: OnAlunoListener) {
        listener = taskListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAlunos(list: List<AlunoDTO>){
        listAlunos = list
        notifyDataSetChanged()
    }
 }