package com.example.testironsource

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.testironsource.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import java.util.*

class MainActivity :
    AppCompatActivity(),
    MainContract.View {

    // UI references
    private lateinit var binding: ActivityMainBinding
    private lateinit var button: Button

    // Presenter reference
    private lateinit var mainPresenter: MainPresenter // NOTE: May also be injected by DI (Hilt)

    private val service = ActionNotificationService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        service.obtainNotificationManager()

        mainPresenter = MainPresenter(this)

        mainPresenter.getActionsFromRemote()

        setContentView(binding.root)
    }

    override fun initialize() {
        button = binding.actionButton
    }

    override fun onActionButtonSetClickListener(clickListener: View.OnClickListener) {
        button.setOnClickListener(clickListener)
    }

    override fun onAnimationAction(){
        button.animate().rotation(360.0f).duration = 1000
    }

    override fun onShowToastMessageAction(){
        Toast.makeText(this, R.string.action_is_toast, Toast.LENGTH_LONG).show()
    }

    override fun onShowToastMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onCallAction(){
        val intent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"))
        intent.setDataAndType(null, ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE)
        startActivity(intent)
    }

    override fun onNotificationAction(){
        service.showNotification()
    }
}