package com.example.bibinton

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bibinton.databinding.FragmentFieldsBinding

class FieldsFragment : Fragment() {

    lateinit var binding: FragmentFieldsBinding
    lateinit var adapter: FieldsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFieldsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        val fields = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList("Fields", Field::class.java)
        } else {
            arguments?.getParcelableArrayList("Fields")
        }
        val players = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelableArrayList("Players", Player::class.java)
        } else {
            arguments?.getParcelableArrayList("Players")
        }
        adapter = FieldsAdapter(fields?.toList() ?: emptyList())
        binding.fields.adapter = adapter

        binding.button.setOnClickListener {
            generateFields(players?.toList() ?: emptyList())
        }
        binding.saveBtn.setOnClickListener {

        }
    }

    private fun generateFields(list: List<Player>) {
        val numbers = list.indices // Ваши числа
        val shuffledNumbers = numbers.shuffled() // Перемешиваем числа

        val pairs = shuffledNumbers.chunked(2) // Делим перемешанный список на пары

        val listOfTeams = mutableListOf<Team>()
        // Выводим случайные пары
        pairs.forEachIndexed { index, pair ->
            println("Пара ${index + 1}: ${pair.joinToString()}")
            if (pair.size == 2) {
                val team = Team(list[pair[0]], list[pair[1]])
                listOfTeams.add(team)
            } else {
                val team = Team(list[pair[0]])
                listOfTeams.add(team)
            }
        }

        val pairsTeams = listOfTeams.chunked(2) // Делим перемешанный список на пары

        val listOfFields = arrayListOf<Field>()
        pairsTeams.forEachIndexed { index, pair ->
            println("Команда ${index + 1}: ${pair.joinToString()}")
            if (pair.size == 2) {
                val field = Field(pair[0], pair[1])
                listOfFields.add(field)
            } else {
                val field = Field(pair[0])
                listOfFields.add(field)
            }
        }
        adapter = FieldsAdapter(listOfFields.toList())
        binding.fields.adapter = adapter
    }

    companion object {
        fun newInstance(
            listOfFields: ArrayList<Field>,
            players: ArrayList<Player>
        ): FieldsFragment {
            val args = Bundle()
            args.putParcelableArrayList("Fields", listOfFields)
            args.putParcelableArrayList("Players", players)
            val fragment = FieldsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}