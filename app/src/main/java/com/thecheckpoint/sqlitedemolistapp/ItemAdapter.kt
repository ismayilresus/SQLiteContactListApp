package com.thecheckpoint.sqlitedemolistapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ItemAdapter(val context: Context, val items: ArrayList<ContactsModelClass>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.items,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.tvName.text = item.name
        holder.tvNumber.text = item.number
        holder.ivEdit.setOnClickListener { view ->

            if (context is MainActivity) {
                context.updateRecordDialog(item)
            }
        }

        holder.ivDelete.setOnClickListener { view ->

            if (context is MainActivity) {
                context.deleteRecordAlertDialog(item)
            }
        }
        if (position % 2 == 0) {
            holder.ItemMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorLightGray
                )
            )
        } else {
            holder.ItemMain.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite))
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ItemMain = view.findViewById<LinearLayout>(R.id.ItemMain)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvNumber = view.findViewById<TextView>(R.id.tvNumber)
        val ivEdit = view.findViewById<ImageView>(R.id.ivEdit)
        val ivDelete = view.findViewById<ImageView>(R.id.ivDelete)
    }
}