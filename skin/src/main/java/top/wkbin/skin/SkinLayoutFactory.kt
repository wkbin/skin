package top.wkbin.skin

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import java.lang.reflect.Constructor
import java.util.Observable
import java.util.Observer
import java.lang.Exception

class SkinLayoutFactory(extSkinAttribute: List<String>) :
    LayoutInflater.Factory2, Observer {

    companion object {
        private const val TAG = "SkinLayoutFactory"
    }

    private val classPrefixList = arrayListOf(
        "android.widget.", "android.view.", "android.webkit."
    )
    private val constructorSignature =
        arrayOf<Class<*>>(Context::class.java, AttributeSet::class.java)
    private val constructorMap = hashMapOf<String, Constructor<out View>>()
    private val skinAttribute = SkinAttribute().extSkinAttributes(extSkinAttribute)

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        Log.e(TAG, "onCreateView")
        var view = createViewFromTag(name, context, attrs)
        if (view == null) {
            view = createView(name, context, attrs)
        }

        view?.let {
            skinAttribute.load(it, attrs)
        }

        return view
    }

    private fun createViewFromTag(name: String, context: Context, attrs: AttributeSet): View? {
        Log.e(TAG, "createViewFromTag")
        if (name.indexOf(".") != -1) {
            return null
        }

        classPrefixList.forEach {
            val view = createView("$it$name", context, attrs)
            if (view != null) {
                return view
            }
        }

        return null
    }

    private fun createView(viewName: String, context: Context, attrs: AttributeSet): View? {
        Log.e(TAG, "createView")
        var constructor = constructorMap[viewName]
        if (constructor == null) {
            try {
                val clz = context.classLoader.loadClass(viewName).asSubclass(View::class.java)
                constructor = clz.getConstructor(*constructorSignature)
                constructorMap[viewName] = constructor
            } catch (e: Exception) {
            }
        }

        if (constructor != null) {
            try {
                return constructor.newInstance(context, attrs)
            } catch (e: Exception) {
            }
        }

        return null
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        Log.e(TAG, "onCreateView")
        return null
    }

    override fun update(o: Observable?, arg: Any?) {
        Log.e(TAG, "update")
        skinAttribute.applySkin()
    }
}