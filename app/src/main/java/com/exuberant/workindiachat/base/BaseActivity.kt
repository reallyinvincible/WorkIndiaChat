package com.exuberant.workindiachat.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

open class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: BaseProgressDialog
    private lateinit var internetLostDialog: BaseInternetDialog
    private val isInternetAvailable = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = BaseProgressDialog(this)
        internetLostDialog = BaseInternetDialog(this)
        EventBus.getDefault().register(this)
        setupNetworkListener()
    }

    fun hideSoftKeyBoard() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isAcceptingText) {
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    fun showProgressDialog() {
        if (!isFinishing && progressDialog != null) {
            progressDialog.showDialog()
        }
    }

    fun hideProgressDialog() {
        if (isFinishing) {
            return
        }
        if (progressDialog != null) {
            progressDialog.hideDialog()
        }
    }

    private fun showInternetLostDialog() {
        if (!isFinishing && internetLostDialog != null) {
            internetLostDialog.showInternetLostDialog()
        }
    }

    private fun hideInternetLostDialog() {
        if (isFinishing) {
            return
        }
        if (internetLostDialog != null) {
            internetLostDialog.hideInternetLostDialog()
        }
    }

    private fun setupNetworkListener() {
        val cm =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCallback: NetworkCallback = object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                isInternetAvailable.postValue(true)
            }

            override fun onLost(network: Network) {
                isInternetAvailable.postValue(false)
            }

            override fun onUnavailable() {
                isInternetAvailable.postValue(false)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(networkCallback)
        }
        listenForInternetChanges()
    }

    private fun listenForInternetChanges() {
        isInternetAvailable.observe(this, Observer {
            when (it) {
                false -> showInternetLostDialog()
                else -> hideInternetLostDialog()
            }
        })
    }

    @Subscribe
    open fun fakeEventListener(event: String?) {
        // Do Nothing
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}