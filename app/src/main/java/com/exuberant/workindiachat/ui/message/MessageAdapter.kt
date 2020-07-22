package com.exuberant.workindiachat.ui.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exuberant.workindiachat.R
import com.exuberant.workindiachat.data.model.Message
import kotlinx.android.synthetic.main.list_item_chat_bubble.view.*

class MessageAdapter(val messageList: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView = itemView.tv_message
        val timeTextView = itemView.tv_time

        fun bindMessage(message: Message) {
            messageTextView.text = message.message
            timeTextView.text = message.time
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_chat_bubble, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bindMessage(messageList[position])
    }

}