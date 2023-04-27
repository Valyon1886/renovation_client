package com.example.myapplication.Entity

data class User(
    var userName: String,
    var password: String,
    var imgSrc: String,
    var firstName: String,
    var secondName: String,
    var lastName: String,
    var jobs: MutableList<Job>?,
    var employers: MutableList<Employer>?,
    val id: Int
)