package com.plugin.flutterimagetexture.flutterimagetexture

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.TextureRegistry
import java.util.HashMap

@SuppressLint("LongLogTag")
@RequiresApi(Build.VERSION_CODES.KITKAT)
class FlutterImageTextureDelegate(context: Context){
    internal var context:Context? = null
    private val fluttetrImageHashMap = HashMap<String, FlutterImageTexture>()
    init {
        this.context = context;
    }

    fun loadImage(textures:TextureRegistry?, call:MethodCall, result:MethodChannel.Result){
        val entry = textures?.createSurfaceTexture()
        val width = call.argument<Double>("width")!!.toFloat()
        val height = call.argument<Double>("height")!!.toFloat()
        val url = call.argument<String>("url")!!
        Log.d("FlutterImageTextureDelegate", "entry_id=========" + entry?.id())
        fluttetrImageHashMap[entry?.id().toString()] = FlutterImageTexture(context, url, width, height, entry!!,result)
    }

    fun release(call:MethodCall){
        val textureId = call.argument<String>("id")
        val fluttetrImage = fluttetrImageHashMap[textureId]
        fluttetrImage!!.dispose()
        Log.d("FlutterImageTextureDelegate", "release_entry_id=========" + textureId)
        fluttetrImageHashMap.remove(textureId)
    }

    fun dispose(){
        context = null
    }
}