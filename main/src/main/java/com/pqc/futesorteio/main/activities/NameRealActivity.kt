package com.pqc.futesorteio.main.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import com.pqc.futesorteio.main.R
import kotlinx.android.synthetic.main.activity_name_real.*

class NameRealActivity : AppCompatActivity() {

    private val playerList: ArrayList<String> = ArrayList<String>()
    private val selectedList: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_name_real)
    }

    override fun onResume() {
        super.onResume()
        initiateListeners()
    }

    private fun initiateListeners() {
        btn_remove.setOnClickListener { removeSelecteds() }
        btn_next.setOnClickListener { next() }
        btn_back.setOnClickListener { back() }
        button_add.setOnClickListener { addText() }
        lv.setOnItemClickListener { parent, view, position, id -> selectItem(parent, view, position, id) }

        text_name_add.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                addText()
                return@OnKeyListener true
            }
            false
        })

        text_name_add.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER) {
                addText()
                return@OnEditorActionListener true
            }
            false
        })

        text_name_add.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                    charSequence: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
            ) {
                if (before == 0 && count == 1 && charSequence[start] == '\n') {
                    text_name_add.text.replace(start, start + 1, "")
                    addText()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun back() {
        finish()
    }

    private fun selectItem(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(selectedList.contains(playerList[position]))
            selectedList.remove(playerList[position])
        else
            selectedList.add(playerList[position])

        showButtonRemove()
    }

    private fun showButtonRemove() {
        if(lv.checkedItemCount > 0) btn_remove.visibility = View.VISIBLE else btn_remove.visibility = View.INVISIBLE
    }

    private fun addText() {
        if(text_name_add.text.toString() != "") {
            var lines = text_name_add.text.toString().lines()

            if(lines.size > 1) {
                lines.forEach {
                    if(!playerList.contains(it))
                        playerList.add(it)
                }
            } else {
                if(!playerList.contains(text_name_add.text.toString()))
                    playerList.add(text_name_add.text.toString())
                else
                    showErrorAlert("Já está inserido na lista")
            }

            text_name_add.text.clear()
            loadAdapter()
        } else
            showErrorAlert("Necessário digitar nome")
    }

    private fun loadAdapter() {
        lv.adapter = ArrayAdapter(this, android.R.layout.select_dialog_multichoice, playerList)
        text_count_value.text = playerList.size.toString()
    }

    private fun removeSelecteds() {
        playerList.removeAll(selectedList)
        loadAdapter()
        showButtonRemove()
    }

    private fun next() {
        if(playerList.size > 0){
            val intent = Intent(this, TeamNumberRealActivity::class.java)
                    .putExtra("listPlayer", playerList)
            startActivity(intent)
        } else {
            showErrorAlert("Você não inseriu nenhum nome na lista")
        }
    }

    private fun showErrorAlert(message: String) {
        AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage(message)
                .setCancelable(false)
                .setNeutralButton(android.R.string.ok, null).create()
                .show()
    }
}
