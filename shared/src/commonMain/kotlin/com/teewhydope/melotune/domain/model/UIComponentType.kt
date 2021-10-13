package com.teewhydope.melotune.domain.model

sealed class UIComponentType {

    object Dialog : UIComponentType()

    object None : UIComponentType()
}