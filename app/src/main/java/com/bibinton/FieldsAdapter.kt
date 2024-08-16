package com.bibinton

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bibinton.databinding.ItemFieldBinding

class FieldsAdapter(private val startList: List<Field>) :
    RecyclerView.Adapter<FieldsAdapter.ViewHolder>() {

    private val listOfFields = mutableListOf<Field>()

    init {
        listOfFields.addAll(startList)
    }

    fun getListOfFields() = listOfFields

    inner class ViewHolder(val binding: ItemFieldBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Field, position: Int) {
            with(binding) {
                title.text = "Field ${position + 1}"
                player1Team1.text = item.team1.player1.name
                player2Team1.text = item.team1.player2?.name ?: "No player \uD83D\uDEAB"
                player1Team2.text = item.team2?.player1?.name ?: "No player \uD83D\uDEAB"
                player2Team2.text = item.team2?.player2?.name ?: "No player \uD83D\uDEAB"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFieldBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return startList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(startList[position], position)
    }
}