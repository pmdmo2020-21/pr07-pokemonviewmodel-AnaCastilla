package es.iessaladillo.pedrojoya.intents.ui.battle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.databinding.BattleActivityBinding
import es.iessaladillo.pedrojoya.intents.ui.selection.SelectionActivity
import es.iessaladillo.pedrojoya.intents.ui.winner.WinnerActivity

class BattleActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POKEMON_ID: String = "EXTRA_POKEMON_ID"

        fun newIntent(context: Context, pokemonId: Long): Intent {
            return Intent(context, BattleActivity::class.java).putExtra(EXTRA_POKEMON_ID, pokemonId)
        }
    }

    private lateinit var binding: BattleActivityBinding
    private lateinit var selectFirstPokemon: ActivityResultLauncher<Intent>
    private lateinit var selectSecondPokemon: ActivityResultLauncher<Intent>
    private val pokemonIds: LongArray = longArrayOf(0, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BattleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupBattle()
        setupViews()
    }

    private fun setupBattle() {
        selectFirstPokemon = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (checkActivityResult(result.resultCode, result.data)) {
                    setPokemon(result.data, binding.imgPokemonBattleTop, binding.txtTopPokemonName, 0)
                }
            }
        selectSecondPokemon = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (checkActivityResult(result.resultCode, result.data)) {
                    setPokemon(result.data, binding.imgPokemonBattleBottom, binding.txtBottomPokemonName, 1)
                }
            }
    }

    private fun checkActivityResult(resultCode: Int, data: Intent?): Boolean {
        return resultCode == RESULT_OK && data != null
    }

    private fun setupViews() {
        setRandomPokemons()
        binding.llTop.setOnClickListener {
            navigateToSelectionActivity(pokemonIds[0], selectFirstPokemon)
        }
        binding.llBottom.setOnClickListener {
            navigateToSelectionActivity(pokemonIds[1], selectSecondPokemon)
        }
        binding.btnFight.setOnClickListener {
            val winnerPokemon: Long = winner()
            navigateToWinnerActivity(winnerPokemon)
        }
    }

    private fun setPokemon(data: Intent?, imgPokemon: ImageView, namePokemon: TextView, index: Int) {
        pokemonIds[index] = data!!.getLongExtra(EXTRA_POKEMON_ID, 0)
        setPokemonInfo(imgPokemon, namePokemon, pokemonIds[index])
    }

    private fun winner(): Long {
        val pokemon1: Pokemon = Database.getPokemonById(pokemonIds[0])!!
        val pokemon2: Pokemon = Database.getPokemonById(pokemonIds[1])!!

        return if (pokemon1.power > pokemon2.power) pokemon1.id else pokemon2.id
    }

    private fun setPokemonInfo(imgBattlePokemon: ImageView, txtBattlePokemonName: TextView, pokemonId: Long) {
        val pokemon: Pokemon = Database.getPokemonById(pokemonId)!!
        setPokemonImageForBattle(imgBattlePokemon, pokemon.image)
        setPokemonNameForBattle(txtBattlePokemonName, pokemon.name)
    }

    private fun setPokemonImageForBattle(img: ImageView, imgId: Int) {
        img.setImageDrawable(getDrawable(imgId))
    }

    private fun setPokemonNameForBattle(textView: TextView, name: Int) {
        textView.text = getText(name)
    }

    private fun getRandomPokemonFromDatabase(): Pokemon {
        return Database.getRandomPokemon()
    }

    private fun setRandomPokemon(image: ImageView, name: TextView, position: Int) {
        val randomPokemon: Pokemon = getRandomPokemonFromDatabase()
        setPokemonNameForBattle(name, randomPokemon.name)
        setPokemonImageForBattle(image, randomPokemon.image)
        pokemonIds[position] = randomPokemon.id
    }

    private fun setRandomPokemons() {
        setRandomPokemon(binding.imgPokemonBattleTop, binding.txtTopPokemonName, 0)
        setRandomPokemon(binding.imgPokemonBattleBottom, binding.txtBottomPokemonName, 1)
    }

    private fun navigateToSelectionActivity(pokemonId: Long, activityResultLauncher: ActivityResultLauncher<Intent>) {
        val intent: Intent = SelectionActivity.newIntent(this, pokemonId)
        activityResultLauncher.launch(intent)
    }

    private fun navigateToWinnerActivity(winnerPokemon: Long) {
        val intent = WinnerActivity.newIntent(this, winnerPokemon)
        startActivity(intent)
    }


}