//
//  VWOReactNative.m
//  VWOReactNative
//
//  Created by Rishabh Shukla on 06/08/17.
//  Copyright © 2017 Facebook. All rights reserved.
//

#import <VWO/VWO.h>
#import "VWOReactNative.h"
#import <React/RCTLog.h>
#import <React/RCTConvert.h>

#pragma mark - RCTConvert(VWOLogLevel)
@implementation RCTConvert(VWOLogLevel)
RCT_ENUM_CONVERTER(VWOLogLevel, (@{
                                   @"VWOLogLevelDebug" : @(VWOLogLevelDebug),
                                   @"VWOLogLevelInfo" : @(VWOLogLevelInfo),
                                   @"VWOLogLevelWarning" : @(VWOLogLevelWarning),
                                   @"VWOLogLevelError" : @(VWOLogLevelError),
                                   @"VWOLogLevelNone" : @(VWOLogLevelNone),
                                   }), VWOLogLevelError, integerValue)
@end

#pragma mark - VWOReactNative
@implementation VWOReactNative

RCT_EXPORT_MODULE(VWO);

- (NSDictionary *)constantsToExport {
    return @{ @"logLevelDebug"  : @(VWOLogLevelDebug),
              @"logLevelInfo"   : @(VWOLogLevelInfo),
              @"logLevelWarning": @(VWOLogLevelWarning),
              @"logLevelError"  : @(VWOLogLevelError),
              @"logLevelOff"    : @(VWOLogLevelNone)};
};

+ (BOOL)requiresMainQueueSetup { return TRUE; }

RCT_EXPORT_METHOD(setLogLevel:(VWOLogLevel)level){
    [VWO setLogLevel:level];
}

RCT_EXPORT_METHOD(version:(RCTResponseSenderBlock)callback){
    NSString * version = [VWO version];
    callback(@[[NSNull null], version]);
}

RCT_EXPORT_METHOD(launch:(NSString*)apiKey){
    [VWO launchForAPIKey:apiKey];
}

RCT_EXPORT_METHOD(launchWithCallback: (NSString *)apiKey completion:(RCTResponseSenderBlock)completion){
    [VWO launchForAPIKey:apiKey completion:^{
        completion(@[[NSNull null]]);
    } failure:^(NSString * _Nonnull error) {
        completion(@[error]);
    }];
}

    //RCT_EXPORT_BLOCKING_SYNCHRONOUS_METHOD(launchSynchronously:(NSString*)apiKey timeout:(NSTimeInterval)timeout){
    //    [VWO launchSynchronouslyForAPIKey:apiKey timeout:timeout];
    //    return nil;
    //}

RCT_EXPORT_METHOD(variationForKey:(NSString *)key
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    id variation = [VWO variationForKey:key];
    if (variation != nil) resolve(variation);
    else reject(@"No variation", @"No variation was found for key", nil);
}

RCT_EXPORT_METHOD(markConversionForGoal: (NSString *)goal){
    [VWO markConversionForGoal:goal];
}

RCT_EXPORT_METHOD(markConversionForGoalWithValue: (NSString *)goal withValue:(double) value){
    [VWO markConversionForGoal:goal withValue:value];
}

RCT_EXPORT_METHOD(setCustomVariable: (NSString *) key value:(NSString *) value){
    [VWO setCustomVariable: key withValue:value];
}

@end
