package com.acts2.acts2hymnal

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import androidx.core.content.edit
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus

class AssetDownloader(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("audio_assets_pref", Context.MODE_PRIVATE)

    private val manager = SplitInstallManagerFactory.create(context)
    private val moduleName = "audio_assets_1"
    private val moduleNameTwin = "audio_assets_2"


    fun isInstalled(): Boolean {
        return manager.installedModules.contains(moduleName) && manager.installedModules.contains(moduleNameTwin)
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun checkFirstRun() {
        val firstRun = prefs.getBoolean("first_run", true)
        if (firstRun) {
            if (isWifiConnected()) {
                startDownload()
            } else {
                promptUserToDownload()
            }

            prefs.edit() { putBoolean("first_run", false) }
        }
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    private fun isWifiConnected(): Boolean {
        val connectivityManager =
            context.getSystemService((Context.CONNECTIVITY_SERVICE)) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    } // isWifiConnected()

    private fun promptUserToDownload() {
        AlertDialog.Builder(context)
            .setTitle("Download Audio Files")
            .setMessage("The app needs to download additional audio files (~250 MB) to enable hymn tunes")
            .setPositiveButton("Download") {_, _ -> startDownload() }
            .setNegativeButton("Maybe later", null)
            .show()
    } // promptUserToDownload()

    private fun startDownload() {
        if (isInstalled()) return

        val request = SplitInstallRequest.newBuilder()
            .addModule(moduleName)
            .addModule(moduleNameTwin)
            .build()

        manager.startInstall(request)
            .addOnSuccessListener { Log.d("AssetDownloader", "Download started") }
            .addOnFailureListener { e -> Log.d("AssetDownloader", "Download failed: $e") }

        manager.registerListener { state ->
            state.moduleNames().forEach { module ->
                val progress = if (state.totalBytesToDownload() > 0)
                    state.bytesDownloaded() * 100 / state.totalBytesToDownload()
                else 0
                Log.d("AssetDownloader", "Module $module progress: $progress%")

                if (state.status() == SplitInstallSessionStatus.INSTALLED) {
                    Log.d("AssetDownloader", "Module $module installed successfully")
                }
            }
        }
    } // startDownload()

    fun uninstallModules() {
        val modulesToUninstall = listOf(moduleName, moduleNameTwin)
            .filter { manager.installedModules.contains(it) }

        if (modulesToUninstall.isEmpty()) return

        manager.deferredUninstall(modulesToUninstall)
            .addOnSuccessListener { Log.d("AssetDownloader", "$modulesToUninstall scheduled for uninstall") }
            .addOnFailureListener { e -> Log.d("AssetDownloader", "Failed to uninstall: $e") }
    } // uninstallModule()

}
