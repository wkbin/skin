package top.wkbin.skin

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class Skin(
    val name: String,
    val url: String,
    val md5: String
) : Parcelable{

    @IgnoredOnParcel
    private var file: File? = null
    @IgnoredOnParcel
    var path:String = ""
        private set

    fun skinFile(parent: File): File {
        if (file == null) {
            file = File(parent, name)
        }

        path = file!!.absolutePath

        return file!!
    }


    fun default(): Boolean {
        return TextUtils.isEmpty(md5) && TextUtils.isEmpty(name)
    }
}