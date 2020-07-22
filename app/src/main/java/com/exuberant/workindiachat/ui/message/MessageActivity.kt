package com.exuberant.workindiachat.ui.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.exuberant.workindiachat.R
import com.exuberant.workindiachat.data.model.Chat
import com.exuberant.workindiachat.data.model.Message
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.list_item_call.*
import kotlinx.android.synthetic.main.list_item_call.iv_user_picture
import kotlinx.android.synthetic.main.list_item_call.tv_user_name
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MessageActivity : AppCompatActivity() {

    private val messageList = mutableListOf<Message>()
    private var adapter: MessageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setInitialData()
        initialize()
    }

    private fun initialize() {
        ib_back.setOnClickListener {
            finishAfterTransition()
        }
        rv_message_list.adapter = adapter
        btn_send.setOnClickListener {
            val text = et_send_message.text.toString()
            if (!text.isNullOrEmpty()) {
                val message =
                    Message(text, convertTimeToString(Calendar.getInstance().timeInMillis))
                messageList.add(message)
                et_send_message.setText(null)
                if (adapter == null) {
                    adapter = MessageAdapter(messageList)
                    rv_message_list.adapter = adapter
                } else {
                    adapter!!.notifyDataSetChanged()
                }
            }
        }
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