package es.iessaladillo.pedrojoya.intents.data.local.model

import es.iessaladillo.pedrojoya.intents.R

class PokemonFactory private constructor() {

    companion object {
        fun pokemonDatabase(): List<Pokemon> {
            return listOf(
                Pokemon(1, R.string.txtBulbasur, R.drawable.bulbasur, 40),
                Pokemon(2, R.string.txtGiratina, R.drawable.giratina, 120),
                Pokemon(3, R.string.txtCubone, R.drawable.cubone, 30),
                Pokemon(4, R.string.txtGyarados, R.drawable.gyarados,150),
                Pokemon(5, R.string.txtFeebas, R.drawable.feebas, 10),
                Pokemon(6, R.string.txtPikachu, R.drawable.pikachu, 90),
            )
        }
    }
}