
import 'dart:async';
import 'dart:io';
import 'dart:ui' as ui;
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'flutter_image_texture.dart';

class FlutterImageTextureWidget extends StatefulWidget {

  final String url;

  final double width;

  final double height;

  const FlutterImageTextureWidget({Key key, this.url, this.width, this.height}) : super(key: key);

  @override
  _FlutterImageTextureWidgetState createState() => _FlutterImageTextureWidgetState();
}

class _FlutterImageTextureWidgetState extends State<FlutterImageTextureWidget>{

  int textureId;

  double width;

  double height;
  
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    loadImage();
  }

  Future loadImage() async{
    ui.Image info = await getImageInfo(widget.url);
    width = widget.width??px2dp(info.width);
    height = widget.height??px2dp(info.height);

    textureId = await FlutterImageTexture.loadImg(widget.url,width,height);
    print("hashCode----------$hashCode");
    if(mounted)setState(() {});
  }


  double px2dp(int px){
    double dp = 0.0;
    double pixel = WidgetsBinding.instance.window.devicePixelRatio;
    dp = px/pixel;
    return Platform.isIOS?px.toDouble():dp;
  }


  static Future<ui.Image> getImageInfo(String url) async {
    ImageStream stream =  NetworkImage(url).resolve(ImageConfiguration.empty);
    Completer<ui.Image> completer =  Completer<ui.Image>();
    ImageStreamListener listener;
    listener = new ImageStreamListener(
            (ImageInfo frame, bool synchronousCall) {
          final ui.Image image = frame.image;
          completer.complete(image);
          stream.removeListener(listener);
        });
    stream.addListener(listener);
    return completer.future;
  }

  @override
  void didUpdateWidget(covariant FlutterImageTextureWidget oldWidget) {
    // TODO: implement didUpdateWidget
    super.didUpdateWidget(oldWidget);
    if(oldWidget.url!=widget.url){
      loadImage();
    }
  }

  @override
  Widget build(BuildContext context) {
    
    if(textureId == null){
      return Container(
        color: Colors.white,
      );
    }
    return Container(
      width: width,
      height: height,
      child:Texture(textureId: textureId),
    );
  }

  @override
  void dispose() {
    // TODO: implement dispose
    if(textureId!=null){
      FlutterImageTexture.release(textureId?.toString());
    }
    super.dispose();
  }
}
