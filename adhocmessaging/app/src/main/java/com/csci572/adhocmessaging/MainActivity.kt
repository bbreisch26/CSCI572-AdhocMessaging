package com.csci572.adhocmessaging

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest
import android.os.Bundle
import android.util.Log
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

//    // List of nearby devices
//    var peerList : WifiP2pDeviceList? = null
//    // Call back function
//    private val peerListListener = WifiP2pManager.PeerListListener { peers ->
//        peerList = peers
//    }

    val servicePeers = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up Wifi-Direct Backend
        channel = manager?.initialize(this, mainLooper, null)
        channel?.also { channel ->
//            receiver = WiFiDirectBroadcastReceiver(manager, channel, this, peerListListener)
        }

        this.registerService()
        this.discoverService()

        var serviceRequest = WifiP2pDnsSdServiceRequest.newInstance()
        manager?.addServiceRequest(
            channel,
            serviceRequest,
            object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    // Success!
                }

                override fun onFailure(code: Int) {
                    // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                }
            }
        )

        manager?.discoverServices(
            channel,
            object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    // Success!
                    Log.v("MainActivity","Successful Discover Services")
                }

                override fun onFailure(code: Int) {
                    Log.v("MainActivity","Failure Discover Services: " + code)
                    // Command failed. Check for P2P_UNSUPPORTED, ERROR, or BUSY
                    when (code) {
                        WifiP2pManager.P2P_UNSUPPORTED -> {
                            Log.d("MainActivity", "Wi-Fi Direct isn't supported on this device.")
                        }
                    }
                }
            }
        )


//        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
//
//            override fun onSuccess() {
//                // TODO Insert Success logic
//                Log.v("MainActivity","Successful Discover Peers")
//            }
//
//            override fun onFailure(reasonCode: Int) {
//                // TODO Insert failure logic
//                Log.v("MainActivity","Failure Discover Peers: " + reasonCode)
//            }
//        })
        //Set up data server to receive messages


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

    private fun registerService() {
        //  Create a string map containing information about your service.
        val record: Map<String, String> = mapOf(
            "listenport" to "8888",
            "buddyname" to "John Doe${(Math.random() * 1000).toInt()}",
            "available" to "visible"
        )

        // Service information.  Pass it an instance name, service type
        // _protocol._transportlayer , and the map containing
        // information other devices will want once they connect to this one.
        val serviceInfo =
            WifiP2pDnsSdServiceInfo.newInstance("_test", "_presence._tcp", record)

        // Add the local service, sending the service info, network channel,
        // and listener that will be used to indicate success or failure of
        // the request.
        manager?.addLocalService(channel, serviceInfo, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                // Unless you want to update the UI or add logging statements.
            }

            override fun onFailure(arg0: Int) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
            }
        })
    }

    private fun discoverService() {
        /* Callback includes:
         * fullDomain: full domain name: e.g. "printer._ipp._tcp.local."
         * record: TXT record dta as a map of key/value pairs.
         * device: The device running the advertised service.
         */
        val txtListener = WifiP2pManager.DnsSdTxtRecordListener { fullDomain, record, device ->
            Log.v("MainActivity","DnsSdTxtRecord available -$record")
            record["buddyname"]?.also {
                servicePeers[device.deviceAddress] = it
            }
        }

        val servListener = WifiP2pManager.DnsSdServiceResponseListener { instanceName, registrationType, resourceType ->
            // Update the device name with the human-friendly version from
            // the DnsTxtRecord, assuming one arrived.
            resourceType.deviceName = servicePeers[resourceType.deviceAddress] ?: resourceType.deviceName

            // Add to the custom adapter defined specifically for showing
            // wifi devices.
//            val fragment = fragmentManager
//                .findFragmentById(R.id.frag_peerlist) as WiFiDirectServicesList
//            (fragment.listAdapter as WiFiDevicesAdapter).apply {
//                add(resourceType)
//                notifyDataSetChanged()
//            }

            Log.v("MainActivity", "onBonjourServiceAvailable $instanceName")
        }

        manager?.setDnsSdResponseListeners(channel, servListener, txtListener)
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


