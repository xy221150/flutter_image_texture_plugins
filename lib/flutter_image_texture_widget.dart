

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'flutterimagetexture.dart';

class FlutterImageTextureWidget extends StatefulWidget {

  final String url;

  final double width;

  final double height;

  const FlutterImageTextureWidget({Key key, this.url, this.width, this.height}) : super(key: key);

  @override
  _FlutterImageTextureWidgetState createState() => _FlutterImageTextureWidgetState();
}

class _FlutterImageTextureWidgetState extends State<FlutterImageTextureWidget> {

  int textureId;
  
  @override
  void initState() {
    // TODO: implement initState
    super.initState();
    loadImage();
  }

  Future loadImage() async{
    textureId = await Flutterimagetexture.loadImg(widget.url,widget.width,widget.height);
    if(mounted)setState(() {});
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
      width: widget.width,
      height: widget.height,
      child:Texture(textureId: textureId),
    );
  }

  @override
  void dispose() {
    // TODO: implement dispose
    if(textureId!=null){
      Flutterimagetexture.release(textureId?.toString());
    }
    super.dispose();
  }
}
