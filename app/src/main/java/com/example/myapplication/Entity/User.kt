package com.example.myapplication.Entity

data class User(
    var userName: String, /*имя в приложении*/
    var password: String, /*пароль*/
    var imgSrc: String, /*изображение профиля*/
    var firstName: String, /*имя бригадира*/
    var secondName: String, /*отчество бригадира*/
    var lastName: String, /*фамилия бригадира*/
    var jobs: MutableList<Job>?, /*списко выполняемых заданий*/
    var employers: MutableList<Employer>?, /*список сотрудников в бригаде*/
    var completedTasks: MutableList<Job>?, /*список выполненных заданий*/
    val id: Int /*идентификатор пользователя*/
)