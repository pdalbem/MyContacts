package com.ifsp.mycontacts.repository

import com.ifsp.mycontacts.db.ContactsDao
import com.ifsp.mycontacts.db.ContactsEntity
import javax.inject.Inject

class DatabaseRepository @Inject constructor(private val dao: ContactsDao){


    suspend fun saveContact(entity: ContactsEntity) = dao.saveContacts(entity)

    fun getAllContacts()=dao.getAllContacts()

    fun deleteAllContacts()=dao.deleteAllContacts()

    fun sortedASC()=dao.sortedASC()

    fun sortedDESC()=dao.sortedDESC()

    fun searchContact(name: String) = dao.searchContact(name)

    suspend fun updateContact(entity: ContactsEntity)=dao.updateContact(entity)

    suspend fun deleteContact(entity: ContactsEntity)=dao.deleteContact(entity)

    fun getContact(id : Int) = dao.getContact(id)


}