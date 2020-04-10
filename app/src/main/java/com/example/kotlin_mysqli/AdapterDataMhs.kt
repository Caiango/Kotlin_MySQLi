package com.example.kotlin_mysqli

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class AdapterDataMhs (val dataMhs: List<HashMap<String, String>>) :
RecyclerView.Adapter<AdapterDataMhs.HolderDataMhs>() {
    lateinit var texto: String
    override fun onCreateViewHolder(
        p0: ViewGroup,
        p1: Int
    ): AdapterDataMhs.HolderDataMhs {
    val v = LayoutInflater.from(p0.context).inflate(R.layout.row_mhs, p0, false)
        return HolderDataMhs(v)
    }

    override fun getItemCount(): Int {
        return dataMhs.size

    }

    override fun onBindViewHolder(p0: AdapterDataMhs.HolderDataMhs, p1: Int) {
        val data = dataMhs.get(p1)
        p0.txNome.setText(data.get("user_nome"))
        p0.txTask.setText(data.get("task_name"))
        p0.txID.setText(data.get("user_id"))
    }

    class HolderDataMhs(v: View) : RecyclerView.ViewHolder(v){

        val txID = v.findViewById<TextView>(R.id.textViewID)
        val txNome = v.findViewById<TextView>(R.id.textViewNome)
        val txTask = v.findViewById<TextView>(R.id.textViewTask)


    }
}