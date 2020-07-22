package com.exuberant.workindiachat.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.transition.Fade
import androidx.transition.Slide
import com.exuberant.workindiachat.R
import com.exuberant.workindiachat.ui.home.calls.CallFragment
import com.exuberant.workindiachat.ui.home.chats.ChatListFragment
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class HomeActivity : AppCompatActivity() {

    var bool1 = true
    var bool2 = true

    val isMessageSelected = MutableLiveData<Boolean>()
    private val isSearching = MutableLiveData<Boolean>()
    val searchText = MutableLiveData<String?>()
    private var isButtonActivated = false
    private val textWatcher: TextWatcher = object : TextWatcher {
        private var timer = Timer()
        private val DELAY: Long = 500L
        override fun afterTextChanged(s: Editable?) {
            timer.cancel()
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    searchText.postValue(s.toString())
                }
            }, DELAY)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        isSearching.postValue(false)
        isMessageSelected.postValue(true)
        searchText.postValue(null)
        initializeUi()
    }

    private fun initializeUi() {

        isMessageSelected.observe(this, androidx.lifecycle.Observer {
            if (it) {
                tv_message_control.textSize = 24f
                tv_message_control.alpha = 1f
                tv_call_control.textSize = 16f
                tv_call_control.alpha = 0.7f
                switchToChatList()
            } else {
                tv_message_control.textSize = 16f
                tv_message_control.alpha = 0.7f
                tv_call_control.textSize = 24f
                tv_call_control.alpha = 1f
                switchToCalls()
            }
        })
        isSearching.observe(this, androidx.lifecycle.Observer {
            if (it) {
                et_search.visibility = View.VISIBLE
                ib_search.setImageResource(R.drawable.ic_close)
            } else {
                et_search.text = null
                searchText.postValue(null)
                et_search.visibility = View.GONE
                ib_search.setImageResource(R.drawable.ic_search)
            }
        })
        ib_search.setOnClickListener {
            isButtonActivated = !isButtonActivated
            if (isButtonActivated) {
                isSearching.postValue(true)
            } else {
                isSearching.postValue(false)
            }
        }
        tv_message_control.setOnClickListener {
            isMessageSelected.postValue(true)
        }
        tv_call_control.setOnClickListener {
            isMessageSelected.postValue(false)
        }

        et_search.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchText.postValue(textView.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        et_search.addTextChangedListener(textWatcher)

    }

    private fun switchFragment(fragment: Fragment) {
        fragment.enterTransition = Fade()
        fragment.exitTransition = Fade()
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, fragment).commit()
    }

    private fun switchToCalls() {
        val fragment = CallFragment()
        fragment.enterTransition = Slide(Gravity.RIGHT)
        fragment.exitTransition = Slide(Gravity.RIGHT)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, fragment).commit()
    }

    private fun switchToChatList() {
        val fragment = ChatListFragment()
        fragment.enterTransition = Slide(Gravity.LEFT)
        fragment.exitTransition = Slide(Gravity.LEFT)
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fl_fragment_container, fragment).commit()
    }

    fun showPaging() {
        paging.visibility = View.VISIBLE
    }

    fun hidePaging() {
        paging.visibility = View.GONE
    }

}