package com.example.kotlin_mysqli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var mhsAdapter: AdapterDataMhs
    var listaDados = mutableListOf<HashMap<String, String>>()
    val urlDados = "http://192.168.15.14/mysql_kotlin/show_data.php"
    val url2 = "http://192.168.15.14/mysql_kotlin/query_upd_del_ins.php"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mhsAdapter = AdapterDataMhs(listaDados)
        listMhs.layoutManager = LinearLayoutManager(this)
        listMhs.adapter = mhsAdapter
        buttonDelete.setOnClickListener(this)
        buttonInsert.setOnClickListener(this)
        buttonUpdate.setOnClickListener(this)
        buttonFind.setOnClickListener(this)

        showDataMhs("")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonInsert -> {
                queryInsertUpdateDelete("insert")
            }
            R.id.buttonDelete -> {
                queryInsertUpdateDelete("delete")
            }
            R.id.buttonUpdate -> {
                queryInsertUpdateDelete("update")
            }
            R.id.buttonFind -> {
                showDataMhs(edNome.text.toString().trim())
            }
        }
    }


    fun queryInsertUpdateDelete(mode: String) {
        val request = object : StringRequest(Request.Method.POST, url2,
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                val error = jsonObject.getString("code")
                if (error.equals("000")){
                    Toast.makeText(this, "Operação bem sucedida", Toast.LENGTH_LONG).show()
                    showDataMhs("")
                }else {
                    Toast.makeText(this, "Operação FALHOU", Toast.LENGTH_LONG).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this,"Não foi possível conectar ao server" , Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                when (mode) {
                    "insert" -> {
                        hm.put("mode", "insert")
                        hm.put("user_id", edID.text.toString().trim())
                        hm.put("user_nome", edNome.text.toString().trim())
                        hm.put("task_name", edTarefa.text.toString().trim())

                    }
                    "update" -> {
                        hm.put("mode", "update")
                        hm.put("user_id", edID.text.toString().trim())
                        hm.put("user_nome", edNome.text.toString().trim())
                        hm.put("task_name", edTarefa.text.toString().trim())

                    }
                    "delete" -> {
                        hm.put("mode", "delete")
                        hm.put("user_id", edID.text.toString().trim())

                    }
                }
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


    fun showDataMhs(nome: String) {
        val request = object: StringRequest(Request.Method.POST, urlDados,
            Response.Listener { response ->
                listaDados.clear()
                val jsonArray = JSONArray(response)
                for (x in 0..(jsonArray.length() - 1)) {
                    val jsonObject = jsonArray.getJSONObject(x)
                    var mhs = HashMap<String, String>()
                    mhs.put("user_id", jsonObject.getString("user_id"))
                    mhs.put("user_nome", jsonObject.getString("user_nome"))
                    mhs.put("task_name", jsonObject.getString("task_name"))
                    textViewTexto.text = jsonObject.getString("task_name")
                    listaDados.add(mhs)
                }
                mhsAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(this, volleyError.message, Toast.LENGTH_LONG).show()
            }){
            override fun getParams(): MutableMap<String, String> {
                val hm = HashMap<String, String>()
                hm.put("nome", nome)
                return hm
            }
        }
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }

}