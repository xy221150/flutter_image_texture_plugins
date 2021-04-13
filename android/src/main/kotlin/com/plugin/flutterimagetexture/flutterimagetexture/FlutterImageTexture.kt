package com.plugin.flutterimagetexture.flutterimagetexture

import android.R.attr.scaleHeight
import android.R.attr.scaleWidth
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.TextureRegistry.SurfaceTextureEntry
import java.io.File


@RequiresApi(Build.VERSION_CODES.KITKAT)
class FlutterImageTexture(var context: Context?, var url: String?, var width: Float, var height: Float, entry: SurfaceTextureEntry, result: MethodChannel.Result?) {
    var mEntry: SurfaceTextureEntry?
    var surface: Surface?
    var result: MethodChannel.Result?
    var imgWidth: Int? = null
    var imgHeight: Int? = null
    var handler : Handler? = Handler(Looper.getMainLooper())
    var bitmap:Bitmap? = null
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
        handler = null
        context = null
        if(bitmap != null && !bitmap!!.isRecycled()){
            bitmap!!.recycle();
            bitmap = null;
        }
    }

    private fun loadImage(context: Context?, url: String) {
        val ct = object : CustomTarget<File>() {
            override fun onLoadCleared(placeholder: Drawable?) {
                TODO("Not yet implemented")
            }

            override fun onResourceReady(resource: File, transition: Transition<in File>?) {
               //图片优化
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                options.inPreferredConfig= Bitmap.Config.RGB_565
                BitmapFactory.decodeFile(resource.absolutePath, options)
                val outHeight = options.outHeight
                val outWidth = options.outWidth
                BitmapFactory.decodeFile(resource.absolutePath, options)

                //当没传默认宽高时使用图片本身宽高
                if(width <= 0)
                    imgWidth = outWidth
                else
                    imgWidth = dp2px(context, width)
                if(height <= 0)
                    imgHeight = outHeight
                else
                    imgHeight =  dp2px(context, height)

                options.inSampleSize = calculateInSampleSize(outWidth,outHeight,imgWidth!!,imgHeight!! )
                options.inJustDecodeBounds =false

                val bm = BitmapFactory.decodeFile(resource.absolutePath, options)


                //因为图片大小改变重新获取一遍宽高
                if(width <= 0)
                    imgWidth = bm.width
                else
                    imgWidth = dp2px(context, width)
                if(height <= 0)
                    imgHeight = bm.height
                else
                    imgHeight =  dp2px(context, height)

                //缩放bitmap
                val matrix = Matrix()
                matrix.postScale((imgWidth!!.toFloat()/bm.width), (imgHeight!!.toFloat()/bm.height))
                bitmap = Bitmap.createBitmap(bm, 0, 0, bm.width, bm.height, matrix, true)
                //主线程内执行
                handler?.post(object :Runnable{
                    override fun run() {
                        draw(bitmap!!)
                    }
                })
            }
        }

        Glide.with(context!!).downloadOnly().load(url).skipMemoryCache(true).dontAnimate().into(ct)
    }

    companion object {
        fun dp2px(context: Context?, dpValue: Float?): Int {
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