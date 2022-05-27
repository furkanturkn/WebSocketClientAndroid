package com.furkan.sockettest

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.furkan.sockettest.databinding.ActivityMainBinding
import com.furkan.sockettest.model.ClientUser
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft_17
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {

    private var mWebSocketClient: WebSocketClient? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        connectWebSocket()

        binding.buttonRun.setOnClickListener {
            mWebSocketClient?.let {
                //it.close()
                connectWebSocket()
                Log.d("test", "furkannnnn")
            }
        }

        binding.sendMessage.setOnClickListener {
            sendMessage(binding.etMessageToServer.text.toString())
        }
    }


    private fun connectWebSocket() {

        val mSocketAddress = "ws://10.34.80.28:8887"
        val uri: URI
        uri = try {
            URI(mSocketAddress)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }

        mWebSocketClient = object : WebSocketClient(uri, Draft_17()) {
            override fun onOpen(serverHandshake: ServerHandshake) {
                Log.i("Websocket", "Opened")


                runOnUiThread {
                    binding.txtConnectionStatus.text = "connected!"
                }


                val gson = Gson()
                var jsonString = gson.toJson(
                    ClientUser(
                        Utils.getAndroidId(applicationContext),
                        Utils.getBuildModel()
                    )
                )
                mWebSocketClient!!.send("*--*$jsonString")
            }

            override fun onMessage(s: String) {
                Log.i("Websocket", "message has been retrieved: $s")
                runOnUiThread {
                    binding.testTextView.text = s
                }

            }

            override fun onClose(i: Int, s: String, b: Boolean) {
                runOnUiThread {
                    binding.txtConnectionStatus.text = "connection lost"
                }

                Log.i("Websocket", "onClose $s")
            }

            override fun onError(e: Exception) {
                runOnUiThread {
                    binding.txtConnectionStatus.text = "connection lost"
                }
                Log.i("Websocket", "onError" + e.message)
            }
        }
        mWebSocketClient?.connect() ?: run {
            Log.d("websocketxxx", "furkan1")
            Log.i("Websocket", "Error " + "WEB SOCKET NULL HOCAM")
        }

    }

    fun sendMessage(message: String) {
        mWebSocketClient?.let {
            mWebSocketClient!!.send(message)
        } ?: run {
            Log.i("Websocket", "Error " + "WEB SOCKET NULL HOCAM send message")
        }

    }
}