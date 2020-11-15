package es.iessaladillo.pedrojoya.intents.data.local

import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.data.local.model.PokemonFactory
import kotlin.random.Random

// TODO: Haz que Database implemente DataSource
object Database: DataSource {
    val pokemons: List<Pokemon> = PokemonFactory.pokemonDatabase()
    val rnd: Random = Random.Default

    override fun getRandomPokemon(): Pokemon {
        var randomPokemon = rnd.nextInt(6)

        return pokemons[randomPokemon]
    }

    override fun getAllPokemons(): List<Pokemon> {
        return pokemons
    }

    override fun getPokemonById(id: Long): Pokemon? {
        val pokemon = pokemons.filter { pokemon -> pokemon.id == id }

        if(pokemon.isEmpty()) {
            throw IllegalArgumentException("There aren't pokemons with that ID in the Database")
        }

        return pokemon[0]
    }


}