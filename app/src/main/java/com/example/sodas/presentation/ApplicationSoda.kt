package com.example.sodas.presentation

import android.app.Application
import com.example.sodas.data.Drinkers
import com.example.sodas.data.Soda
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class ApplicationSoda : Application() {
    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        val config = RealmConfiguration.create(
            schema = setOf(
                Drinkers::class,
                Soda::class
            )
        )
        realm = Realm.open(config)
    }
}
