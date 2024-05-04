package com.example.sodas

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sodas.presentation.MainViewModel
import com.example.sodas.presentation.theme.SodasTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SodasTheme {
                // A surface container using the 'background' color from the theme
                val model = MainViewModel()
                val allSodas by remember {
                    model.allSodas
                }.collectAsState()
                val scope = rememberCoroutineScope()
                val state = rememberLazyListState()
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(floatingActionButton = {
                        Button(onClick = {
                            model.onAddSoda()
                            scope.launch {
                                state.animateScrollToItem(allSodas.indexOf(allSodas.lastOrNull()))
                            }
                        }) {
                            Text(text = "click me😥")
                        }
                    }) { _ ->
                        LazyColumn(
                            state = state,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(7.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            itemsIndexed(allSodas) { index, item ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .combinedClickable(
                                            onClick = { model.onAddDrinkers(item) },
                                            onLongClick = {
                                                model.onDeleteSoda(item)
                                            },
                                            onLongClickLabel = "delete"
                                        ), horizontalAlignment = Alignment.Start
                                ) {
                                    Text(text = "$index : ${item.name}     ${item.date} \n ${item._id}")
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(
                                                rememberScrollState()
                                            )
                                    ) {
                                        item.drinkers.forEachIndexed { num, drinker ->
                                            Text(
                                                text = "$num : ${drinker.name}     ${drinker.date} \n ${drinker._id}",
                                                color = Color.Red
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
