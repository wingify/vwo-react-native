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

RCT_EXPORT_METHOD(version:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  NSString * version = [VWO version];
  resolve(version);
}

- (VWOConfig *)vwoConfigFromDictionary:(NSDictionary *)configDict {
  VWOConfig *config = [VWOConfig new];
  config.disablePreview = [configDict[@"disablePreview"] boolValue];
  config.optOut = [configDict[@"optOut"] boolValue];
  config.customVariables = configDict[@"customVariables"];
  return config;
}

RCT_EXPORT_METHOD(launch:(NSString*)apiKey
                  config:(NSDictionary *)config
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  VWOConfig *vwoConfig = [self vwoConfigFromDictionary:config];
  [VWO launchForAPIKey:apiKey config:vwoConfig completion:^{
    resolve([NSNull null]);
  } failure:^(NSString * _Nonnull error) {
    reject(@"VWO_LAUNCH_ERR", error, nil);
  }];
}

RCT_EXPORT_METHOD(intForKey:(NSString *)key
                  defaultValue:(int)defaultValue
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  int variation = [VWO intForKey:key defaultValue:defaultValue];
  resolve(@(variation));
}

RCT_EXPORT_METHOD(floatForKey:(NSString *)key
                  defaultValue:(double)defaultValue
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  double variation = [VWO doubleForKey:key defaultValue:defaultValue];
  resolve(@(variation));
}

RCT_EXPORT_METHOD(boolForKey:(NSString *)key
                  defaultValue:(BOOL)defaultValue
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  BOOL variation = [VWO boolForKey:key defaultValue:defaultValue];
  resolve(@(variation));
}

RCT_EXPORT_METHOD(stringForKey:(NSString *)key
                  defaultValue:(nullable NSString *)defaultValue
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  NSString *variation = [VWO stringForKey:key defaultValue:defaultValue];
  resolve(variation);
}


RCT_EXPORT_METHOD(__objectForKey__:(NSString *)key
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  id variation = [VWO objectForKey:key defaultValue:nil];
  resolve(variation);
}

RCT_EXPORT_METHOD(variationNameForTestKey:(NSString *)testKey
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject) {
  NSString *name = [VWO variationNameForTestKey:testKey];
  resolve(name);
}

RCT_EXPORT_METHOD(trackConversion: (NSString *)goal){
  [VWO trackConversion:goal];
}

RCT_EXPORT_METHOD(trackConversionWithValue: (NSString *)goal withValue:(double) value){
  [VWO trackConversion:goal withValue:value];
}

@end
