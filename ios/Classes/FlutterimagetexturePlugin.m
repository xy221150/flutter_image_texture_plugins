#import "FlutterimagetexturePlugin.h"
#if __has_include(<flutterimagetexture/flutterimagetexture-Swift.h>)
#import <flutterimagetexture/flutterimagetexture-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutterimagetexture-Swift.h"
#endif

@implementation FlutterimagetexturePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterimagetexturePlugin registerWithRegistrar:registrar];
}
@end
