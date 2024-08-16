package com.bibinton

import android.app.AlertDialog
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bibinton.R
import com.example.bibinton.databinding.FragmentPlayersBinding
import kotlinx.parcelize.Parcelize

class PlayersFragment : Fragment() {

    private lateinit var binding: FragmentPlayersBinding
    private val adapter = PlayersAdapter(
        onDelete = ::deletePlayer,
        changeName = ::changeName
    )

    private var items = currentList.toList()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        setAdapter()
        setButtons()
    }

    private fun setAdapter() {
        with(binding) {
            list.adapter = adapter
            adapter.submitList(items)
        }
    }

    private fun setButtons() {
        with(binding) {
            button.setOnClickListener {
                val players = adapter.getSelected()
                generateTeams(players)
            }
        }
    }

    private fun generateTeams(list: List<Player>) {
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
        val players = arrayListOf<Player>()
        players.addAll(list)
        val fieldsFragment = FieldsFragment.newInstance(listOfFields, players)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fieldsFragment, tag)
            .addToBackStack(tag)
            .commitAllowingStateLoss()

    }

    private fun deletePlayer(player: Player) {
        context?.let {
            AlertDialog.Builder(it).setTitle("Delete player")
                .setMessage("You sure to delete ${player.name}?")
                .setPositiveButton("Agree") { _, _ ->
                    currentList.remove(player)
                    items = currentList
                    adapter.submitList(items)
                }.show()
        }
    }

    private fun changeName(player: Player) {
        items = currentList.map {
            if (it.id == player.id) player
            else it
        }
        adapter.submitList(items)
    }

    companion object {
        val currentList = mutableListOf(
            Player(1, "Oleg \uD83D\uDE24", 0.8f),
            Player(2, "Nikita \uD83D\uDE08", 0.98f),
            Player(3, "Daniil \uD83D\uDE33", 0.5f),
            Player(4, "Nadya \uD83C\uDF08", 0.8f),
            Player(5, "Polina \uD83C\uDF38", 0.7f),
            Player(6, "Arina \uD83E\uDD2F", 0.4f),
            Player(7, "Kseniia \uD83C\uDF3F", 0.8f),
            Player(8, "Nastya \uD83C\uDF1F", 0.6f),
            Player(9, "Sophiia \uD83D\uDD25", 0.3f),
            Player(10, "Sanya pivovar \uD83C\uDF7A", 0.3f)
        ).sortedByDescending { it.score }.toMutableList()
    }
}

@Parcelize
data class Team(
    val player1: Player,
    val player2: Player? = null,
    val score: Int = 0
) : Parcelable

@Parcelize
data class Field(
    val team1: Team,
    val team2: Team? = null
) : Parcelable