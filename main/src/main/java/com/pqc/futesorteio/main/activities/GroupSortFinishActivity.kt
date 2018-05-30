package com.pqc.futesorteio.main.activities

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.pqc.futesorteio.main.R
import kotlinx.android.synthetic.main.activity_group_sort_finish.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class GroupSortFinishActivity : AppCompatActivity() {

    public enum class ViewToSet {
        LOADING, MAIN
    }

    private var listResult: ArrayList<String> = ArrayList<String>()

    private var teamList: ArrayList<String> = ArrayList<String>()
    private var playerList: ArrayList<String> = ArrayList<String>()
    private var playerOneList: ArrayList<String> = ArrayList<String>()
    private var playerTwoList: ArrayList<String> = ArrayList<String>()
    private var numberGroups: Int = 0
    private var mode: String = ""
    private var resultSort: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_sort_finish)
    }

    override fun onResume() {
        super.onResume()
        initiateListeners()
    }

    private fun initiateListeners() {
        mode = intent.getStringExtra("mode")

        if(mode.equals("vs")){
            playerOneList = intent.getStringArrayListExtra("listPlayerOne")
            playerTwoList = intent.getStringArrayListExtra("listPlayerTwo")
        } else {
            numberGroups = intent.getIntExtra("groupNumber", 0)
            teamList = intent.getStringArrayListExtra("listTeam")
            playerList = intent.getStringArrayListExtra("listPlayer")
        }

        btn_back.setOnClickListener { back() }
        btn_sort.setOnClickListener { doSort() }
        btn_new.setOnClickListener { newSort() }
        btn_share.setOnClickListener { shareResult() }
    }

    private fun showView(option: ViewToSet) {
        when (option) {
            ViewToSet.LOADING -> {
                layout_content.visibility = View.INVISIBLE
                progress_bar_loading.visibility = View.VISIBLE
            }
            ViewToSet.MAIN -> {
                layout_content.visibility = View.VISIBLE
                progress_bar_loading.visibility = View.GONE
            }
        }
    }

    private fun back() {
        finish()
    }

    private fun doSort() {
        showView(ViewToSet.LOADING)

        if(mode.equals("vs")){
            vsSort()
        } else {
            groupSort()
        }

        loadAdapter()
        setVisibilityButtons()
        showView(ViewToSet.MAIN)
    }

    private fun setVisibilityButtons() {
        if(list_result.count > 0) {
            layout_buttons.visibility = View.VISIBLE
        } else {
            layout_buttons.visibility = View.INVISIBLE
        }
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

    private fun vsSort() {
        listResult = ArrayList<String>()

        val totalPlayerOneList = playerOneList.size
        val totalPlayerTwoList = playerTwoList.size

        var listPositionPlayerOne: ArrayList<Int> = ArrayList<Int>()
        var listPositionPlayerTwo: ArrayList<Int> = ArrayList<Int>()

        var random: Random = Random()
        var randomNumber: Int
        var increment: Int = 0

        val totalListaMenor = if(totalPlayerOneList > totalPlayerTwoList) totalPlayerTwoList else totalPlayerOneList

        while (increment < totalListaMenor) {
            randomNumber = random.nextInt(totalPlayerOneList)

            if(!listPositionPlayerOne.contains(randomNumber)) {
                listPositionPlayerOne.add(randomNumber)
                increment++
            }
        }

        increment = 0

        while (increment < totalListaMenor) {
            randomNumber = random.nextInt(totalPlayerTwoList)

            if(!listPositionPlayerTwo.contains(randomNumber)) {
                listPositionPlayerTwo.add(randomNumber)
                increment++
            }
        }

        for(item in 0..(totalListaMenor - 1)) {
            resultSort += "${playerOneList[listPositionPlayerOne[item]].toUpperCase().trim()} x ${playerTwoList[listPositionPlayerTwo[item]].toUpperCase().trim()} \n "
            listResult.add("${playerOneList[listPositionPlayerOne[item]].toUpperCase().trim()} x ${playerTwoList[listPositionPlayerTwo[item]].toUpperCase().trim()}")
        }
    }

    private fun groupSort() {
        listResult = ArrayList<String>()

        val totalListTeam = teamList.size
        val totalPlayerList = playerList.size
        val groupCount = numberGroups
        var repeatNumber: Double
        var listGroup: ArrayList<Int> = ArrayList<Int>()

        var listPositionTeam: ArrayList<Int> = ArrayList<Int>()
        var listPositionPlayer: ArrayList<Int> = ArrayList<Int>()
        var listPositionGroup: ArrayList<Int> = ArrayList<Int>()


        var random: Random = Random()
        var randomNumber: Int
        var increment: Int = 0

        val totalListaMenor = if(totalListTeam > totalPlayerList) totalPlayerList else totalListTeam

        repeatNumber = (totalListaMenor / groupCount).toDouble()

        for(item in 0..(numberGroups - 1)) {
            for(repeatItem in 0..(repeatNumber.roundToInt() - 1)){
                listGroup.add(item)
            }
        }

        while (increment < totalListaMenor) {
            randomNumber = random.nextInt(totalListTeam)

            if(!listPositionTeam.contains(randomNumber)) {
                listPositionTeam.add(randomNumber)
                increment++
            }
        }

        increment = 0

        while (increment < totalListaMenor) {
            randomNumber = random.nextInt(totalPlayerList)

            if(!listPositionPlayer.contains(randomNumber)) {
                listPositionPlayer.add(randomNumber)
                increment++
            }
        }

        increment = 0

        while (increment < totalListaMenor) {
            randomNumber = random.nextInt(totalListaMenor)

            if(!listPositionGroup.contains(randomNumber)) {
                listPositionGroup.add(randomNumber)
                increment++
            }
        }

        for(item in 0..(totalListaMenor - 1)) {
            resultSort += "${playerList[listPositionPlayer[item]].toUpperCase().trim()} -> ${teamList[listPositionTeam[item]].toUpperCase().trim()} -> GRUPO ${(listGroup[listPositionGroup[item]] + 1).toString()} \n "
            listResult.add("${playerList[listPositionPlayer[item]].toUpperCase().trim()} -> ${teamList[listPositionTeam[item]].toUpperCase().trim()} -> GRUPO ${(listGroup[listPositionGroup[item]] + 1).toString()}")
        }
    }

    private fun loadAdapter() {
        list_result.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listResult!!)
    }

    private fun shareResult() {
        val sdf = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm 'hs'")
        val currentDate = sdf.format(Date())

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Resultado do Sorteio: \n\n ${resultSort} \n Realizado em ${ currentDate }")
        startActivity(Intent.createChooser(shareIntent,"Resultado do sorteio"))
    }
}
