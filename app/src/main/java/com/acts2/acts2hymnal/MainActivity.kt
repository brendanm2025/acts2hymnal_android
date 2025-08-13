package com.acts2.acts2hymnal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.acts2.acts2hymnal.ui.theme.Acts2HymnalTheme

class MainActivity : ComponentActivity() {

    private lateinit var assetDownloader: AssetDownloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        assetDownloader = AssetDownloader(this)
        assetDownloader.checkFirstRun()

        enableEdgeToEdge()
        val songList = readAllSongs(this@MainActivity)
        setContent {
            Acts2HymnalTheme {
                HymnalApp(songList=songList, context=this@MainActivity)
            }
        }
    }
}