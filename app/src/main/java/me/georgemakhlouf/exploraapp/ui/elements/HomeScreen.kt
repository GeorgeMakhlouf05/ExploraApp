package me.georgemakhlouf.exploraapp.ui.elements

import android.R.attr.onClick
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(onClickAddTouristicPlace: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Explora Colombia") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(paddingValues = innerPadding)
        ) {

        }
    }
}

@Composable
fun AddTouristicPlaceScreen(){
    Text(fontSize = 50.sp,text = "Add Touristic Place")
}