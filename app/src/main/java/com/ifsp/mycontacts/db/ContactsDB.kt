package com.ifsp.mycontacts.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [ContactsEntity::class], version = 1, exportSchema = false)
abstract class ContactsDB : RoomDatabase(){


    abstract fun contactsDao() : ContactsDao



}