package com.pqc.futesorteio.main.activities.activities

import android.app.AlertDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.pqc.futesorteio.main.R
import com.pqc.futesorteio.main.activities.model.Result
import com.pqc.futesorteio.main.activities.utils.hide
import com.pqc.futesorteio.main.activities.utils.invisible
import com.pqc.futesorteio.main.activities.utils.show
import kotlinx.android.synthetic.main.activity_group_sort_finish.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

class GroupSortFinishActivity : AppCompatActivity() {

    enum class ViewToSet {
        LOADING, MAIN
    }

    private var teamList: ArrayList<String> = ArrayList()
    private var playerList: ArrayList<String> = ArrayList()
    private var playerOneList: ArrayList<String> = ArrayList()
    private var playerTwoList: ArrayList<String> = ArrayList()
    private var playersTeamCount: Int = 0
    private var mode: String = ""

    private var resultSortList: ArrayList<Result> = ArrayList()

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
        } else if (mode.equals("team_real")) {
            playersTeamCount = intent.getIntExtra("groupNumber", 0)
            playerList = intent.getStringArrayListExtra("listPlayer")
        } else if (mode.equals("team_real_group")) {
            playersTeamCount = intent.getIntExtra("groupNumber", 0)
            teamList = intent.getStringArrayListExtra("listTeam")
        } else {
            playersTeamCount = intent.getIntExtra("groupNumber", 0)
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
                layout_content.invisible()
                progress_bar_loading.show()
            }
            ViewToSet.MAIN -> {
                layout_content.show()
                progress_bar_loading.hide()
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
        } else if (mode.equals("team_real")) {
            teamReal()
        } else if (mode.equals("team_real_group")) {
            teamRealGroup()
        } else {
            groupSort()
        }

        loadAdapter()
        setVisibilityButtons()
        showView(ViewToSet.MAIN)
    }

    private fun setVisibilityButtons() {
        if(list_result.count > 0) {
            layout_buttons.show()
        } else {
            layout_buttons.invisible()
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
        resultSortList = ArrayList()

        val totalPlayerOneList = playerOneList.size
        val totalPlayerTwoList = playerTwoList.size

        var listPositionPlayerOne: ArrayList<Int> = ArrayList()
        var listPositionPlayerTwo: ArrayList<Int> = ArrayList()

        var random = Random()
        var randomNumber: Int
        var increment = 0

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
            resultSortList.add(
                    Result(
                            0,
                            "${playerOneList[listPositionPlayerOne[item]].toUpperCase().trim()}",
                            "${playerTwoList[listPositionPlayerTwo[item]].toUpperCase().trim()}",
                            ""
                    )
            )
        }
    }

    private fun teamReal() {
        resultSortList= ArrayList()

        val totalPlayerList = playerList.size
        val playersPerTeam = playersTeamCount
        var listGroup: ArrayList<Int> = ArrayList()
        var random = Random()
        var randomNumber: Int
        var listPositionPlayer: ArrayList<Int> = ArrayList()
        var listPositionGroup: ArrayList<Int> = ArrayList()
        var increment = 0
        var repeatNumber: Double
        var completeTeamNumber = 0

        var realNumberPlayers: Int

        repeatNumber = (totalPlayerList / playersPerTeam).toDouble()

        realNumberPlayers = repeatNumber.roundToInt() * playersPerTeam

        for (item in 0..(realNumberPlayers - 1)) {
            if(increment < repeatNumber.roundToInt())
                listGroup.add(increment)
            else {
                if(completeTeamNumber < playersPerTeam) {
                    increment = 0
                    listGroup.add(increment)
                    completeTeamNumber++
                }
                else {
                    break
                }
            }

            increment++
        }

        increment = 0

        while (increment < realNumberPlayers) {
            randomNumber = random.nextInt(totalPlayerList)

            if(!listPositionPlayer.contains(randomNumber) && listPositionPlayer.size <= realNumberPlayers) {
                listPositionPlayer.add(randomNumber)
                increment++
            }
        }

        increment = 0

        while (increment < listGroup.size) {
            randomNumber = random.nextInt(realNumberPlayers)

            if(!listPositionGroup.contains(randomNumber)) {
                listPositionGroup.add(randomNumber)
                increment++
            }
        }

        for(item in 0..(realNumberPlayers - 1)) {
            resultSortList.add(
                    Result(
                            listGroup[listPositionGroup[item]] + 1,
                            "TIME ${(listGroup[listPositionGroup[item]] + 1)}",
                            playerList[listPositionPlayer[item]].toUpperCase().trim(),
                            ""
                    )
            )
        }
    }

    private fun teamRealGroup() {
        resultSortList= ArrayList()

        val totalListTeam = teamList.size
        val groupCount = playersTeamCount
        var repeatNumber: Double
        var listGroup: ArrayList<Int> = ArrayList()

        var listPositionTeam: ArrayList<Int> = ArrayList()
        var listPositionGroup: ArrayList<Int> = ArrayList()


        var random = Random()
        var randomNumber: Int
        var increment = 0

        repeatNumber = (totalListTeam / groupCount).toDouble()

        for(item in 0..(playersTeamCount - 1)) {
            for(repeatItem in 0..(repeatNumber.roundToInt() - 1)){
                listGroup.add(item)
            }
        }

        while (increment < totalListTeam) {
            randomNumber = random.nextInt(totalListTeam)

            if(!listPositionTeam.contains(randomNumber)) {
                listPositionTeam.add(randomNumber)
                increment++
            }
        }

        increment = 0

        while (increment < totalListTeam) {
            randomNumber = random.nextInt(totalListTeam)

            if(!listPositionGroup.contains(randomNumber)) {
                listPositionGroup.add(randomNumber)
                increment++
            }
        }

        for(item in 0..(totalListTeam - 1)) {
            resultSortList.add(
                    Result(
                            listGroup[listPositionGroup[item]] + 1,
                            teamList[listPositionTeam[item]].toUpperCase().trim(),
                            "",
                            "GRUPO ${(listGroup[listPositionGroup[item]] + 1)}"
                    )
            )
        }
    }

    private fun groupSort() {
        resultSortList= ArrayList()

        val totalListTeam = teamList.size
        val totalPlayerList = playerList.size
        val groupCount = playersTeamCount
        var repeatNumber: Double
        var listGroup: ArrayList<Int> = ArrayList()

        var listPositionTeam: ArrayList<Int> = ArrayList()
        var listPositionPlayer: ArrayList<Int> = ArrayList()
        var listPositionGroup: ArrayList<Int> = ArrayList()


        var random = Random()
        var randomNumber: Int
        var increment: Int = 0

        val totalListaMenor = if(totalListTeam > totalPlayerList) totalPlayerList else totalListTeam

        repeatNumber = (totalListaMenor / groupCount).toDouble()

        for(item in 0..(playersTeamCount - 1)) {
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
            resultSortList.add(
                    Result(
                            listGroup[listPositionGroup[item]] + 1,
                            teamList[listPositionTeam[item]].toUpperCase().trim(),
                            playerList[listPositionPlayer[item]].toUpperCase().trim(),
                            "GRUPO ${(listGroup[listPositionGroup[item]] + 1)}"
                    )
            )
        }
    }

    private fun loadAdapter() {
        var sortedList: List<String>

        if(mode.equals("vs")){
            sortedList = resultSortList.sortedWith(compareBy({ it.order })).map { "${it.team} x ${it.name}" }
        } else if (mode.equals("team_real")) {
            sortedList = resultSortList.sortedWith(compareBy({ it.order })).map { "${it.team} -> ${it.name}" }
        } else if (mode.equals("team_real_group")) {
            sortedList = resultSortList.sortedWith(compareBy({ it.order })).map { "${it.group} -> ${it.team}" }
        } else {
            sortedList = resultSortList.sortedWith(compareBy({ it.order })).map { "${it.group} -> ${it.name} -> ${it.team}" }
        }

        list_result.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sortedList)
    }

    private fun shareResult() {
        val sdf = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm 'hs'")
        val currentDate = sdf.format(Date())

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Resultado do Sorteio: \n\n${orderShareResult()} \nRealizado em ${ currentDate }.")
        startActivity(Intent.createChooser(shareIntent,"Resultado do sorteio"))
    }

    private fun orderShareResult(): String{
        var sortedList: List<String>
        var returnResult: String = ""

        if(mode.equals("vs")){
            sortedList = resultSortList.sortedWith(compareBy({ it.order })).map { "${it.team} x ${it.name}" }
        } else if (mode.equals("team_real")) {
            sortedList = resultSortList.sortedWith(compareBy({ it.order })).map { "${it.team} -> ${it.name}" }
        } else if (mode.equals("team_real_group")) {
            sortedList = resultSortList.sortedWith(compareBy({ it.order })).map { "${it.group} -> ${it.team}" }
        } else {
            sortedList = resultSortList.sortedWith(compareBy({ it.order })).map { "${it.group} -> ${it.name} -> ${it.team}" }
        }

        for(item in sortedList) {
            returnResult += "$item \n"
        }

        return returnResult
    }
}
