package com.example.testironsource

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainPresenter(private val view: MainContract.View) :
    MainContract.Presenter,
    Callback<List<Actions>> {

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

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]

        list?.forEachIndexed { index, actions ->

            val validDay = actions.validDays.contains(dayOfWeek)

            if(highPriority < actions.priority && actions.enabled && validDay){

                highPriority = actions.priority
                chosenActions.add(index)
            }
            else if(highPriority == actions.priority && actions.enabled && validDay){
                chosenActions.add(index)
            }
        }

        fun getActionToRun(index: Int) = list?.get(chosenActions[index])

        view.onActionButtonSetClickListener {

            val type = if(chosenActions.isEmpty()) "none"
            else if(chosenActions.size == 1){
                getActionToRun(chosenActions[0])?.type
            } else {
                val random = Random()
                getActionToRun(random.nextInt(chosenActions.size))?.type
            }

            when(type){
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