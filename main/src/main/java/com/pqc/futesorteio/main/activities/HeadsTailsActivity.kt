package com.pqc.futesorteio.main.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.pqc.futesorteio.main.R
import kotlinx.android.synthetic.main.activity_heads_tails.*
import java.util.*

class HeadsTailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heads_tails)
    }

    override fun onResume() {
        super.onResume()
        initiateListeners()
    }

    private fun initiateListeners() {
        btn_back.setOnClickListener { back() }
        btn_sort.setOnClickListener { doSort() }
        btn_new.setOnClickListener { newSort() }
    }

    private fun back() {
        finish()
    }

    private fun doSort() {
        val randomNumber = Random().nextInt(2)
        var coinFace: Int

        coinFace = when(randomNumber) {
            0 -> { R.drawable.moeda_cara }
            else -> { R.drawable.moeda_coroa }
        }

        coinId.setImageDrawable(ContextCompat.getDrawable(this, coinFace))

        setVisibilityButtons()
    }

    private fun setVisibilityButtons() {
        layout_buttons.visibility = View.VISIBLE
    }

    private fun newSort() {
        val alert = AlertDialog.Builder(this)

        with(alert){
            setTitle("Atenção")
            setMessage("Deseja finalizar o sorteio atual e começar um novo?")

            setPositiveButton("SIM") {
                dialog, which ->
                dialog.dismiss()
                callMainActivity()
            }

            setNegativeButton("NÃO") {
                dialog, which ->
                dialog.dismiss()
            }

            val dialog = alert.create()
            dialog.show()
        }
    }

    private fun callMainActivity() {
        val intent = Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).putExtra("EXIT", true)
        startActivity(intent)
        finish()
    }
}
