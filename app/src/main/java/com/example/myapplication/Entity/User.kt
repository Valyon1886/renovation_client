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
    var completedTasks: MutableList<Job>?,
    val id: Int
)