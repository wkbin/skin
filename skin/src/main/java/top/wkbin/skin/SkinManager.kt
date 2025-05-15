package top.wkbin.skin

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.util.Observable
import java.util.Observer

class SkinManager private constructor(
    private val application: Application,
    extSkinAttribute: List<String>
) : Observable() {

    companion object {
        private const val TAG = "SkinManager"

        @Volatile
        private var instance: SkinManager? = null
        fun init(application: Application, extSkinAttribute: List<String> = arrayListOf()) {
            instance ?: synchronized(this) {
                instance ?: SkinManager(application, extSkinAttribute).also { instance = it }
            }
        }

        fun get(): SkinManager {
            return instance!!
        }
    }

    private val skinActivityLifecycle = SkinActivityLifecycle(extSkinAttribute)
    private val skinPreference: SkinPreference = SkinPreference(application)

    init {
        SkinResources.init(application)
        application.registerActivityLifecycleCallbacks(skinActivityLifecycle)
        useLocaleStorageSkin()
    }

    fun useLocaleStorageSkin() {
        loadSkin(skinPreference.getSkinPath())
        notifyChanged()
    }

    private fun loadSkin(path: String): String {
        if (SkinApkHelper.emptyPath(application, path)) {
            SkinResources.get().reset()
            return ""
        }

        try {
            val am = AssetManager::class.java.newInstance()
            val addAssetPath = am.javaClass.getMethod("addAssetPath", String::class.java)
            addAssetPath.isAccessible = true
            addAssetPath.invoke(am, path)

            val resources = application.resources
            val skinResources = Resources(am, resources.displayMetrics, resources.configuration)
            val info = application.packageManager!!.getPackageArchiveInfo(
                path,
                PackageManager.GET_ACTIVITIES
            )
            val packageName = info?.packageName!!
            SkinResources.get().applySkin(skinResources, packageName)
        } catch (e: Exception) {
            Log.e(TAG, "error: ${e.message}")
            e.printStackTrace()
            return skinPreference.getSkinPath()
        }

        return path
    }

    private fun notifyChanged() {
        setChanged()
        notifyObservers()
    }

    fun previewSkin(skin: Skin) {
        SkinApkHelper.selectSkin(application, skin)
        loadSkin(skin.path)
        notifyChanged()
    }

    fun changeSkin(skin: Skin) {
        SkinApkHelper.selectSkin(application, skin)
        skinPreference.setSkin(skin.name, skin.md5, loadSkin(skin.path))
        notifyChanged()
    }

    fun currentSkinName(): String {
        val skinName = skinPreference.getSkinName()
        if (!TextUtils.isEmpty(skinName)) {
            return skinName
        }

        val path = skinPreference.getSkinPath()
        if (TextUtils.isEmpty(path)) {
            return ""
        }

        val nameStartIndex = path.lastIndexOf("/") + 1
        return path.substring(nameStartIndex, path.length)
    }

    fun currentSkinMd5(): String {
        return skinPreference.getSkinMd5()
    }

    fun updateSkin(activity: Activity) {
        skinActivityLifecycle.updateSkin(activity)
    }

    fun drawable(resId: Int): Drawable {
        return SkinResources.get().drawable(resId)
    }
}