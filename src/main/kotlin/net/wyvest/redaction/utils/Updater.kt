package net.wyvest.redaction.utils

import gg.essential.api.EssentialAPI
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.minecraft.util.Util
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion
import net.wyvest.redaction.Redaction
import net.wyvest.redaction.Redaction.mc
import net.wyvest.redaction.config.RedactionConfig
import net.wyvest.redaction.gui.DownloadConfirmGui
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


object Updater {
    var updateUrl: String = ""
    lateinit var latestTag: String
    var shouldUpdate = false

    /**
     * Adapted from SimpleToggleSprint under AGPLv3
     * https://github.com/My-Name-Is-Jeff/SimpleToggleSprint/blob/1.8.9/LICENSE
     */
    fun update() {
        CoroutineScope(Dispatchers.IO + CoroutineName("${Redaction.NAME}-UpdateChecker")).launch {
            val latestRelease =
                APIUtil.getJSONResponse("https://api.github.com/repos/Wyvest/${Redaction.ID}/releases/latest")
            latestTag = latestRelease.get("tag_name").asString

            val currentVersion = DefaultArtifactVersion(Redaction.VERSION.substringBefore("-"))
            val latestVersion = DefaultArtifactVersion(latestTag.substringAfter("v").substringBefore("-"))

            if ((Redaction.VERSION.contains("BETA") && currentVersion >= latestVersion)) {
                return@launch
            } else if (currentVersion < latestVersion) {
                updateUrl = latestRelease["assets"].asJsonArray[0].asJsonObject["browser_download_url"].asString
            }
            if (updateUrl.isNotEmpty()) {
                if (RedactionConfig.showUpdateNotification) {
                    EssentialAPI.getNotifications()
                        .push("Mod Update", "${Redaction.NAME} $latestTag is available!\nClick here to download it!", 5f) {
                            EssentialAPI.getGuiUtil().openScreen(DownloadConfirmGui(mc.currentScreen))
                        }
                }
                shouldUpdate = true
            }
        }
    }

    /**
     * Adapted from RequisiteLaunchwrapper under LGPLv2.1
     * https://github.com/TGMDevelopment/RequisiteLaunchwrapper/blob/main/LICENSE
     */
    fun download(url: String, file: File): Boolean {
        if (file.exists()) return true
        var newUrl = url
        newUrl = newUrl.replace(" ", "%20")
        val downloadClient: HttpClient =
            HttpClientBuilder.create().setDefaultRequestConfig(RequestConfig.custom().setConnectTimeout(10000).build())
                .build()
        try {
            FileOutputStream(file).use { fileOut ->
                val downloadResponse: HttpResponse = downloadClient.execute(HttpGet(newUrl))
                val buffer = ByteArray(1024)
                var read: Int
                while (downloadResponse.entity.content.read(buffer).also { read = it } > 0) {
                    fileOut.write(buffer, 0, read)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * Adapted from Skytils under AGPLv3
     * https://github.com/Skytils/SkytilsMod/blob/1.x/LICENSE.md
     */
    fun addShutdownHook() {
        EssentialAPI.getShutdownHookUtil().register(Thread {
            println("Deleting old ${Redaction.NAME} jar file...")
            try {
                val runtime = getJavaRuntime()
                if (Util.getOSType() == Util.EnumOS.OSX) {
                    println("On Mac, trying to open mods folder")
                    Desktop.getDesktop().open(Redaction.jarFile.parentFile)
                }
                println("Using runtime $runtime")
                val file = File("config/Wyvest/Deleter-1.2.jar")
                println("\"$runtime\" -jar \"${file.absolutePath}\" \"${Redaction.jarFile.absolutePath}\"")
                Runtime.getRuntime()
                    .exec("\"$runtime\" -jar \"${file.absolutePath}\" \"${Redaction.jarFile.absolutePath}\"")
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            Thread.currentThread().interrupt()
        })
    }

    /**
     * Gets the current Java runtime being used.
     * @link https://stackoverflow.com/a/47925649
     */
    @Throws(IOException::class)
    fun getJavaRuntime(): String {
        val os = System.getProperty("os.name")
        val java = "${System.getProperty("java.home")}${File.separator}bin${File.separator}${
            if (os != null && os.lowercase().startsWith("windows")) "java.exe" else "java"
        }"
        if (!File(java).isFile) {
            throw IOException("Unable to find suitable java runtime at $java")
        }
        return java
    }

}