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
        button_head_tail.setOnClickListener { openHeadTailsActivity() }
        button_team_player.setOnClickListener { openNameRealActivity() }
        button_team_group.setOnClickListener { openTeamGroupActivity() }
    }

    private fun openTeamNameActivity() {
        val intent = Intent(this, TeamNameActivity::class.java)
        startActivity(intent)
    }

    private fun openPlayerNameOneActivity() {
        val intent = Intent(this, PlayerNameOneActivity::class.java)
        startActivity(intent)
    }

    private fun openHeadTailsActivity() {
        val intent = Intent(this, HeadsTailsActivity::class.java)
        startActivity(intent)
    }

    private fun openNameRealActivity() {
        val intent = Intent(this, NameRealActivity::class.java)
        startActivity(intent)
    }

    private fun openTeamGroupActivity() {
        val intent = Intent(this, TeamGroupActivity::class.java)
        startActivity(intent)
    }
}
