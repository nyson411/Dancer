package app.amano.nayu.dance

import android.icu.text.Transliterator

data class Dancer (
    val id: Int,
    val name : String,
    val positionList :MutableList<Position>,
        )