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

RCT_EXPORT_METHOD(variationForKey:(NSString *)key callback:(RCTResponseSenderBlock)callback){
    id variation = [VWO variationForKey:key];
    if (variation == nil) callback(@[@"Variation not found"]);
    else callback(@[[NSNull null], variation]);
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
