package top.wkbin.skin.app

import android.app.Application
import top.wkbin.skin.SkinManager

class SkinApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化换肤管理器
        SkinManager.init(this)
    }
}