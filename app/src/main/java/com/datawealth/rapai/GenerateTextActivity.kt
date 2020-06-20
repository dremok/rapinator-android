package com.datawealth.rapai

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.net.URL

private const val TAG = "GenerateTextActivity"

class CallModelAPI(private val textView: TextView) : AsyncTask<String?, Void?, String>() {
    override fun doInBackground(vararg params: String?): String {
        val artistName = params[0]
        return URL("http://13.49.79.162:8003/song?artist=$artistName").readText()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

        this.textView.text = result
    }
}

class GenerateTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_text)

        val artistName = intent.getStringExtra(EXTRA_MESSAGE)

        Log.d(TAG, "Artist name: $artistName")
        findViewById<TextView>(R.id.textViewGeneratedLyrics).text = getString(
            R.string.generating_text_wait_msg, artistName
        )

        val modelAPICaller = CallModelAPI(findViewById(R.id.textViewGeneratedLyrics))
        modelAPICaller.execute(artistName)
    }
}