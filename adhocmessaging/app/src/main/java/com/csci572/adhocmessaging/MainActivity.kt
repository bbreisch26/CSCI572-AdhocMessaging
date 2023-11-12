package com.csci572.adhocmessaging

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.csci572.adhocmessaging.ui.theme.AdhocmessagingTheme

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.csci572.adhocmessaging.ui.theme.Blue80

import com.csci572.adhocmessaging.WiFiDirectBroadcastReceiver

class MainActivity : ComponentActivity() {
    val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    var channel: WifiP2pManager.Channel? = null
    var receiver: WiFiDirectBroadcastReceiver? = null
    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    // List of nearby devices
    var peerList : WifiP2pDeviceList? = null
    //
    private val peerListListener = WifiP2pManager.PeerListListener { peers ->
        peerList = peers
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        channel = manager?.initialize(this, mainLooper, null)
        channel?.also { channel ->
            receiver = WiFiDirectBroadcastReceiver(manager, channel, this, peerListListener)
        }

        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {
                // TODO Insert Success logic
            }

            override fun onFailure(reasonCode: Int) {
                // TODO Insert failure logic
            }
        })

        setContent {
            AdhocmessagingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Blue80
                ) {
                    Navigation(this)
                }
            }
        }
    }

    /* register the broadcast receiver with the intent values to be matched */
    override fun onResume() {
        super.onResume()
        receiver?.also { receiver ->
            registerReceiver(receiver, intentFilter)
        }
    }

    /* unregister the broadcast receiver */
    override fun onPause() {
        super.onPause()
        receiver?.also { receiver ->
            unregisterReceiver(receiver)
        }
    }

    fun connectToPeer(address : String) {
        val config = WifiP2pConfig()
        config.deviceAddress = address
        channel?.also { channel ->
            manager?.connect(channel, config, object : WifiP2pManager.ActionListener {

                override fun onSuccess() {
                    //success logic
                }

                override fun onFailure(reason: Int) {
                    //failure logic
                }
            })
        }
    }
}


