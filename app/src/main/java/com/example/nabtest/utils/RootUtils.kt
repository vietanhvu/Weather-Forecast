package com.example.nabtest.utils

import android.os.Build
import java.io.File

object RootUtils {
    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        for (pathDir in System.getenv("PATH").split(":").toTypedArray()) {
            if (File(pathDir, "su").exists()) {
                return true
            }
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = ProcessBuilder().command("su").start()
            true
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }
}