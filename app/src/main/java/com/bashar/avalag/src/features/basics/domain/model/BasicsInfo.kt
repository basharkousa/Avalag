package com.bashar.avalag.src.features.basics.domain.model

data class BasicsInfo(
    val promotions: List<Promotion>,
    val countries: List<Country>
)

data class Promotion(
    val id: Int,
    val title: String,
    val description: String,
    val image: String
)

data class Country(
    val id: Int,
    val name: String,
    val currency: String
)