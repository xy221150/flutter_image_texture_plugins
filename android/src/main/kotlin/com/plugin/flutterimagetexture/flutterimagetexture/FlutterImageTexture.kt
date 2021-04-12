package com.plugin.flutterimagetexture.flutterimagetexture

import android.R.attr
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.TextureRegistry.SurfaceTextureEntry
import org.json.JSONException
import org.json.JSONObject
import java.io.File


@RequiresApi(Build.VERSION_CODES.KITKAT)
class FlutterImageTexture(var context: Context?, var url: String?, var width: Float?, var height: Float?, entry: SurfaceTextureEntry, result: MethodChannel.Result?) {
    var mEntry: SurfaceTextureEntry?
    var surface: Surface?
    var result: MethodChannel.Result?
    var imgWidth: Int? = null
    var imgHeight: Int? = null
    val handler = Handler(Looper.getMainLooper())
    private fun draw(bitmap: Bitmap) {
        if (surface != null && surface!!.isValid) {
            mEntry!!.surfaceTexture().setDefaultBufferSize(imgWidth!!, imgHeight!!)
            val canvas = surface!!.lockCanvas(null)
            canvas.drawBitmap(bitmap, 0f, 0f, Paint())
            surface!!.unlockCanvasAndPost(canvas)
            Log.d("FlutterImageTexture", "entry_id=========" + mEntry!!.id())
            result!!.success(mEntry!!.id())
        }
    }

    fun dispose() {
        surface!!.release()
        surface = null
        mEntry!!.release()
        mEntry = null
        result = null
        context = null
        //        if(bitmap != null && !bitmap.isRecycled()){
//            bitmap.recycle();
//            bitmap = null;
//        }
    }

    private fun loadImage(context: Context?, url: String) {
        val ct = object : CustomTarget<File>() {
            override fun onLoadCleared(placeholder: Drawable?) {
                TODO("Not yet implemented")
            }

            override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                options.inPreferredConfig= Bitmap.Config.RGB_565
                BitmapFactory.decodeFile(resource.absolutePath, options)
                val outHeight = options.outHeight
                val outWidth = options.outWidth
                if(width == null)
                    imgWidth = outWidth
                else
                    imgWidth = dip2px(context, width)
                if(height == null)
                    imgHeight = outHeight
                else
                    imgHeight =  dip2px(context, height)
                BitmapFactory.decodeFile(resource.absolutePath, options)
                val inSampleSize = (outWidth/imgWidth!!)/(outHeight/imgHeight!!)
                options.inSampleSize = inSampleSize
                options.inJustDecodeBounds =false;
                val bitmap = BitmapFactory.decodeFile(resource.absolutePath, options)
                handler.post(object :Runnable{
                    override fun run() {
                        draw(bitmap)
                    }
                })
            }
        }

        Glide.with(context!!).downloadOnly().load(url).skipMemoryCache(true).dontAnimate().into(ct)
    }

    companion object {
        fun dip2px(context: Context?, dpValue: Float?): Int {
            val scale = context!!.resources.displayMetrics.density
            if (dpValue != null) {
                return (dpValue * scale + 0.5f).toInt()
            }
            return 0
        }
        fun calculateInSampleSize(outWidth: Int, outHeight: Int, reqWidth: Int, reqHeight: Int): Int {
            var inSampleSize = 1
            if (outWidth > reqWidth || outHeight > reqHeight) {
                val halfWidth = outWidth / 2
                val halfHeight = outHeight / 2
                while (halfWidth / inSampleSize >= reqWidth && halfHeight / inSampleSize >= reqHeight) {
                    inSampleSize *= 2
                }
            }
            return inSampleSize
        }
    }

    //    Bitmap bitmap;
    init {
        mEntry = entry
        surface = Surface(entry.surfaceTexture())
        this.result = result
        loadImage(context, url!!)
    }
}