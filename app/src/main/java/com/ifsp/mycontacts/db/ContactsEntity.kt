package com.ifsp.mycontacts.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ifsp.mycontacts.utils.Constants.CONTACTS_TABLE

@Entity(tableName = CONTACTS_TABLE)
data class ContactsEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    var name : String = "",
    var phone : String= ""
)
