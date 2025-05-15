package top.wkbin.skin

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.res.ResourcesCompat

class SkinResources private constructor(val context: Context) {

    companion object {
        private const val TAG = "SkinResources"

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: SkinResources? = null
        fun init(context: Context) {
            instance ?: synchronized(this) {
                instance ?: SkinResources(context.applicationContext).also { instance = it }
            }
        }

        fun get(): SkinResources {
            return instance!!
        }
    }

    private val appResources = context.resources
    private var skinResources: Resources? = null
    private var skinPkgName = ""
    private var useDefaultSkin = true

    fun reset() {
        skinResources = null
        skinPkgName = ""
        useDefaultSkin = true
    }

    fun applySkin(resources: Resources?, pkgName: String) {
        skinResources = resources
        skinPkgName = pkgName
        useDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null
        Log.d(TAG, "Applied skin: pkgName=$pkgName, useDefaultSkin=$useDefaultSkin")
    }

    fun background(resId: Int): Any {
        val resourceTypeName = appResources.getResourceTypeName(resId)
        if (resourceTypeName == "color") {
            return color(resId)
        }
        return drawable(resId)
    }

    fun color(resId: Int): Int {
        if (useDefaultSkin) {
            return ResourcesCompat.getColor(appResources, resId, null)
        }

        val skinId = identifier(resId)
        Log.d(TAG, "Color mapping: original=$resId, skin=$skinId")
        if (skinId == 0) {
            return ResourcesCompat.getColor(appResources, resId, null)
        }

        try {
            return ResourcesCompat.getColor(skinResources!!, skinId, null)
        } catch (e: Exception) {
            Log.e(TAG, "Error getting color from skin: ${e.message}")
            return ResourcesCompat.getColor(appResources, resId, null)
        }
    }

    private fun identifier(resId: Int): Int {
        if (useDefaultSkin) {
            return resId
        }

        val resName = appResources.getResourceEntryName(resId)
        val resType = appResources.getResourceTypeName(resId)
        
        // 尝试在皮肤包中查找资源
        var skinId = skinResources!!.getIdentifier(resName, resType, skinPkgName)
        
        // 如果找不到，尝试使用默认资源
        if (skinId == 0) {
            skinId = skinResources!!.getIdentifier(resName, resType, "android")
        }
        
        Log.d(TAG, "Resource mapping: name=$resName, type=$resType, pkg=$skinPkgName, id=$skinId")
        return skinId
    }

    fun colorStateList(resId: Int): ColorStateList {
        if (useDefaultSkin) {
            return ResourcesCompat.getColorStateList(appResources, resId, null)!!
        }

        val skinId = identifier(resId)
        Log.d(TAG, "ColorStateList mapping: original=$resId, skin=$skinId")
        if (skinId == 0) {
            return ResourcesCompat.getColorStateList(appResources, resId, null)!!
        }

        try {
            return ResourcesCompat.getColorStateList(skinResources!!, skinId, null)!!
        } catch (e: Exception) {
            Log.e(TAG, "Error getting ColorStateList from skin: ${e.message}")
            return ResourcesCompat.getColorStateList(appResources, resId, null)!!
        }
    }

    fun drawable(resId: Int): Drawable {
        if (useDefaultSkin) {
            return ResourcesCompat.getDrawable(appResources, resId, null)!!
        }

        val skinId = identifier(resId)
        Log.d(TAG, "Drawable mapping: original=$resId, skin=$skinId")
        if (skinId == 0) {
            return ResourcesCompat.getDrawable(appResources, resId, null)!!
        }

        try {
            return ResourcesCompat.getDrawable(skinResources!!, skinId, null)!!
        } catch (e: Exception) {
            Log.e(TAG, "Error getting drawable from skin: ${e.message}")
            return ResourcesCompat.getDrawable(appResources, resId, null)!!
        }
    }

    fun resourceId(attributeName: String, attrs: AttributeSet): Int {
        for (i in 0 until attrs.attributeCount) {
            val temp = attrs.getAttributeName(i)
            if (temp == attributeName) {
                val attributeValue = attrs.getAttributeValue(i)
                if (attributeValue.startsWith("#") || attributeValue.startsWith("?")) {
                    return 0
                }
                return attributeValue.substring(1).toInt()
            }
        }
        return 0
    }
}