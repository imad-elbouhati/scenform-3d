package com.imadev.helloscenform

import androidx.annotation.RawRes

data class Model(@RawRes val sbf: Int, val image: Int,var selected: Boolean = false) {


    fun toggleSelected(): Model {
        selected = !selected
        return this
    }
}