package com.csci572.adhocmessaging

import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.net.wifi.p2p.WifiP2pInfo
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
import android.annotation.SuppressLint
import android.os.Build
import android.os.Environment
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream


class MainActivity : ComponentActivity() {

   var p2papp: MyWifiP2PApp? = null
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        p2papp?.onDestroy()

    }

    /* register the broadcast receiver with the intent values to be matched */
    override fun onResume() {
        super.onResume()
        if(p2papp != null) {
            p2papp?.receiver?.also { receiver ->
                registerReceiver(receiver, p2papp!!.intentFilter)
            }
        }
    }

    /* unregister the broadcast receiver */
    override fun onPause() {
        super.onPause()
        if(p2papp != null) {
            p2papp?.receiver?.also { receiver ->
                unregisterReceiver(receiver)
            }
        }

    }
}


