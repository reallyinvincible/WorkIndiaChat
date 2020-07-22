package com.exuberant.workindiachat.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.exuberant.workindiachat.R
import com.exuberant.workindiachat.data.model.CallHistory
import com.exuberant.workindiachat.data.model.State
import kotlinx.android.synthetic.main.list_item_call_history.view.*
import kotlinx.android.synthetic.main.list_item_chat.view.iv_user_picture
import kotlinx.android.synthetic.main.list_item_chat.view.tv_message
import kotlinx.android.synthetic.main.list_item_chat.view.tv_user_name

class CallHistoryAdapter(
    val callHistoryActivity: CallHistoryActivity, val name: String,
    val faceId: Int,
    val callHistoryList: List<CallHistory>
) : RecyclerView.Adapter<CallHistoryAdapter.CallHistoryViewHolder>() {

    class CallHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userProfileImageView = itemView.iv_user_picture
        val userNameTextView = itemView.tv_user_name
        val userMessageTextView = itemView.tv_message
        val phoneStateImageView = itemView.iv_call_action

        fun bindCallHistory(
            callHistoryActivity: CallHistoryActivity,
            name: String,
            faceId: Int,
            callHistory: CallHistory
        ) {
            Glide.with(callHistoryActivity)
                .load(faceId)
                .into(userProfileImageView)
            userNameTextView.text = name
            userMessageTextView.text = when (callHistory.state) {
                State.INCOMING -> {
                    "You received a call at ${callHistory.time}"
                }
                State.OUTGOING -> {
                    "You called at ${callHistory.time}"
                }
                State.MISSED -> {
                    "You missed a call at ${callHistory.time}"
                }
            }
            val iconId = when (callHistory.state) {
                State.INCOMING -> {
                    R.drawable.ic_phone_received
                }
                State.OUTGOING -> {
                    R.drawable.ic_dialed
                }
                State.MISSED -> {
                    R.drawable.ic_phone_missed
                }
            }
            phoneStateImageView.setImageResource(iconId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_call_history, parent, false)
        return CallHistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return callHistoryList.size
    }

    override fun onBindViewHolder(holder: CallHistoryViewHolder, position: Int) {
        holder.bindCallHistory(callHistoryActivity, name, faceId, callHistoryList[position])
    }

}