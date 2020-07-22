package com.exuberant.workindiachat.ui.home.chats

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exuberant.workindiachat.R
import com.exuberant.workindiachat.data.model.Chat
import com.exuberant.workindiachat.ui.home.HomeActivity
import com.exuberant.workindiachat.ui.message.MessageActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.list_item_chat.view.*
import java.util.*
import kotlin.math.max

class ChatAdapter(var chatList: List<Chat>, val homeActivity: HomeActivity) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userProfileImageView = itemView.iv_user_picture
        val userNameTextView = itemView.tv_user_name
        val userMessageTextView = itemView.tv_message
        val badgeTextView = itemView.tv_badge
        val chatTimeTextView = itemView.tv_chat_time

        fun bindChat(
            chat: Chat,
            homeActivity: HomeActivity,
            position: Int
        ) {
            Glide.with(homeActivity)
                .load(chat.imageId)
                .into(userProfileImageView)
            userNameTextView.text = chat.userName
            userMessageTextView.text = chat.message
            if (position == 0 && homeActivity.bool1) {
                badgeTextView.visibility = View.VISIBLE
                badgeTextView.text = max(2, Random().nextInt(15)).toString()
            } else if (position == 1 && homeActivity.bool2) {
                badgeTextView.visibility = View.VISIBLE
                badgeTextView.text = max(2, Random().nextInt(15)).toString()
            } else {
                badgeTextView.visibility = View.GONE
            }
            chatTimeTextView.text = chat.time
            userProfileImageView.setOnClickListener {
                if (position == 0) {
                    homeActivity.bool1 = false
                }
                if (position == 1) {
                    homeActivity.bool2 = false
                }
                val intent = Intent(homeActivity, MessageActivity::class.java)
                val gson = Gson()
                intent.putExtra("user", gson.toJson(chat))
                val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        homeActivity,
                        itemView.iv_user_picture as View,
                        "profile_picture"
                    )
                homeActivity.startActivity(intent, options.toBundle())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bindChat(chatList[position], homeActivity, position)
    }

}