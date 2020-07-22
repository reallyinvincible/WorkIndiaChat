package com.exuberant.workindiachat.ui.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.exuberant.workindiachat.R
import com.exuberant.workindiachat.data.model.Chat
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_message.*
import kotlinx.android.synthetic.main.list_item_call.*
import kotlinx.android.synthetic.main.list_item_call.iv_user_picture
import kotlinx.android.synthetic.main.list_item_call.tv_user_name

class MessageActivity : AppCompatActivity() {
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
}