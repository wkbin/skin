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
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: SkinResources? = null

        fun init(context: Context) {
            instance ?: synchronized(this) {
                instance ?: SkinResources(context.applicationContext).also { instance = it }
            }
        }

        fun get() = instance!!
    }

    private val appResources = context.resources
    private var skinResources: Resources? = null
    private var skinPkgName: String? = null

    private var useDefaultSkin = true

    fun reset() {
        skinResources = null
        skinPkgName = ""
        useDefaultSkin = true
    }

    fun applySkin(resources: Resources?, pkgName: String?) {
        skinResources = resources
        skinPkgName = pkgName
        useDefaultSkin = TextUtils.isEmpty(pkgName) || resources == null
    }

    fun background(resId: Int): Any {
        val resourceTypeName = appResources.getResourceTypeName(resId)
        if (resourceTypeName == "color") {
            return color(resId)
        }

        return drawable(resId)
    }

    fun color(resId: Int): Long {
        if (useDefaultSkin) {
            return ResourcesCompat.getColor(appResources, resId, null).toLong()
        }
        try {
            val skinId = identifier(resId)
            if (skinId == 0) {
                return ResourcesCompat.getColor(appResources, resId, null).toLong()
            }

            return ResourcesCompat.getColor(skinResources!!, skinId, null).toLong()
        }catch (e: Resources.NotFoundException){
            return ResourcesCompat.getColor(appResources, resId, null).toLong()
        }
    }

    @SuppressLint("DiscouragedApi")
    fun identifier(resId: Int): Int {
        if (useDefaultSkin) {
            return resId
        }

        val resName = appResources.getResourceEntryName(resId)
        val resType = appResources.getResourceTypeName(resId)

        val id =  skinResources!!.getIdentifier(resName, resType, skinPkgName)
        if (id == 0){
            return resId
        }
        return id
    }

    fun colorStateList(resId: Int): ColorStateList {
        if (useDefaultSkin) {
            return ResourcesCompat.getColorStateList(appResources, resId, null)!!
        }

        val skinId = identifier(resId)
        if (skinId == 0) {
            return ResourcesCompat.getColorStateList(appResources, resId, null)!!
        }

        return ResourcesCompat.getColorStateList(skinResources!!, skinId, null)!!
    }

    fun drawable(resId: Int): Drawable {
        if (useDefaultSkin) {
            return ResourcesCompat.getDrawable(appResources, resId, null)!!
        }

        try {
            val skinId = identifier(resId)
            Log.i("temp", "drawable: $skinId")
            if (skinId == 0) {
                return ResourcesCompat.getDrawable(appResources, resId, null)!!
            }

            return ResourcesCompat.getDrawable(skinResources!!, skinId, null)!!
        } catch (e: Resources.NotFoundException){
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