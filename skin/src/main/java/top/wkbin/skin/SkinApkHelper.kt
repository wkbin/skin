package top.wkbin.skin

import android.content.Context
import android.text.TextUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import java.math.BigInteger
import java.security.MessageDigest

object SkinApkHelper {

    private const val TAG = ""

    fun selectSkin(context: Context, skin: Skin): Boolean {
        if (skin.default()) {
            return true
        }

        val skins = File(localStorageRootPath(context), "skins")
        if (skins.exists() && skins.isFile) {
            skins.delete()
        }
        if (!skins.exists()) {
            skins.mkdirs()
        }

        val skinFile = skin.skinFile(skins)
        if (skinFile.exists() && TextUtils.equals(skinMD5(skinFile), skin.md5)) {
            return true
        }

        var fos: FileOutputStream? = null
        var iStream: InputStream? = null
        val tempSkin = File(skinFile.parentFile, "${skin.name}.temp")
        try {
            fos = FileOutputStream(tempSkin)
            iStream = context.assets.open(skin.url)
            val buffer = ByteArray(1024)
            var length = 0
            while (true) {
                length = iStream.read(buffer)
                if (length == -1) {
                    break
                }

                fos.write(buffer, 0, length)
            }

            if (TextUtils.equals(skinMD5(tempSkin), skin.md5)) {
                tempSkin.renameTo(skinFile)
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            tempSkin.delete()
            iStream?.close()
            fos?.close()
        }

        return false
    }

    private fun skinMD5(file: File): String {
        val result: BigInteger?

        var fis: FileInputStream? = null
        try {
            val md5 = MessageDigest.getInstance("MD5")
            fis = FileInputStream(file)
            val buffer = ByteArray(1024)
            var length = 0
            while (true) {
                length = fis.read(buffer)
                if (length == -1) {
                    break
                }
                md5.update(buffer, 0, length)
            }
            result = BigInteger(1, md5.digest())
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        } finally {
            fis?.close()
        }

        return result!!.toString(16)
    }

    fun emptyPath(context: Context, path: String): Boolean {
        if (TextUtils.isEmpty(path)) {
            return true
        }

        return path == localStorageRootPath(context)
    }

    private fun localStorageRootPath(context: Context): String {
        context.getExternalFilesDir("")?.let {
            return it.absolutePath
        }

        return context.filesDir.path
    }
}