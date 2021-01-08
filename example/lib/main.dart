import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutterimagetexture/flutterimagetexture.dart';
import 'package:flutterimagetexture/flutter_image_texture_widget.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: FlutterImageTextureWidget(url: "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594790452308&di=e461f1fd43aa67086364ccb7f6812085&imgtype=0&src=http%3A%2F%2Fpic.kekenet.com%2F2013%2F0522%2F46061369189999.jpg",width: 300,height: 300,),
        ),
      ),
    );
  }
}
