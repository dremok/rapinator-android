package com.datawealth.rapai

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import kotlinx.android.synthetic.main.activity_text_generator.*

// AdMob stuff
private lateinit var mAdView: AdView
private val mAppUnitId: String by lazy {
    "ca-app-pub-5384360475282906~2655897082"
}
private val mInterstitialAdUnitId: String by lazy {
    "ca-app-pub-3940256099942544/1033173712"
}
private lateinit var mInterstitialAd: InterstitialAd
// END OF AdMob stuff

const val EXTRA_MESSAGE = "com.datawealth.rapai.MESSAGE"

class TextGeneratorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_generator)

        mAdView = findViewById(R.id.adView)
        initializeBannerAd(mAppUnitId)
        loadBannerAd()

        mInterstitialAd = InterstitialAd(this)
        initializeInterstitialAd(mAppUnitId)
        loadInterstitialAd(mInterstitialAdUnitId)

        button.setOnClickListener {
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                Log.d("TAG", "The interstitial ad wasn't loaded yet.")
                generateText()
                finish()
            }
        }

        runAdEvents()
    }

    fun generateText() {
        val editTextArtistName = findViewById<EditText>(R.id.editTextArtistName)
        val message = editTextArtistName.text.toString()
        val intent = Intent(this, GenerateTextActivity::class.java).apply {
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }

    // AdMob stuff
    private fun initializeBannerAd(appUnitId: String) {
        MobileAds.initialize(this, appUnitId)
    }

    private fun loadBannerAd() {
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun initializeInterstitialAd(appUnitId: String) {
        MobileAds.initialize(this, appUnitId)
    }

    private fun loadInterstitialAd(interstitialAdUnitId: String) {
        mInterstitialAd.adUnitId = interstitialAdUnitId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun runAdEvents() {
        mInterstitialAd.adListener = object : AdListener() {

            override fun onAdClicked() {
                super.onAdOpened()
                mInterstitialAd.adListener.onAdClosed()
            }

            override fun onAdClosed() {
                generateText()
                finish()
            }
        }
    }
}