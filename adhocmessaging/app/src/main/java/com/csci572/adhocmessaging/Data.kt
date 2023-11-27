package com.csci572.adhocmessaging

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update


@Entity
data class Message (
    @PrimaryKey val id: Int? = null,
    @ColumnInfo(name = "contact_mac") val contactMAC: String,
    @ColumnInfo(name = "source_is_me") val sourceIsMe: Boolean,
    @ColumnInfo(name = "contents") val contents: String
)

@Dao
interface MessageDao {

    @Insert
    fun insert(message: Message)

    @Delete
    fun delete(message: Message)

    @Query("SELECT * FROM message WHERE contact_mac = :contactMAC")
    fun getMessagesForContact(contactMAC: String)
}

@Database(entities = [Message::class], version = 1)
abstract class MessageDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
}
