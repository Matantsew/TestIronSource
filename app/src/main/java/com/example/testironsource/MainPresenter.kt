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
        val list = mutableListOf<Actions>() // response.body()
        list.add(Actions("animation", true, 12, intArrayOf(0,2,5).toList() as ArrayList<Int>, 86400000))
        list.add(Actions("toast", true, 14, intArrayOf(0,5,6).toList() as ArrayList<Int>, 3600000))
        list.add(Actions("call", true, 7, intArrayOf(0,2,4).toList() as ArrayList<Int>, 1000))
        list.add(Actions("notification", true, 10, intArrayOf(0,5).toList() as ArrayList<Int>, 60000))

        var highPriority = 0

        val chosenActions: MutableList<Int> = mutableListOf()

        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]

        list.forEachIndexed { index, actions ->

            val validDay = actions.validDays.contains(dayOfWeek)

            if(highPriority < actions.priority && actions.enabled && validDay){

                highPriority = actions.priority
                chosenActions.clear()
                chosenActions.add(index)
            }
            else if(highPriority == actions.priority && actions.enabled && validDay){
                chosenActions.add(index)
            }
        }

        fun getActionToRun(index: Int) = list[chosenActions[index]]

        view.onActionButtonSetClickListener {

            val type = if(chosenActions.isEmpty()) "none"
            else if(chosenActions.size == 1){
                getActionToRun(chosenActions[0]).type
            } else {
                val random = Random()
                getActionToRun(random.nextInt(chosenActions.size)).type
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