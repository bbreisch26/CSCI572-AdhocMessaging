package com.csci572.adhocmessaging

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


/**
 * A BroadcastReceiver that notifies of important Wi-Fi p2p events.
 */
class WiFiDirectBroadcastReceiver(
    private val manager: WifiP2pManager?,
    private val channel: WifiP2pManager.Channel,
    private val p2papp: MyWifiP2PApp
) : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onReceive(context: Context, intent: Intent) {
        val action: String? = intent.action
        when (action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                when (state) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        // Wifi P2P is enabled
                        Log.v("BroadcastReceiver", "WiFi P2P Enabled")
                        val networkP2pInfo: WifiP2pInfo? = intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_INFO)
                        Log.v("networkP2pinfo", networkP2pInfo.toString())
                    }
                    else -> {
                        // Wi-Fi P2P is not enabled
                    }
                }
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                Log.v("BroadcastReceiver", "Called P2P PEERS CHANGED ACTION")
                manager?.requestPeers(channel, WifiP2pManager.PeerListListener { peers ->
                    // Perform action with peers
                    // May not be applicable now that we are using service discovery
                    p2papp.peerList = peers
                })
            }
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Respond to new connection or disconnections
                Log.v("BroadCast", "WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION")
                //WifiP2pManager.EXTRA_NETWORK_INFO

                val networkInfo: NetworkInfo? = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO)
                Log.v("NetworkInfo", networkInfo.toString())
                manager?.requestConnectionInfo(channel, p2papp.connectionInfoListener)
                manager?.requestNetworkInfo(channel, object : WifiP2pManager.NetworkInfoListener {
                    override fun onNetworkInfoAvailable(networkInfo: NetworkInfo) {
                        Log.v("MainActivity", "Network Info Available")
                        Log.v("MainActivity", networkInfo.toString()) // TODO: WifiP2pInfo should not be empty
                    }
                })
            }
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // Respond to this device's wifi state changing
            }
            else -> {
                if (action != null) {
                    Log.v("MainActivity",action)
                }
            }
        }
    }
}