package com.thecheckpoint.sqlitedemolistapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<Button>(R.id.BtnAdd).setOnClickListener { view ->
            addRecord(view)
        }
        showItems()

    }


    private fun addRecord(view: View){
        val name = findViewById<EditText>(R.id.EtName).text.toString()
        val number = findViewById<EditText>(R.id.EtNumber).text.toString()
        val databasehandler: DatabaseHandler = DatabaseHandler(this)
        if(!name.isEmpty() && !number.isEmpty()) {
            val addRecord = databasehandler.addContact(ContactsModelClass(0, name, number))
            if (addRecord > -1) {
                Toast.makeText(applicationContext,"Contact Saved",Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.EtNumber).text.clear()
                findViewById<EditText>(R.id.EtName).text.clear()
                showItems()
            } else {
                Toast.makeText(applicationContext, "Name or Number cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getContactsList() : ArrayList<ContactsModelClass>{
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val cntList: ArrayList<ContactsModelClass> = databaseHandler.viewContacts()
        return cntList
    }

    @SuppressLint("CutPasteId")
    private fun showItems(){
        if(getContactsList().size > 0){
            findViewById<RecyclerView>(R.id.rvItemList).visibility = View.VISIBLE
            findViewById<TextView>(R.id.tvEmpty).visibility = View.GONE

            findViewById<RecyclerView>(R.id.rvItemList).layoutManager = LinearLayoutManager(this)

            val itemAdapter = ItemAdapter(this, getContactsList())
            findViewById<RecyclerView>(R.id.rvItemList).adapter = itemAdapter
        } else{
            findViewById<RecyclerView>(R.id.rvItemList).visibility = View.GONE
            findViewById<TextView>(R.id.tvEmpty).visibility = View.VISIBLE
        }
    }

    fun updateRecordDialog(contactsModelClass: ContactsModelClass) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.findViewById<EditText>(R.id.etUpdateName).setText(contactsModelClass.name)
        updateDialog.findViewById<EditText>(R.id.etUpdateNumber).setText(contactsModelClass.number)

        updateDialog.findViewById<TextView>(R.id.tvUpdate).setOnClickListener(View.OnClickListener {

            val name = updateDialog.findViewById<EditText>(R.id.etUpdateName).text.toString()
            val email = updateDialog.findViewById<EditText>(R.id.etUpdateNumber).text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (name.isNotEmpty() && email.isNotEmpty()) {
                val updater = databaseHandler.updateContact(ContactsModelClass(contactsModelClass.id, name, email))
                if (updater > -1) {
                    Toast.makeText(applicationContext, "Contact Updated.", Toast.LENGTH_LONG).show()

                    showItems()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext, "Name or Number cannot be blank", Toast.LENGTH_LONG).show()
            }
        })
        updateDialog.findViewById<TextView>(R.id.tvCancel).setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        updateDialog.show()
    }

    fun deleteRecordAlertDialog(contactModelClass: ContactsModelClass) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Contact")
        builder.setMessage("Are you sure you wants to delete ${contactModelClass.name}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            val delete = databaseHandler.deleteContact(ContactsModelClass(contactModelClass.id, "", ""))
            if (delete > -1) {
                Toast.makeText(applicationContext, "Contact deleted.", Toast.LENGTH_LONG).show()
                showItems()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}