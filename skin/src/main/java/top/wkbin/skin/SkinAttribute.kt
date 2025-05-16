package top.wkbin.skin

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.BaseProgressIndicator
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputLayout

class SkinAttribute {
    private val attributes = arrayListOf(
        "background", "src",
        "textColor", "textColorHint",
        "drawableStart", "drawableTop", "drawableEnd", "drawableBottom",
        "backgroundTint", "tint", "buttonTint",
        "trackTint", "thumbTint",
        "strokeColor", "rippleColor",
        "boxStrokeColor", "boxBackgroundColor",
        "indicatorColor", "trackColor",
        "cursorColor", "statusBarColor", "navigationBarColor"
    )

    private val skinViews = arrayListOf<SkinView>()

    fun extSkinAttributes(list: List<String>): SkinAttribute {
        attributes.addAll(list)
        return this
    }

    fun load(view: View, attrs: AttributeSet) {
        val skinPairs = arrayListOf<SkinPair>()
        for (i in 0 until attrs.attributeCount) {
            val attributeName = attrs.getAttributeName(i)
            if (!attributes.contains(attributeName)) {
                continue
            }

            val attributeValue = attrs.getAttributeValue(i)
            if (attributeValue.startsWith("#")) {
                // 忽略: 固定的属性值
                continue
            }

            val resId = if (attributeValue.startsWith("?")) {
                val attrId = attributeValue.substring(1).toInt()
                val temp = SkinThemeHelper.resId(view.context, intArrayOf(attrId))
                if (temp.isNotEmpty()) {
                    temp[0]
                } else {
                    0
                }
            } else {
                attributeValue.substring(1).toInt()
            }
            if (resId == 0) {
                // 忽略: 不存在的资源值
                continue
            }

            skinPairs.add(SkinPair(attributeName, resId))
        }

        if (skinPairs.isEmpty() && view !is TextView && view !is SkinViewSupport) {
            return
        }

        skinViews.add(SkinView(view, skinPairs))
    }

    fun applySkin() {
        Log.d("SkinAttribute", "applySkin: applying skin to ${skinViews.size} views")
        skinViews.forEach {
            it.applySkin()
        }
    }
}

class SkinView(private val view: View, private val skinPairs: List<SkinPair>) {
    fun applySkin() {
        Log.d("SkinAttribute", "SkinView.applySkin: applying skin to view ${view.javaClass.simpleName}")
        applySkinViewSupport()

        skinPairs.forEach {
            setAttrs(view, it)
        }
    }

    private fun setAttrs(view: View, pair: SkinPair) {
        Log.d("SkinAttribute", "SkinView.setAttrs: setting attribute ${pair.attributeName} for view ${view.javaClass.simpleName}")
        var drawableStart: Drawable? = null
        var drawableEnd: Drawable? = null
        var drawableTop: Drawable? = null
        var drawableBottom: Drawable? = null

        when (pair.attributeName) {
            "background" -> {
                val background = SkinResources.get().background(pair.resId)
                if (background is Int) {
                    view.setBackgroundColor(background)
                } else {
                    ViewCompat.setBackground(view, background as Drawable)
                }
            }
            "src" -> {
                val background = SkinResources.get().background(pair.resId)
                (view as ImageView).setImageDrawable(
                    if (background is Int) ColorDrawable(background) else (background as Drawable)
                )
            }
            "textColor" -> {
                (view as TextView).setTextColor(SkinResources.get().colorStateList(pair.resId))
            }
            "textColorHint" -> {
                (view as TextView).setHintTextColor(SkinResources.get().colorStateList(pair.resId))
            }
            "drawableStart" -> {
                drawableStart = SkinResources.get().drawable(pair.resId)
            }
            "drawableEnd" -> {
                drawableEnd = SkinResources.get().drawable(pair.resId)
            }
            "drawableTop" -> {
                drawableTop = SkinResources.get().drawable(pair.resId)
            }
            "drawableBottom" -> {
                drawableBottom = SkinResources.get().drawable(pair.resId)
            }
            "backgroundTint" -> {
                ViewCompat.setBackgroundTintList(view, SkinResources.get().colorStateList(pair.resId))
            }
            "tint" -> {
                if (view is ImageView) {
                    view.imageTintList = SkinResources.get().colorStateList(pair.resId)
                }
            }
            "buttonTint" -> {
                if (view is CompoundButton){
                    view.buttonTintList = SkinResources.get().colorStateList(pair.resId)
                }
            }
            "trackTint" -> {
                when(view){
                    is SwitchCompat -> {
                        view.trackTintList = SkinResources.get().colorStateList(pair.resId)
                    }
                    is Slider->{
                        view.trackTintList = SkinResources.get().colorStateList(pair.resId)
                    }
                }
            }
            "thumbTint" -> {
                when(view){
                    is SwitchCompat -> {
                        view.thumbTintList = SkinResources.get().colorStateList(pair.resId)
                    }
                    is Slider -> {
                        view.thumbTintList = SkinResources.get().colorStateList(pair.resId)
                    }
                }
            }
            "strokeColor" -> {
                if (view is MaterialButton) {
                    view.strokeColor = SkinResources.get().colorStateList(pair.resId)
                }
            }
            "rippleColor" -> {
                if (view is MaterialButton) {
                    view.rippleColor = SkinResources.get().colorStateList(pair.resId)
                }
            }
            "boxStrokeColor" -> {
                if (view is TextInputLayout) {
                    view.boxStrokeColor = SkinResources.get().color(pair.resId)
                }
            }
            "boxBackgroundColor" -> {
                if (view is TextInputLayout) {
                    view.boxBackgroundColor = SkinResources.get().color(pair.resId)
                }
            }
            "indicatorColor" -> {
                if (view is BaseProgressIndicator<*>) {
                    view.setIndicatorColor(SkinResources.get().color(pair.resId))
                }
            }
            "trackColor" -> {
                if (view is BaseProgressIndicator<*>) {
                    view.trackColor = SkinResources.get().color(pair.resId)
                }
            }
            "cursorColor" -> {
                if (view is TextInputLayout) {
                    view.editText?.let { editText ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            editText.textCursorDrawable?.setTint(SkinResources.get().color(pair.resId))
                        } else {
                            // Ignore
                        }
                    }
                }
            }
            "statusBarColor" -> {
                if (view.context is Activity) {
                    val window = (view.context as Activity).window
                    window.statusBarColor = SkinResources.get().color(pair.resId)
                    // 设置状态栏图标颜色
                    val isDark = isColorDark(SkinResources.get().color(pair.resId))
                    WindowCompat.getInsetsController(window, window.decorView).apply {
                        isAppearanceLightStatusBars = !isDark
                    }
                }
            }
            "navigationBarColor" -> {
                if (view.context is Activity) {
                    val window = (view.context as Activity).window
                    window.navigationBarColor = SkinResources.get().color(pair.resId)
                    // 设置导航栏图标颜色
                    val isDark = isColorDark(SkinResources.get().color(pair.resId))
                    WindowCompat.getInsetsController(window, window.decorView).apply {
                        isAppearanceLightNavigationBars = !isDark
                    }
                }
            }
        }

        if (drawableStart != null || drawableEnd != null || drawableTop != null || drawableBottom != null) {
            (view as TextView).setCompoundDrawablesWithIntrinsicBounds(
                drawableStart,
                drawableTop,
                drawableEnd,
                drawableBottom
            )
        }
    }

    private fun applySkinViewSupport() {
        if (view is SkinViewSupport) {
            view.applySkin()
        }
    }

    private fun isColorDark(color: Int): Boolean {
        val darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
        return darkness >= 0.5
    }
}

data class SkinPair(val attributeName: String, val resId: Int) 