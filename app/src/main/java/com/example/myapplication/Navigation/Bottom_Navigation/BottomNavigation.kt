package com.example.myapplication.Navigation.Bottom_Navigation

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.myapplication.ui.theme.NavColor

@Composable
fun BottomNavigation(
    navController: NavController
){
    val listItems = listOf(
        BottomItem.Profile,
        BottomItem.Jobs,
        BottomItem.Tasks,
        BottomItem.Docx
    )

    androidx.compose.material.BottomNavigation(
        backgroundColor = NavColor
    ) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRout = backStackEntry?.destination?.route

        listItems.forEach{item ->
            BottomNavigationItem(
                selected = currentRout == item.route,
                onClick = {
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconId),
                        contentDescription = "icon"
                    )
                },
                selectedContentColor = Color.Red,
                unselectedContentColor = Color.Gray
            )
        }
    }
}