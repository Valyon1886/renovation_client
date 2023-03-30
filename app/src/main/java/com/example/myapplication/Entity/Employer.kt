package com.example.myapplication.Entity

data class Employer(
    var firstName: String,
    var secondName: String,
    var lastName: String,
    var post: String,
    var cost: Int, // Стоимость в час
    var clock: Int, //Смена
)