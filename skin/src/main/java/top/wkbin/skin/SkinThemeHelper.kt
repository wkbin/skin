package top.wkbin.skin

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet

object SkinThemeHelper {
    fun resId(context: Context, attrs: IntArray): IntArray {
        val typedArray = context.obtainStyledAttributes(attrs)
        val resIds = IntArray(attrs.size)
        for (i in attrs.indices) {
            resIds[i] = typedArray.getResourceId(i, 0)
        }
        typedArray.recycle()
        return resIds
    }
} 