package com.example.testironsource

import com.example.testironsource.model.Actions
import com.example.testironsource.model.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.fixedRateTimer

class MainPresenter(private val view: MainContract.View) :
    MainContract.Presenter,
    Callback<List<Actions>> {

    private var coolDownPeriod = 0L

    init {
        view.initialize()
    }

    override fun getActionsFromRemote() {
        val call: Call<List<Actions>>? = RetrofitClient.instance?.getMyApi()?.getActions()
        call?.enqueue(this)
    }

    override fun onResponse(call: Call<List<Actions>>, response: Response<List<Actions>>) {
        val list = response.body()
        var highPriority = 0

        val chosenActions: MutableList<Int> = mutableListOf()

        val dayFormat = SimpleDateFormat("u", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val weekDay = dayFormat.format(calendar.time).toInt()

        list?.forEachIndexed { index, actions ->

            val validDay = actions.validDays.contains(weekDay)

            if(highPriority < actions.priority && actions.enabled && validDay){

                highPriority = actions.priority
                chosenActions.clear()
                chosenActions.add(index)
            }
            else if(highPriority == actions.priority && actions.enabled && validDay){
                chosenActions.add(index)
            }
        }

        fun getActionToRun(index: Int) = list?.get(index)

        view.onActionButtonSetClickListener {

            val type = if (chosenActions.isEmpty()) "none"
                else if (chosenActions.size == 1) {
                    getActionToRun(chosenActions[0])?.type
                } else {
                    val random = Random()
                    val randomIndex = random.nextInt(chosenActions.size)
                    getActionToRun(chosenActions[randomIndex])?.type
                }

                when (type) {
                    "animation" -> view.onAnimationAction()
                    "toast" -> view.onShowToastMessageAction()
                    "call" -> view.onCallAction()
                    "notification" -> view.onNotificationAction()
                    "none" -> {}
                    else -> {}
                }
        }
    }

    override fun onFailure(call: Call<List<Actions>>, t: Throwable) {
        t.message?.let { view.onShowToastMessage(it) }
    }

}