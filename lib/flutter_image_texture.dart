
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterImageTexture {
  static const MethodChannel _channel =
      const MethodChannel('FlutterImageTexture');

  static Future<int> loadImg(String url,double width,double height) async {
    final args = <String, dynamic>{"url":url,"height":height??0,"width":width??0};
    return await _channel.invokeMethod("load", args);
  }

  static Future<String> release(String id) async {
    final args = <String, dynamic>{"id": id};
    return await _channel.invokeMethod("release", args);
  }
}
