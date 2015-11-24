//
//  AppDelegate.m
//  WoPassOauth
//
//  Created by htz on 15/6/23.
//  Copyright (c) 2015年 unisk. All rights reserved.
//

#import "AppDelegate.h"
#import "WPUViewController.h"
#import "WPUAuthorizationManager.h"

@interface AppDelegate ()

@property (nonatomic, strong)WPUAuthorizationManager *authManager;


@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    self.window = [[UIWindow alloc] initWithFrame:CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height)];
    self.window.rootViewController = [[WPUViewController alloc] init];
    [self.window makeKeyAndVisible];
    
//     level 0

//    self.authManager = [WPUAuthorizationManager sharedManagerWithParams:@{
//                                                                          @"client_id" : @"abc",
//                                                                          @"redirect_uri" :     @"http://www.anywhere",
//                                                                          @"client_secret" : @"123"
//                                                                          } options:WPUAuthImplicit delegate:self];
    

//     level 2
//    self.authManager = [WPUAuthorizationManager sharedManagerWithParams:@{
//                                                                          @"client_id" : @"0E6E5A4747B5BF9E",
//                                                                          @"redirect_uri" :     @"http://www.anywhere"
//                                                                          } options:WPUAuthImplicit delegate:self];

//    level 1
    self.authManager = [WPUAuthorizationManager sharedManagerWithParams:@{
                                                                          @"client_id" : @"unisk",
                                                                          @"redirect_uri" :     @"http://www.unisk.cn",
                                                                          @"client_secret" : @"UNISK"
                                                                          } options:WPUAuthCode delegate:self];
    
    
//    // 云盘
//    self.authManager = [WPUAuthorizationManager sharedManagerWithParams:@{
//                                                                          @"client_id" : @"78e696134d334befa4ca651270ed1037",
//                                                                          @"redirect_uri" :     @"http://www.wocloud.com.cn/"
//                                                                          } options:WPUAuthImplicit delegate:self];

    return YES;
}

- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url {
    
    [self.authManager handleURL:url];
    return YES;
}

- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation {
    
    [self.authManager handleURL:url];
    return YES;
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<NSString *,id> *)options {
    
    [self.authManager handleURL:url];
    return YES;
}

@end
