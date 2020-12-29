package com.plugin.flutterimagetexture.flutterimagetexture

import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.view.TextureRegistry
import java.util.HashMap


class FlutterImageTextureDelegate(messenger: BinaryMessenger, handler: MethodChannel.MethodCallHandler, context: Context){
    internal var methodChannel : MethodChannel? = null
    internal val METHOD_NAME = "ImageTexture"
    internal var context:Context? = null
    private val fluttetrImageHashMap = HashMap<String, FlutterImageTexture>()
    init {
        this.context = context;
        methodChannel = MethodChannel(messenger,METHOD_NAME)
        methodChannel?.setMethodCallHandler(handler)

    }

    fun loadImage(textures:TextureRegistry?,call:MethodCall,result:MethodChannel.Result){
        val entry = textures?.createSurfaceTexture()
        val width = call.argument<Float>("width")!!
        val height = call.argument<Float>("height")!!
        val url = call.argument<String>("url")
        fluttetrImageHashMap[entry?.id().toString()] = FlutterImageTexture(context, url, width, height, entry)
        result.success(entry?.id())
    }

    fun release(call:MethodCall){
        val textureId = call.argument<String>("id")
        val fluttetrImage = fluttetrImageHashMap[textureId]
        fluttetrImage!!.dispose()
        fluttetrImageHashMap.remove(textureId)
    }

    fun dispose(){
        if(methodChannel != null){
            methodChannel = null
        }
        context = null
    }
}