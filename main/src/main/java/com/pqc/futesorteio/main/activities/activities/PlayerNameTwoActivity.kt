package com.pqc.futesorteio.main.activities.activities

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
import com.pqc.futesorteio.main.activities.utils.showStandardAlert
import kotlinx.android.synthetic.main.activity_player_name_two.*

class PlayerNameTwoActivity : AppCompatActivity() {

    private var playerTwoList: ArrayList<String> = ArrayList<String>()
    private var playerOneList: ArrayList<String> = ArrayList<String>()
    private var selectedList: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_name_two)
    }

    override fun onResume() {
        super.onResume()
        initiateListeners()
    }

    private fun initiateListeners() {
        playerOneList = intent.getStringArrayListExtra("listPlayerOne")

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
        if(selectedList.contains(playerTwoList[position]))
            selectedList.remove(playerTwoList[position])
        else
            selectedList.add(playerTwoList[position])

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
                    if(!playerTwoList.contains(it))
                        playerTwoList.add(it)
                }
            } else {
                if(!playerTwoList.contains(text_name_add.text.toString()))
                    playerTwoList.add(text_name_add.text.toString())
                else
                    showStandardAlert(this, "Atenção", "Já está inserido na lista")
            }

            text_name_add.text.clear()
            loadAdapter()
        } else
            showStandardAlert(this, "Atenção", "Necessário digitar nome")
    }

    private fun loadAdapter() {
        lv.adapter = ArrayAdapter(this, android.R.layout.select_dialog_multichoice, playerTwoList)
        text_count_value.text = playerTwoList.size.toString()
    }

    private fun removeSelecteds() {
        playerTwoList.removeAll(selectedList)
        loadAdapter()
        showButtonRemove()
    }

    private fun next() {
        if(playerTwoList.size > 0){
            val intent = Intent(this, GroupSortFinishActivity::class.java)
                    .putExtra("listPlayerOne", playerOneList)
                    .putExtra("listPlayerTwo", playerTwoList)
                    .putExtra("mode", "vs")
            startActivity(intent)
        } else {
            showStandardAlert(this, "Atenção", "Você não inseriu nenhum nome na lista")
        }
    }
}
