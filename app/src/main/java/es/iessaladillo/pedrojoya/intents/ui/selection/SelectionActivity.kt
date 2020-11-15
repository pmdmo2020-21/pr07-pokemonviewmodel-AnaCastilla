package es.iessaladillo.pedrojoya.intents.ui.selection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.databinding.SelectionActivityBinding
import es.iessaladillo.pedrojoya.intents.ui.battle.BattleActivity

class SelectionActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POKEMON_ID: String = "EXTRA_POKEMON_ID"
        fun newIntent(context: Context, pokemonId: Long): Intent {
            return Intent(context, SelectionActivity::class.java).putExtra(EXTRA_POKEMON_ID, pokemonId)
        }
    }

    private lateinit var binding: SelectionActivityBinding
    private lateinit var pokemon: Pokemon
    private lateinit var rButtons: Array<RadioButton>
    private lateinit var images: Array<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelectionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getDataFromBattleActivity()
        setupFields()
        setupViews()
        setupInitState()
    }

    override fun onBackPressed() {
        setActivityResult()
        super.onBackPressed()
    }

    private fun setActivityResult() {
        val intent: Intent = BattleActivity.newIntent(this, pokemon.id)
        setResult(RESULT_OK, intent)
    }

    private fun setupViews() {
        setupTagsOfRadioButtons()
        setupTagsForImageViews()
        setupsRadioButtonsListener()
        setupImageViewsListener()
    }

    private fun getDataFromBattleActivity() {
        getIntentData()
        val pokemon: Pokemon = extractPokemon(intent.getLongExtra(EXTRA_POKEMON_ID, 0))
        selectCurrentPokemon(pokemon)
    }

    private fun setupFields() {
        rButtons = arrayOf(
            binding.rbSelectFirstPokemon,
            binding.rbSelectSecondPokemon,
            binding.rbSelectThirdPokemon,
            binding.rbSelectFourthPokemon,
            binding.rbSelectFifthPokemon,
            binding.rbSelectSixthPokemon,
        )
        images = arrayOf(
            binding.imgSelectFirstPokemon,
            binding.imgSelectSecondPokemon,
            binding.imgSelectThirdPokemon,
            binding.imgSelectFourthPokemon,
            binding.imgSelectFifthPokemon,
            binding.imgSelectSixthPokemon,
        )
    }

    private fun setupInitState() {
        var pokemon: Pokemon
        for (rb in rButtons) {
            pokemon = rb.tag as Pokemon

            if (this.pokemon == pokemon) {
                rb.isChecked = true
                return
            }
        }
    }

    private fun getIntentData() {
        if (intent == null || !intent.hasExtra(EXTRA_POKEMON_ID)) {
            throw RuntimeException("SelectionActivity needs to receive a valid pokemon")
        }
    }

    private fun extractPokemon(pokemonId: Long): Pokemon {
        return Database.getPokemonById(pokemonId)?: throw NullPointerException("Pokemon not found")
    }

    private fun setupTagsOfRadioButtons() {
        for (i in rButtons.indices) {
            rButtons[i].tag = Database.getPokemonById(i.toLong() + 1)
        }
    }

    private fun setupTagsForImageViews() {
        for (i in images.indices) {
            images[i].tag = rButtons[i]
        }
    }

    private fun setupsRadioButtonsListener() {
        for (i in rButtons.indices) {
            rButtons[i].setOnClickListener { view: View ->
                checkRadioButton(view)
            }
        }
    }

    private fun setupImageViewsListener() {
        for (i in images.indices) {
            images[i].setOnClickListener { view: View ->
                listenOnClickImage(view)
            }
        }
    }

    private fun listenOnClickImage(view: View) {
        val rb: RadioButton = view.tag as RadioButton
        rb.isChecked = true
        checkRadioButton(rb)
    }

    private fun checkRadioButton(view: View) {
        val selectedRb: RadioButton = view as RadioButton
        for (rb in rButtons) {
            if (selectedRb != rb) {
                rb.isChecked = false
            }
        }
        selectCurrentPokemonFromView(view)
    }

    private fun selectCurrentPokemonFromView(view: View) {
        val pokemon: Pokemon = view.tag as Pokemon
        selectCurrentPokemon(pokemon)
    }

    private fun selectCurrentPokemon(pokemon: Pokemon) {
        this.pokemon = pokemon
    }

}