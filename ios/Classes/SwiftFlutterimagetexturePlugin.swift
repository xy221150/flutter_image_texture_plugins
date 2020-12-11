import Flutter
import UIKit

public class SwiftFlutterimagetexturePlugin: NSObject, FlutterPlugin {
    
  public static var renders:NSMutableDictionary? = nil
  
  public static var textures:FlutterTextureRegistry? = nil
  
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutterimagetexture", binaryMessenger: registrar.messenger())
    textures = registrar.textures()
    renders = NSMutableDictionary()
    let instance = SwiftFlutterimagetexturePlugin()
    
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
