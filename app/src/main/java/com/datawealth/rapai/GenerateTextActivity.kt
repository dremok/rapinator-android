package com.datawealth.rapai

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Klaxon
import java.io.IOException
import java.lang.RuntimeException
import java.net.URL

// Dev API
private const val API_URL = "http://13.48.138.44:8003"
// Live API
//private const val API_URL = "http://13.49.79.162:8003"

private const val TAG = "GenerateTextActivity"

class Song(val artist: String, val title: String, val lyrics: String)

class CallModelAPI(
    private val artistTextView: TextView,
    private val titleTextView: TextView,
    private val lyricsTextView: TextView,
    private val startOverBtn: Button
) : AsyncTask<String?, Void?, String>() {
    override fun doInBackground(vararg params: String?): String {
        val artistName = params[0]
        return try {
            URL("$API_URL/song?artist=$artistName").readText()
        } catch (e: IOException) {
            ""
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        this.startOverBtn.visibility = View.VISIBLE

        if (result.isEmpty()) {
            this.titleTextView.text = "Temporary Error"
            this.lyricsTextView.text = "Something is not right. Please try again later."
            return
        }

        val parsedJson = Klaxon()
            .parse<Song>(result) ?: return

        val songTitle = parsedJson.title
        this.titleTextView.text = "Title: $songTitle"
        this.lyricsTextView.text = parsedJson.lyrics
    }
}

class GenerateTextActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_text)

        val artistTextView = findViewById<TextView>(R.id.textViewArtist)
        val titleTextView = findViewById<TextView>(R.id.textViewTitle)
        val lyricsTextView = findViewById<TextView>(R.id.textViewGeneratedLyrics)
        val startOverBtn = findViewById<Button>(R.id.btnStartOver)

        lyricsTextView.movementMethod = ScrollingMovementMethod()

        val artistName = intent.getStringExtra(EXTRA_MESSAGE)

        Log.d(TAG, "Artist name: $artistName")
        artistTextView.text = getString(
            R.string.artist_name, artistName
        )
        lyricsTextView.text = getString(
            R.string.generating_text_wait_msg, artistName
        )

        val modelAPICaller = CallModelAPI(
            artistTextView, titleTextView, lyricsTextView,
            startOverBtn
        )
        modelAPICaller.execute(artistName)

    }
}