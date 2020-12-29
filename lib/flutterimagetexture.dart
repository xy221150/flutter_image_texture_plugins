
import 'dart:async';

import 'package:flutter/services.dart';

class Flutterimagetexture {
  static const MethodChannel _channel =
      const MethodChannel('FlutterImageTexture');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }


  static Future<int> loadImg(String url,double width,double height) async {
    final args = <String, dynamic>{"url":url,"height":height,"width":width};
    return await _channel.invokeMethod("load", args);
  }

  static Future<String> release(String id) async {
    final args = <String, dynamic>{"id": id};
    return await _channel.invokeMethod("release", args);
  }
}
