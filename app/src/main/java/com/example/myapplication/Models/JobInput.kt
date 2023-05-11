package com.example.myapplication.Models

import com.example.myapplication.Entity.Employer
import com.example.myapplication.Entity.Material

data class JobInput(
    var name: String?,
    var description: String?,
    var materials: MutableList<Material>?,
    var employers: MutableList<Employer>?,
    var beginDate: String?,
    var endDate: String?
)