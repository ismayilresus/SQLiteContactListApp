package com.thecheckpoint.sqlitedemolistapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "ContactsDatabase"

        private val TABLE_CONTACTS = "ContactsTable"

        private val KEY_ID = "_id"
        private val KEY_NAME = "name"
        private val KEY_NUMBER = "number"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_NUMBER + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS" + TABLE_CONTACTS)
        onCreate(db)
    }


    fun addContact(cnt: ContactsModelClass): Long{
        val db = this.writableDatabase
        val ContentValues = ContentValues()
        ContentValues.put(KEY_NAME, cnt.name)
        ContentValues.put(KEY_NUMBER, cnt.number)

        val insertDb = db.insert(TABLE_CONTACTS, null, ContentValues)
        db.close()
        return insertDb
    }

    fun viewContacts(): ArrayList<ContactsModelClass> {
        val db = this.readableDatabase
        val cntList : ArrayList<ContactsModelClass> = ArrayList<ContactsModelClass>()
        val selectQuery = "SELECT  * FROM $TABLE_CONTACTS"

        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id : Int
        var name : String
        var number : String

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                number = cursor.getString(cursor.getColumnIndex(KEY_NUMBER))

                val cnt = ContactsModelClass(id = id, name = name, number = number)
                cntList.add(cnt)
            } while (cursor.moveToNext())
        }
        return cntList
    }

    fun updateContact(cnt: ContactsModelClass) : Int {
        val db = this.writableDatabase
        val ContentValues = ContentValues()
        ContentValues.put(KEY_NAME, cnt.name)
        ContentValues.put(KEY_NUMBER, cnt.number)

        val update = db.update(TABLE_CONTACTS,ContentValues, KEY_ID +  "=" + cnt.id, null)
        db.close()
        return update
    }

    fun deleteContact(cnt: ContactsModelClass) : Int {
        val db = this.writableDatabase
        val ContentValues = ContentValues()
        ContentValues.put(KEY_ID, cnt.id)
        val delete = db.delete(TABLE_CONTACTS, KEY_ID + "=" + cnt.id, null)
        db.close()
        return delete

    }

}
