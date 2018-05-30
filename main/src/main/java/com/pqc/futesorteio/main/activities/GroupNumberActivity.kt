package com.pqc.futesorteio.main.activities

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.pqc.futesorteio.main.R
import kotlinx.android.synthetic.main.activity_group_number.*

class GroupNumberActivity : AppCompatActivity() {

    private var teamList: ArrayList<String> = ArrayList<String>()
    private var playerList: ArrayList<String> = ArrayList<String>()
    private var numberGroups: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_number)
    }

    override fun onResume() {
        super.onResume()
        initiateListeners()
    }

    private fun initiateListeners() {
        teamList =  intent.getStringArrayListExtra("listTeam")
        playerList = intent.getStringArrayListExtra("listPlayer")

        btn_next.setOnClickListener { next() }
        btn_back.setOnClickListener { back() }

        text_group_number.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                next()
                return@OnKeyListener true
            }
            false
        })

        text_group_number.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent?.keyCode == KeyEvent.KEYCODE_ENTER) {
                next()
                return@OnEditorActionListener true
            }
            false
        })

        text_group_number.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                    charSequence: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
            ) {
                if (before == 0 && count == 1 && charSequence[start] == '\n') {
                    text_group_number.text.replace(start, start + 1, "")
                    next()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun back() {
        finish()
    }

    private fun next() {
        if(text_group_number.text.toString() != ""){
            numberGroups = text_group_number.text.toString().toInt()

            if(numberGroups <= teamList.size && numberGroups <= playerList.size) {
                val checkNumber = (if(teamList.size > playerList.size) playerList.size else teamList.size) % numberGroups

                if(checkNumber == 0) {
                    val intent = Intent(this, GroupSortFinishActivity::class.java)
                            .putExtra("listTeam", teamList!!)
                            .putExtra("listPlayer", playerList!!)
                            .putExtra("groupNumber", numberGroups)
                            .putExtra("mode", "group")
                    startActivity(intent)
                } else {
                    showErrorAlert("O número digitado não é divisível pelo número de itens na lista")
                }
            } else {
                showErrorAlert("O número de grupos não pode ser maior que os itens da lista")
            }
        } else {
            showErrorAlert("Você não digitou o número de grupos")
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
