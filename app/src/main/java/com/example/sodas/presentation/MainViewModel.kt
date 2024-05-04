package com.example.sodas.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sodas.data.Drinkers
import com.example.sodas.data.Soda
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.Random

class MainViewModel : ViewModel() {


    private val realm = ApplicationSoda.realm

    var allSodas = realm.query<Soda>(
    ).asFlow().map {
        it.list.toList()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        emptyList()
    )
    private val sodaList = listOf(
        "Coca-Cola",
        "Pepsi",
        "Sprite",
        "Fanta",
        "Dr Pepper",
        "Mountain Dew",
        "7Up",
        "Ginger Ale",
        "Root Beer",
        "Orange Crush",
        "Grapefruit Soda",
        "Sunkist",
        "Sierra Mist",
        "IBC Root Beer",
        "A&W Root Beer"
    )

    private val peopleList = listOf(
        "John Doe",
        "Jane Smith",
        "David Lee",
        "Maria Garcia",
        "Michael Brown",
        "Elizabeth Jones",
        "Andrew Wilson",
        "Margaret Miller",
        "Charles Taylor",
        "Catherine Walker",
        "Robert Williams",
        "Emily Davis",
        "William Johnson",
        "Jennifer Lopez",
        "Matthew Lewis"
    )

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAddDrinkers(soda: Soda) {
        viewModelScope.launch {
            val newSoda = soda.copyFromRealm()
            realm.write {
                val drinker = Drinkers().apply {
                    name = pickRandomName(peopleList)
                    date = LocalDateTime.now().toString()
                    age = 19
                }
                newSoda.drinkers.add(drinker)
                copyToRealm(drinker, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(newSoda, updatePolicy = UpdatePolicy.ALL)

            }

        }
    }

    fun onDeleteSoda(soda: Soda) {
        viewModelScope.launch {
            realm.write {
                val sodaToDelete = findLatest(soda) ?: return@write
                delete(sodaToDelete)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onAddSoda() {
        viewModelScope.launch(Dispatchers.IO) {
            realm.write {
                val soda = Soda().apply {
                    name = pickRandomName(sodaList)
                    date = LocalDateTime.now().toString()
                }

                copyToRealm(soda, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }

    private fun pickRandomName(names: List<String>): String {
        require(names.isNotEmpty()) { "List of names cannot be empty." }
        val randomIndex = Random().nextInt(names.size)
        return names[randomIndex]
    }

}