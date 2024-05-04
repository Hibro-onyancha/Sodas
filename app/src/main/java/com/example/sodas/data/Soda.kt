package com.example.sodas.data

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class Soda : RealmObject {
    @PrimaryKey var _id: ObjectId = BsonObjectId()
    var name: String = ""
    var date: String = ""
    var drinkers: RealmList<Drinkers> = realmListOf()
}