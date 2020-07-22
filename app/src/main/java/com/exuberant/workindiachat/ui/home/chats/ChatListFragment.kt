package com.exuberant.workindiachat.ui.home.chats

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.exuberant.workindiachat.R
import com.exuberant.workindiachat.data.model.Chat
import com.exuberant.workindiachat.ui.home.HomeActivity
import com.exuberant.workindiachat.util.OnSwipeTouchListener
import kotlinx.android.synthetic.main.fragment_chat_list.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max


class ChatListFragment : Fragment() {

    private var searchText: String? = null

    private lateinit var homeActivity: HomeActivity
    val faceListFemale = mutableListOf(
        R.drawable.face_1,
        R.drawable.face_3,
        R.drawable.face_10,
        R.drawable.face_11,
        R.drawable.face_12,
        R.drawable.face_13,
        R.drawable.face_14
    )
    val faceListMale =
        mutableListOf(
            R.drawable.face_2, R.drawable.face_4,
            R.drawable.face_5, R.drawable.face_6, R.drawable.face_7, R.drawable.face_8,
            R.drawable.face_9
        )
    val chats = mutableListOf<Chat>()
    private lateinit var messageList: List<String>
    private lateinit var maleNameList: List<String>
    private lateinit var femaleNameList: List<String>
    private var adapter: ChatAdapter? = null

    var time = Calendar.getInstance().timeInMillis

    override fun onAttach(context: Context) {
        super.onAttach(context)
        homeActivity = context as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeActivity.searchText.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it.isNullOrEmpty()) {
                searchText = ""
                setFakeData()
            } else {
                searchText = it
                setFakeData()
            }
        })
        initialize()
    }

    private fun initialize() {
        rv_chat_list.setOnTouchListener(object : OnSwipeTouchListener(homeActivity) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                homeActivity.isMessageSelected.postValue(false)
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                //Do nothing
            }
        })
        messageList = resources.getStringArray(R.array.messages).toList()
        maleNameList = resources.getStringArray(R.array.name_male).toList()
        femaleNameList = resources.getStringArray(R.array.name_female).toList()
        setFakeData()
        setListener()
    }

    private fun setListener() {
        rv_chat_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    homeActivity.showPaging()
                    Handler().postDelayed({
                        setFakeData()
                        adapter?.notifyDataSetChanged()
                        homeActivity.hidePaging()
                    }, 2000)
                }
            }
        })
    }

    private fun setFakeData() {
        for (i in 0..16) {
            val rand = Random().nextInt(7)
            var message = messageList[rand]
            if (i < 2) {
                message = messageList[max(2, rand)]
            }
            var randomTime = Random().nextInt(5000)
            time = time - (randomTime * 60 * 1000)
            if (rand % 2 == 0) {
                val randSelector = Random().nextInt(7)
                val chat =
                    Chat(
                        faceListMale[randSelector],
                        maleNameList[rand],
                        message,
                        convertTimeToString(time)
                    )
                chats.add(chat)
            } else {
                val randSelector = Random().nextInt(7)
                val chat =
                    Chat(
                        faceListFemale[randSelector],
                        femaleNameList[rand],
                        message,
                        convertTimeToString(time)
                    )
                chats.add(chat)
            }
        }
        if (adapter == null) {
            setAdapter(chats)
        }
        filter(searchText)
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

    private fun setAdapter(chats: MutableList<Chat>) {
        adapter = ChatAdapter(chats, homeActivity)
        rv_chat_list.adapter = adapter
    }

    private fun filter(text: String?) {
        val temp: MutableList<Chat> = ArrayList()
        for (d in chats) {
            val name = d.userName
            if (name.contains(text.toString())) {
                temp.add(d)
            }
        }
        adapter?.chatList = temp
        adapter?.notifyDataSetChanged()
    }

}