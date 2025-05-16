package top.wkbin.skin.app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial
import top.wkbin.skin.Skin
import top.wkbin.skin.SkinManager
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var progressBar: LinearProgressIndicator
    private lateinit var switchDarkMode: SwitchMaterial
    private lateinit var slider: Slider
    private lateinit var radioGroup: RadioGroup
    private val handler = Handler(Looper.getMainLooper())
    private var progress = 0

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 应用上次设置的皮肤包
        SkinManager.get().updateSkin(this)

        // 初始化控件
        progressBar = findViewById(R.id.progressBar)
        switchDarkMode = findViewById(R.id.switchDarkMode)
        slider = findViewById(R.id.slider)
        radioGroup = findViewById(R.id.radioGroup)


        // 设置默认皮肤按钮
        findViewById<MaterialButton>(R.id.btnResetSkin).setOnClickListener {
            loadSkin(SkinConfiguration.skinDefault)
        }

        // 设置蓝色皮肤按钮
        findViewById<MaterialButton>(R.id.btnBlueSkin).setOnClickListener {
            loadSkin(SkinConfiguration.skinBlue)
        }

        // 设置红色皮肤按钮
        findViewById<MaterialButton>(R.id.btnRedSkin).setOnClickListener {
            loadSkin(SkinConfiguration.skinRed)
        }

        // 设置绿色皮肤按钮
        findViewById<MaterialButton>(R.id.btnGreenSkin).setOnClickListener {
            loadSkin(SkinConfiguration.skinGreen)
        }

        // 设置滑块监听
        slider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                progressBar.setProgress(value.toInt(), true)
            }
        }

        // 启动进度条动画
        startProgressAnimation()
    }


    private fun loadSkin(skin: Skin) {
        SkinManager.get().changeSkin(skin)
    }

    private fun startProgressAnimation() {
        handler.post(object : Runnable {
            override fun run() {
                progress = (progress + 1) % 100
                progressBar.setProgress(progress, true)
                handler.postDelayed(this, 50)
            }
        })
    }
} 