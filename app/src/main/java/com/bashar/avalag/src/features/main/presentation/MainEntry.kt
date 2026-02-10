package com.bashar.avalag.src.features.main.presentation

import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.bashar.avalag.R

// main/presentation/MainEntry.kt
@Composable
fun MainEntry() {
    MainScreen(
        homeContent = { TestScreen(title = "Home") },       // from Home feature
        cartContent = { TestScreen("Cart") },       // from Cart feature
        ordersContent = { TestScreen("Orders") },   // from Orders feature
        profileContent = { TestScreen("Profile") }  // from Profile feature
    )
}





@Preview
@Composable
fun TestScreen(
    title : String = "Screen",
    onClick:()->Unit = {}
) {

    Scaffold {
        Surface(
            modifier = Modifier.padding(it).clickable{
                onClick()
            }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(modifier = Modifier.clickable{

                }, contentAlignment = Alignment.Center) {
                    Text(
                        title,
                        )
                }
            }
        }
    }

}
