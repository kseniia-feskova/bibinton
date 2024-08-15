package com.example.bibinton

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bibinton.databinding.ItemPlayerBinding
import kotlinx.parcelize.Parcelize

class PlayersAdapter(
    private val onDelete: (Player) -> Unit = {},
    private val changeName: (Player) -> Unit = {}
) :
    ListAdapter<Player, PlayersAdapter.ViewHolder>(BankInfoDiffCallback()) {

    private class BankInfoDiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.name == newItem.name && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(val binding: ItemPlayerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Player, position: Int) {
            with(binding) {
                name.text = item.name
                checkbox.isChecked = item.isChecked
                checkbox.setOnClickListener {
                    currentList[position].isChecked = !item.isChecked
                }

                edit.setOnClickListener {
                    edit.isVisible = false
                    editIcons.isVisible = true
                    name.isVisible = false
                    nameInput.setText(name.text)
                    nameInput.isVisible = true
                }

                close.setOnClickListener {
                    if (nameInput.text?.isNotEmpty() == true) {
                        edit.isVisible = true
                        editIcons.isVisible = false
                        name.isVisible = true
                        nameInput.isVisible = false
                        changeName(item.copy(name = nameInput.text.toString()))
                    } else {
                        nameInput.error = "Введите имя"
                    }
                }
                delete.setOnClickListener {
                    onDelete(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPlayerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position], position)
    }

    fun getSelected(): List<Player> {
        return currentList.filter { it.isChecked }
    }


}

@Parcelize
data class Player(
    val id: Int,
    val name: String,
    val score: Float,
    var isChecked: Boolean = false,
) : Parcelable