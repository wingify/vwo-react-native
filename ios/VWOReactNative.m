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

RCT_EXPORT_METHOD(setOptOut:(BOOL)optOut){
    [VWO setOptOut:optOut];
}

RCT_EXPORT_METHOD(version:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    NSString * version = [VWO version];
    resolve(version);
}

RCT_EXPORT_METHOD(launch:(NSString*)apiKey
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    [VWO launchForAPIKey:apiKey completion:^{
        resolve([NSNull null]);
    } failure:^(NSString * _Nonnull error) {
        reject(@"VWO_LAUNCH_ERR", error, nil);
    }];
}


RCT_EXPORT_METHOD(variationForKey:(NSString *)key
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
    id variation = [VWO variationForKey:key];
    resolve(variation);
}

RCT_EXPORT_METHOD(trackConversion: (NSString *)goal){
    [VWO trackConversion:goal];
}

RCT_EXPORT_METHOD(trackConversionWithValue: (NSString *)goal withValue:(double) value){
    [VWO trackConversion:goal withValue:value];
}

RCT_EXPORT_METHOD(setCustomVariable: (NSString *) key value:(NSString *) value){
    [VWO setCustomVariable: key withValue:value];
}

@end

