package com.exuberant.workindiachat.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.exuberant.workindiachat.R
import com.exuberant.workindiachat.data.model.CallHistory
import com.exuberant.workindiachat.data.model.Chat
import com.exuberant.workindiachat.data.model.State
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_call_history.*
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.activity_message.ib_back
import kotlinx.android.synthetic.main.activity_message.iv_user_picture
import kotlinx.android.synthetic.main.activity_message.tv_user_name
import kotlinx.android.synthetic.main.list_item_call.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CallHistoryActivity : AppCompatActivity() {

    private val callHistoryList = mutableListOf<CallHistory>()
    var time = Calendar.getInstance().timeInMillis

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_history)
        initialize()
    }

    private fun initialize() {
        ib_back.setOnClickListener {
            finishAfterTransition()
        }
        setInitialData()
    }

    private fun setFakeData(name: String, faceId: Int) {
        for (i in 1..20) {
            var randomTime = Random().nextInt(5000)
            time = time - (randomTime * 60 * 1000)
            var rand = Random().nextInt(3)
            val state = when (rand) {
                0 -> State.MISSED
                1 -> State.INCOMING
                else -> State.OUTGOING
            }
            val callHistory = CallHistory(state, convertTimeToString(time))
            callHistoryList.add(callHistory)
        }
        val adapter = CallHistoryAdapter(this, name, faceId, callHistoryList)
        rv_call_history_list.adapter = adapter
    }

    private fun setInitialData() {
        val intent = intent
        val gson = Gson()
        val userString = intent.getStringExtra("user")
        val chat = gson.fromJson<Chat>(userString, Chat::class.java)
        Glide.with(this)
            .load(chat.imageId)
            .into(iv_user_picture)
        tv_user_name.text = chat.userName
        setFakeData(chat.userName, chat.imageId)
    }

    fun convertTimeToString(timeInMillis: Long): String {
        val currentTime = Calendar.getInstance().timeInMillis
        val millis = currentTime - timeInMillis
        val hours: Long = millis / (1000 * 60 * 60)
        var simple: DateFormat = SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z")
        if (hours < 24) {
            simple = SimpleDateFormat("HH:mm")
        } else if (hours < (24 * 7)) {
            simple = SimpleDateFormat("EEE")
        } else {
            simple = SimpleDateFormat("dd-MMM")
        }
        val result = Date(timeInMillis)
        return simple.format(result)
    }
}