#import "FlutterimagetexturePlugin.h"
#import "DuiaflutterextexturePresenter.h"

NSObject<FlutterTextureRegistry> *textures;
NSMutableDictionary<NSNumber *, DuiaflutterextexturePresenter *> *renders;

@implementation FlutterimagetexturePlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterimagetexturePlugin *plugin = [[FlutterimagetexturePlugin alloc] init];
    FlutterMethodChannel *channel = [FlutterMethodChannel methodChannelWithName:@"FlutterImageTexture" binaryMessenger:registrar.messenger];
    [registrar addMethodCallDelegate:plugin channel:channel];
    renders = [[NSMutableDictionary alloc] init];
    textures = registrar.textures;
}

- (void)handleMethodCall:(FlutterMethodCall *)call result:(FlutterResult)result{
    if([call.method isEqualToString:@"load"]){
        NSString *imageStr = call.arguments[@"url"];
        Boolean asGif = [call.arguments[@"asGif"] boolValue];
        CGFloat width = [call.arguments[@"width"] floatValue]*[UIScreen mainScreen].scale;
        CGFloat height = [call.arguments[@"height"] floatValue]*[UIScreen mainScreen].scale;

        CGSize size = CGSizeMake(width, height);
        
        DuiaflutterextexturePresenter *render = [[DuiaflutterextexturePresenter alloc] initWithImageStr:imageStr size:size asGif:asGif];
        int64_t textureId = [textures registerTexture:render];

        render.updateBlock = ^{
            [textures textureFrameAvailable:textureId];
        };
        [renders setObject:render forKey:[NSString stringWithFormat:@"%@",@(textureId)]];
       
        result(@(textureId));
    }else if([call.method isEqualToString:@"release"]){
        if (call.arguments[@"id"]!=nil && ![call.arguments[@"id"] isKindOfClass:[NSNull class]]) {
            DuiaflutterextexturePresenter *render = [renders objectForKey:call.arguments[@"id"]];
            [renders removeObjectForKey:call.arguments[@"id"]];
            [render dispose];
            NSString *textureId =  call.arguments[@"id"];
        
            [textures unregisterTexture:@([call.arguments[@"id"] integerValue]).longValue];
        }
    }else {
        result(FlutterMethodNotImplemented);
      }
}
@end
