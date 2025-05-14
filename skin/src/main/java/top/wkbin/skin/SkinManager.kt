package top.wkbin.skin

import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.util.Log
import java.io.File

class SkinManager private constructor(
    private val application: Application
) {

    companion object {
        private const val TAG = "SkinManager"

        @Volatile
        private var instance: SkinManager? = null
        fun init(application: Application) {
            instance ?: synchronized(this) {
                instance ?: SkinManager(application).also { instance = it }
            }
        }

        fun get() = instance!!
    }

    fun previewSkin(skin: Skin) {
        if (SkinApkHelper.selectSkin(application, skin)) {
            loadSkin(skin.path)
        }
    }

    fun loadSkin(path: String) {
        if (!File(path).exists()) {
            SkinResources.get().applySkin(null,null)
            Log.d(TAG, "loadSkin : file not found at $path")
            return
        }

        try {
            val am = AssetManager::class.java.newInstance()
            val addAssetPath = am.javaClass.getMethod("addAssetPath", String::class.java)
            addAssetPath.isAccessible = true
            addAssetPath.invoke(am, path)
            val resources = application.resources
            val skinResources = Resources(am, resources.displayMetrics, resources.configuration)
            val info = application.packageManager?.getPackageArchiveInfo(
                path,
                PackageManager.GET_ACTIVITIES
            )
            val packageName = info?.packageName
            SkinResources.get().applySkin(skinResources, packageName)
        } catch (e: Exception) {
            Log.d(TAG, "loadSkin: e = ${e.message}")
        }
    }
}