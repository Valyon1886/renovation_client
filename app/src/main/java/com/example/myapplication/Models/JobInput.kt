package com.example.myapplication.Models

import com.example.myapplication.Entity.Material

data class JobInput(
    var name: String?,
    var description: String?,
    var materials: MutableList<Material>?
)