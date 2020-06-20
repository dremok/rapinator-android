package com.datawealth.rapai

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

const val EXTRA_MESSAGE = "com.datawealth.rapai.MESSAGE"

class TextGeneratorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_generator)
    }

    fun generateText(view: View) {
        val editTextArtistName = findViewById<EditText>(R.id.editTextArtistName)
        val message = editTextArtistName.text.toString()
        val intent = Intent(this, GenerateTextActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

}