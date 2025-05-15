package top.wkbin.skin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import org.lsposed.hiddenapibypass.HiddenApiBypass
import java.lang.Exception

class SkinActivityLifecycle(private val extSkinAttribute: List<String>) :
    Application.ActivityLifecycleCallbacks {

    @SuppressLint("NewApi")
    companion object {
        private const val TAG = "SkinActivityLifecycle"
        
        init {
            try {
                // 添加 android.view 包到豁免列表
                HiddenApiBypass.addHiddenApiExemptions("Landroid/view/")
                Log.d(TAG, "Successfully added android.view to hidden API exemptions")
            } catch (e: Exception) {
                Log.e(TAG, "Failed to add hidden API exemptions: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private val layoutFactoryMap = hashMapOf<Activity, SkinLayoutFactory>()

    @SuppressLint("PrivateApi")
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val layoutInflater = LayoutInflater.from(activity)
        try {
            // 现在可以直接使用反射，因为已经在豁免列表中
            val field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            field.isAccessible = true
            field.setBoolean(layoutInflater, false)
            
            val skinLayoutFactory = SkinLayoutFactory(extSkinAttribute)
            LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutFactory)
            SkinManager.get().addObserver(skinLayoutFactory)
            layoutFactoryMap[activity] = skinLayoutFactory
            Log.d(TAG, "Successfully set SkinLayoutFactory for ${activity.javaClass.simpleName}")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting SkinLayoutFactory: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        SkinManager.get().deleteObserver(layoutFactoryMap.remove(activity))
    }

    fun updateSkin(activity: Activity) {
        layoutFactoryMap[activity]?.update(null, null)
    }

    override fun onActivityStarted(activity: Activity) {
        // Nothing
    }

    override fun onActivityResumed(activity: Activity) {
        // Nothing
    }

    override fun onActivityPaused(activity: Activity) {
        // Nothing
    }

    override fun onActivityStopped(activity: Activity) {
        // Nothing
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Nothing
    }
}