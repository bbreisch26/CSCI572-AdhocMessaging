package com.csci572.adhocmessaging

import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.IntentFilter
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pConfig.GROUP_CLIENT_IP_PROVISIONING_MODE_IPV4_DHCP
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.toMutableStateList
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket


class MyWifiP2PApp {
    var username: String = ""
    var manager: WifiP2pManager? = null
    var messageDatabase: MessageDatabase? = null
    var channel: WifiP2pManager.Channel? = null
    var receiver: WiFiDirectBroadcastReceiver? = null
    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }
    private var serverSocket: ServerSocket? = null
    private var serverSocketTask: ServerSocketTask? = null
    private val serverPort = 8888
    var peerIPAddress: String? = ""
    var p2pinfo: WifiP2pInfo? = null

    // List of peer devices
    var peerList: WifiP2pDeviceList? = null

    // Map of (MAC address, device name) items
    var servicePeerList = mutableStateMapOf<String, String>()
    private var context: Context? = null
    var groupInfoListener: WifiP2pManager.GroupInfoListener =
        WifiP2pManager.GroupInfoListener { group ->
            if (group != null) {
                Log.v("MainActivityGroup", group.toString())
            }
        }
    var connectionInfoListener: WifiP2pManager.ConnectionInfoListener =
        WifiP2pManager.ConnectionInfoListener { wifiP2pInfo ->
            Log.v("MainActivity", wifiP2pInfo.toString())
            Log.v("MainActivity", manager?.requestGroupInfo(channel, groupInfoListener).toString())
            //p2pinfo
            p2pinfo = wifiP2pInfo
            if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner) {
                Log.v("MainActivity", "Group formed and the owner")
                Log.v("MainActivity", "Starting Server")
                //figure out peerIPAddress
                //wait
                //connState.text = getString(R.string.host)
                //serverClass = ServerClass(this)
                //serverClass.start()
                Log.i("conn", "serverClass started!")
            } else if (wifiP2pInfo.groupFormed && !wifiP2pInfo.isGroupOwner) {
                Log.v("MainActivity", "Group formed but not the owner")
                Log.v("MainActivity", "Starting Client")
                peerIPAddress = wifiP2pInfo.groupOwnerAddress.hostAddress
                Log.v("MainActivity", "Sending IP Address HELLO message to group owner")
                sendMessage("") //send message to group owner
            }
        }


    @RequiresApi(Build.VERSION_CODES.M)
    fun setup(context: Context, userName: String) {
        this.context = context
        this.username = userName
        this.messageDatabase = Room.databaseBuilder(
            context,
            MessageDatabase::class.java, "database-name"
        ).build()

        manager = context.getSystemService(WifiP2pManager::class.java)

        channel = manager?.initialize(this.context, this.context!!.mainLooper, null)
        channel?.also { channel ->
            receiver = WiFiDirectBroadcastReceiver(manager, channel, this)
        }
        registerReceiver(
            this.context!!,
            receiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
        registerService(userName)
        discoverService()

        var serviceRequest = WifiP2pDnsSdServiceRequest.newInstance()
        manager?.addServiceRequest(
            channel,
            serviceRequest,
            object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    // Success!
                    Log.v("MainActivity", "Successful Add Service Request")
                }

                override fun onFailure(code: Int) {
                    // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                    Log.v("MainActivity", "Failure Add Service Request: " + code)
                }
            }
        )

        manager?.discoverServices(
            channel,
            object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    // Success!
                    Log.v("MainActivity", "Successful Discover Services")
                }

                override fun onFailure(code: Int) {
                    Log.v("MainActivity", "Failure Discover Services: " + code)
                    // Command failed. Check for P2P_UNSUPPORTED, ERROR, or BUSY
                    when (code) {
                        WifiP2pManager.P2P_UNSUPPORTED -> {
                            Log.d("MainActivity", "Wi-Fi Direct isn't supported on this device.")
                        }
                    }
                }
            }
        )
        initiateServerSocket()
    }

    private fun registerService(userName: String) {
        //  Create a string map containing information about your service.
        val record: Map<String, String> = mapOf(
            "listenport" to serverPort.toString(),
            "buddyname" to userName,
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
                Log.v("MainActivity", "Successful Add Local Service ")

                // Unless you want to update the UI or add logging statements.
            }

            override fun onFailure(code: Int) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                Log.v("MainActivity", "Failure Add Local Service: " + code)
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
            Log.v("MainActivity", "DnsSdTxtRecord available -$record")
            device.status
            record["buddyname"]?.also {
                servicePeerList[device.deviceAddress] = it
            }

        }

        val servListener =
            WifiP2pManager.DnsSdServiceResponseListener { instanceName, registrationType, resourceType ->
                // Update the device name with the human-friendly version from
                // the DnsTxtRecord, assuming one arrived.
                resourceType.deviceName =
                    servicePeerList[resourceType.deviceAddress] ?: resourceType.deviceName

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

    fun connectToPeer(address: String) {
        val config = WifiP2pConfig()
        config.deviceAddress = address
        config.groupOwnerIntent = 15
        manager?.connect(channel, config, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {
                Log.v("MainActivity", "Successfully connected to peer: $address")
            }

            override fun onFailure(reason: Int) {
                //failure logic
                Log.v("MainActivity", "Failed to connect to peer: $address")
            }
        })
    }

    fun onDestroy() {
        manager?.cancelConnect(channel, null)
        //Receiver already unregistered with onPause()
        //if(receiver != null) {
        //unregisterReceiver(receiver);
        //}
        manager?.clearLocalServices(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                // Command successful! Code isn't necessarily needed here,
                Log.v("MainActivity", "Successfully Removed Local Services ")

                // Unless you want to update the UI or add logging statements.
            }

            override fun onFailure(code: Int) {
                // Command failed.  Check for P2P_UNSUPPORTED, ERROR, or BUSY
                Log.v("MainActivity", "Failure To Remove Local Services: " + code)
            }
        })
        serverSocketTask?.cancel(true)
        serverSocket?.close()

        manager?.stopPeerDiscovery(channel, null)
    }

    private fun initiateServerSocket() {
        // Create a server socket to listen for incoming messages
        // Start a background thread to accept incoming connections
        serverSocket = ServerSocket(serverPort)
        serverSocketTask = ServerSocketTask()
        serverSocketTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }
    fun sendMessage(message: String) {
        Log.v("sendMessage", "Sending message: $message")
        // Create a server socket to listen for incoming messages
        // Start a background thread to accept incoming connections
        //Have to use executeOnExecutor to use thread pool for async tasks
        //https://stackoverflow.com/questions/31957815/android-asynctask-not-executing
        if(peerIPAddress == "") {
            Log.v("sendMessage","Tried to send message $message but peerIpAddress is empty")
        }
        else {
            SendMessageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, message, peerIPAddress)
        }
    }


    // AsyncTask to accept incoming connections in the background
    private inner class ServerSocketTask(
    ) : AsyncTask<Void, Void, Void>() {

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: Void): Void? {
            Log.v("ServerSocket", "Starting Server In Background")
            while(true) {
                try {
                    // Accept incoming connections
                    val client = serverSocket?.accept()
                    if (isCancelled()) { return null }
                    peerIPAddress = client?.inetAddress?.hostAddress

                    Log.v("ServerSocket", "Accepted incoming socket connection from ${client?.inetAddress?.hostAddress}")

                    val inputstream = client!!.getInputStream()
                    val inputAsString = inputstream.bufferedReader().use { it.readText() }
                    Log.v("ServerSocket", "Received message: $inputAsString")
                    //format: username#message1234
                    val inputList : List<String> = inputAsString.split("#",limit=2)
                    Log.v("ServerSocket",inputList.toString())
                    //put message into database
                    Log.v("ServerSocket", "Finished receiving message")
                    if(inputList.size == 2 && inputList[1].isNotEmpty()) {
                        messageDatabase?.messageDao()?.insert(Message(contactName=inputList[0], sourceIsMe = false, contents = inputList[1]))
                    }
                } catch (e: IOException) {
                    Log.e("ServerSocketTask", "Error accepting connection: ${e.message}")
                    return null
                }
            }

        }
        //restart server task once it is done executing
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            serverSocketTask = ServerSocketTask()
            serverSocketTask?.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        }

        private fun File.doesNotExist(): Boolean = !exists()

    }
    private inner class SendMessageTask : AsyncTask<String, Void, Void>(){
        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg params: String): Void? {
            try {
                val address = params[1]
                val clientSocket = Socket(address,serverPort)
                Log.v("sendMessage", "Successfully opened socket to peer: $address")

                // Get the output stream of the client socket
                val outputStream: OutputStream = clientSocket.getOutputStream()

                // Write the message to the output stream
                val message = params[0]
                //format: username#message1234
                val out = ("$username#$message")
                outputStream.write(out.toByteArray())
                Log.v("sendMessage", "Sent Message : $out to $address")

                outputStream.flush()
                clientSocket.close()
            } catch (e: IOException) {
                Log.e("sendMessage", "Error sending message: ${e.message}")
            }
            return null
        }
    }
}