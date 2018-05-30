package com.pqc.futesorteio.main.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.pqc.futesorteio.main.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        initiateListeners()
    }

    private fun initiateListeners() {
        button_all.setOnClickListener { openTeamNameActivity() }
        button_vs.setOnClickListener { openPlayerNameOneActivity() }
    }

    private fun openTeamNameActivity() {
        val intent = Intent(this, TeamNameActivity::class.java)
        startActivity(intent)
    }

    private fun openPlayerNameOneActivity() {
        val intent = Intent(this, PlayerNameOneActivity::class.java)
        startActivity(intent)
    }
}
