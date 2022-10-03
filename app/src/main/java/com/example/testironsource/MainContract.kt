package com.example.testironsource

interface MainContract {

    interface View{

        fun initialize()

        fun onActionButtonSetClickListener(clickListener: android.view.View.OnClickListener)

        fun onAnimationAction()

        fun onShowToastMessageAction()

        fun onShowToastMessage(msg: String)

        fun onCallAction()

        fun onNotificationAction()
    }

    interface Presenter{
        fun getActionsFromRemote()
    }
}