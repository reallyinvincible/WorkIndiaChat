package com.exuberant.workindiachat.ui.home.calls

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exuberant.workindiachat.R
import com.exuberant.workindiachat.data.model.Chat
import com.exuberant.workindiachat.ui.history.CallHistoryActivity
import com.exuberant.workindiachat.ui.home.HomeActivity
import com.exuberant.workindiachat.ui.message.MessageActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.list_item_chat.view.*


class CallAdapter(var callList: List<Chat>, val homeActivity: HomeActivity) :
    RecyclerView.Adapter<CallAdapter.CallViewHolder>() {

    class CallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userProfileImageView = itemView.iv_user_picture
        val userNameTextView = itemView.tv_user_name
        val userMessageTextView = itemView.tv_message

        fun bindChat(
            chat: Chat,
            homeActivity: HomeActivity
        ) {
            Glide.with(homeActivity)
                .load(chat.imageId)
                .into(userProfileImageView)
            userNameTextView.text = chat.userName
            userMessageTextView.text = "Last call was on ${chat.time}"
            userProfileImageView.setOnClickListener {
                val intent = Intent(homeActivity, CallHistoryActivity::class.java)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_call, parent, false)
        return CallViewHolder(view)
    }

    override fun getItemCount(): Int {
        return callList.size
    }

    override fun onBindViewHolder(holder: CallViewHolder, position: Int) {
        holder.bindChat(callList[position], homeActivity)
    }

}