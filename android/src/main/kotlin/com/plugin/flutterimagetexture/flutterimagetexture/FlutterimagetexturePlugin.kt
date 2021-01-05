package com.plugin.flutterimagetexture.flutterimagetexture

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.view.TextureRegistry

/** FlutterimagetexturePlugin */
class FlutterimagetexturePlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var flutterImageTextureDelegate: FlutterImageTextureDelegate
  private lateinit var textures: TextureRegistry

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "FlutterImageTexture")
    textures = flutterPluginBinding.textureRegistry
    flutterImageTextureDelegate = FlutterImageTextureDelegate(flutterPluginBinding.binaryMessenger,this,flutterPluginBinding.applicationContext)
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when(call.method){
      "load"->{
        flutterImageTextureDelegate.loadImage(textures,call,result)
      }
      "release"->{
        flutterImageTextureDelegate.release(call)
      }
    }
      
    
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
    flutterImageTextureDelegate.dispose()
  }
}
