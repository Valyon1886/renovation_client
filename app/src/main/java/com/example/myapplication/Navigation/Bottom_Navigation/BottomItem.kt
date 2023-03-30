package com.example.myapplication.Navigation.Bottom_Navigation

import com.example.myapplication.R


sealed class BottomItem(val title: String, val iconId: Int, val route: String) {
    object Profile: BottomItem("Profile", R.drawable.profile, "profile")
    object Jobs: BottomItem("Jobs", R.drawable.jobs, "jobs")
    object Tasks: BottomItem("Tasks", R.drawable.tasks, "tasks")
    object Docx: BottomItem("Docx", R.drawable.docx, "docx")
}