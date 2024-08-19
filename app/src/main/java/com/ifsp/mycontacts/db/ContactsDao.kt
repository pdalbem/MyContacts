package com.ifsp.mycontacts.db

import androidx.room.*
import com.ifsp.mycontacts.utils.Constants.CONTACTS_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveContacts(contactsEntity: ContactsEntity)

    @Query("SELECT * FROM $CONTACTS_TABLE")
    fun getAllContacts() : Flow<MutableList<ContactsEntity>>

    @Query("DELETE FROM $CONTACTS_TABLE")
    fun deleteAllContacts()

    @Query("SELECT * FROM $CONTACTS_TABLE ORDER BY name ASC")
    fun sortedASC() : Flow<MutableList<ContactsEntity>>

    @Query("SELECT * FROM $CONTACTS_TABLE ORDER BY name DESC")
    fun sortedDESC() : Flow<MutableList<ContactsEntity>>


    @Query("SELECT * FROM $CONTACTS_TABLE WHERE name LIKE '%' || :name || '%' ")
    fun searchContact(name: String): Flow<MutableList<ContactsEntity>>

    @Update
    suspend fun updateContact(entity: ContactsEntity)

    @Delete
    suspend fun deleteContact(entity: ContactsEntity)

    @Query("SELECT * FROM $CONTACTS_TABLE WHERE id == :id")
    fun getContact(id : Int) : Flow<ContactsEntity>

}