//
//  VWOReactNative.m
//  VWOReactNative
//
//  Created by Rishabh Shukla on 06/08/17.
//  Copyright © 2017 Facebook. All rights reserved.
//

#include "VWOReactNative.h"
#import <React/RCTLog.h>
#import <VWO/VWO.h>

@implementation VWOReactNative

RCT_EXPORT_MODULE(VWO);

RCT_EXPORT_METHOD(launchAsynchronously:(NSString*)apiKey){
  
  [VWO launchForAPIKey:apiKey];
  
}

RCT_EXPORT_METHOD(launchSynchronously:(NSString*)apiKey){
  
  [VWO launchSynchronouslyForAPIKey:apiKey];
  
}

RCT_EXPORT_METHOD(launchAsynchronouslyWithCallback: (NSString *)apiKey callback:(RCTResponseSenderBlock)callback){
  
  [VWO launchForAPIKey:apiKey completion:^{
    callback(@[[NSNull null]]);
  }];
  
}

RCT_EXPORT_METHOD(getVariationForKey: (NSString *)key callback:(RCTResponseSenderBlock)callback){
  
//  NSLog(@"%@", key);
  
  id variation = [VWO variationForKey:key];
  
  callback(@[[NSNull null], variation]);

}

RCT_EXPORT_METHOD(getVariationForKeyWithDefaultValue: (NSString *)key defaultValue:(id)defaultValue callback:(RCTResponseSenderBlock)callback){
  
 id variation =  [VWO variationForKey:key defaultValue:defaultValue];
  
  callback(@[[NSNull null], variation]);

}

RCT_EXPORT_METHOD(markConversionForGoal: (NSString *)goal){
  
  [VWO markConversionForGoal:goal];
  
}

RCT_EXPORT_METHOD(markConversionForGoalWithValue: (NSString *)goal value:(double) value){
  
  [VWO markConversionForGoal:goal withValue:value];
  
}

RCT_EXPORT_METHOD(setCustomVariable: (NSString *) key value:(NSString *) value){
  
  [VWO setCustomVariable: key withValue:value];
  
}

@end
#import <Foundation/Foundation.h>
