package com.csci572.adhocmessaging

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import java.io.IOException
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.SideEffect
import androidx.core.app.ActivityCompat


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
    private var serverSocket: ServerSocket? = null
    private var clientSocket: Socket? = null

    // List of peer devices
    var peerList : WifiP2pDeviceList? = null
    // Map of (MAC address, device name) items
    var servicePeerList = mutableMapOf<String, String>()
    // Address of the current connection
//    var currentConnectionAddress : String? = null
    override fun onStart() {
        super.onStart()
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up Wifi-Direct Backend
        channel = manager?.initialize(this, mainLooper, null)
        channel?.also { channel ->
            receiver = WiFiDirectBroadcastReceiver(manager, channel, this)
        }

        registerService()
        discoverService()

        var serviceRequest = WifiP2pDnsSdServiceRequest.newInstance()
        manager?.addServiceRequest(
            channel,
            serviceRequest,
            object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    // Success!
                    Log.v("MainActivity","Successful Add Service Request")

                }

                override fun onFailure(code: Int) {
                    // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                    Log.v("MainActivity","Failure Add Service Request: " + code)

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

    override fun onDestroy() {
        super.onDestroy()

        manager?.cancelConnect(channel, null)
        unregisterReceiver(receiver);
        closeSockets()
        manager?.stopPeerDiscovery(channel, null)
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
                Log.v("MainActivity","Successful Add Local Service ")

                // Unless you want to update the UI or add logging statements.
            }

            override fun onFailure(code: Int) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                Log.v("MainActivity","Failure Add Local Service: " + code)
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
            device.status
            record["buddyname"]?.also {
                servicePeerList[device.deviceAddress] = it
            }

        }

        val servListener = WifiP2pManager.DnsSdServiceResponseListener { instanceName, registrationType, resourceType ->
            // Update the device name with the human-friendly version from
            // the DnsTxtRecord, assuming one arrived.
            resourceType.deviceName = servicePeerList[resourceType.deviceAddress] ?: resourceType.deviceName

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
        config.wps.setup = WpsInfo.PBC
        channel?.also { channel ->
            manager?.connect(channel, config, object : WifiP2pManager.ActionListener {

                override fun onSuccess() {
                    Log.v("MainActivity", "Successfully connected to peer: $address")
                    initiateServerSocket()
                }

                override fun onFailure(reason: Int) {
                    //failure logic
                    Log.v("MainActivity", "Failed to connect to peer: $address")

                }
            })
        }
    }
    private fun initiateServerSocket() {
        // Create a server socket to listen for incoming messages
        var serverSocket = ServerSocket(8888)
        Handler(Looper.getMainLooper()).post {
            // Start a background thread to accept incoming connections
            ServerSocketTask().execute()
        }
    }
    private fun closeSockets() {
        serverSocket?.close()
        clientSocket?.close()
    }
    // AsyncTask to accept incoming connections in the background
    private inner class ServerSocketTask : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            try {
                // Accept incoming connections
                clientSocket = serverSocket?.accept()
            } catch (e: IOException) {
                Log.e("ServerSocketTask", "Error accepting connection: ${e.message}")
            }

            return null
        }
    }
    private inner class SendMessageTask : AsyncTask<String, Void, Void>() {
        override fun doInBackground(vararg params: String): Void? {
            try {
                // Get the output stream of the client socket
                val outputStream: OutputStream = clientSocket!!.getOutputStream()

                // Write the message to the output stream
                val message = params[0].toByteArray()
                outputStream.write(message)
                outputStream.flush()
            } catch (e: IOException) {
                Log.e("SendMessageTask", "Error sending message: ${e.message}")
            }

            return null
        }
    }
    fun sendMessage(address : String, message: String) {
        Log.v("sendMessage", "Sending message: $message")
        SendMessageTask().execute(message)
    }
}


