package top.wkbin.skin

import android.content.Context
import android.os.Parcel
import android.text.TextUtils
import android.util.Base64

object SkinHelper {

    private const val SP_FILE = "keystone_skin"
    private const val KEY_SKIN = "key_skin"
    private  val skinDefault = Skin("", "", "")

    fun writeSkin(context: Context, skin: Skin):Boolean {
        val bytes = parcelableToByteArray(skin)
        val saveStr = Base64.encodeToString(bytes, 0)
        return context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SKIN, saveStr)
            .commit()
    }

    fun readSkin(context: Context): Skin {
        val readStr = context.getSharedPreferences(SP_FILE, Context.MODE_PRIVATE)
            .getString(KEY_SKIN, "")
        if (TextUtils.isEmpty(readStr)) {
            return skinDefault
        }

        val bytes = Base64.decode(readStr?.toByteArray(), Base64.DEFAULT)
        val parcel = unmarshall(bytes)
        return parcel.readValue(context.classLoader) as Skin
    }

    private fun parcelableToByteArray(skin: Skin): ByteArray {
        val parcel = Parcel.obtain()
        parcel.setDataPosition(0)
        parcel.writeValue(skin)
        val bytes = parcel.marshall()
        parcel.recycle()
        return bytes
    }

    private fun unmarshall(bytes: ByteArray): Parcel {
        val parcel = Parcel.obtain()
        parcel.unmarshall(bytes, 0, bytes.size)
        parcel.setDataPosition(0)
        return parcel
    }
}