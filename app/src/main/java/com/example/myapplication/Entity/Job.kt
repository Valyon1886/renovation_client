package com.example.myapplication.Entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

data class Job(
    var id: Int,
    var name: String?,
    var phone: String?,
    var address: String?,
    var images: MutableList<String>?,
    var rating: Double?,
    var description: String?,
    var beginDate: String?,
    var endDate: String?,
    var type: Int,
    var materials: MutableList<Material>?,
    var employers: MutableList<Employer>?,
    var subTasks: MutableList<Job>?,
    var completedSubTasks: MutableList<Job>?
)