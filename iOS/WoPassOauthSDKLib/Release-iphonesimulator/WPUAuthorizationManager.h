//
//  WPUAuthorizationManager.h
//  WoPassOauth
//
//  Created by htz on 15/8/23.
//  Copyright (c) 2015年 unisk. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, WPULoginPageMode) {
    WPULoginPageModal,
    WPULoginPageAlert
};

/**
 *  授权模式
 */
typedef NS_OPTIONS(NSUInteger, WPUAuthOptions){
    /**
     *  简化模式
     */
    WPUAuthImplicit = 1 << 0,
    /**
     *  授权码模式
     */
    WPUAuthCode = 1 << 1,
    /**
     *  过期自动刷新
     */
    WPUAUthAutoRefresh = 1 << 2,
};

typedef void(^WPUAuthorizeCompletedAction)(id authInfo, NSString * msg);
typedef void(^WPUGetUserDataCompletedAction)(id userData, id authInfo, NSString * msg);
typedef void(^WPULoginPageWillAppearAction)(void);


@interface WPUAuthorizationManager : NSObject

/**
 *  授权选项
 */
@property (nonatomic, assign)WPUAuthOptions authOptions;

/**
 *  授权状态
 *  isAuthorized = YES：用户已授权，并且未过期
 *  isAuthorized = NO：用户已授权，并且未过期
 */
@property (nonatomic, assign, readonly, getter=isAuthorized)BOOL authorize;

/**
 *  授权是否过期
 *  isExpired ＝ YES：授权已过期
 *  isExpired = NO：授权未过期
 */
@property (nonatomic, assign, readonly, getter=isExpired)BOOL expire;


/**
 *  创建manager对象
 *
 *  @param params   授权参数列表
 *  @param options  授权模式选项。warning: 简化模式不支持过期自动刷新
 *  @param delegate 代理
 *
 *  @return manager对象
 */

+ (instancetype)sharedManagerWithParams:(NSDictionary *)params
                                options:(WPUAuthOptions)options
                               delegate:(id)delegate;

/**
 *  获取manager对象
 *
 *  @return manager对象
 */
+ (instancetype)sharedManager;

/**
 *  手动授权，授权界面将在加载完毕后以modal的方式推出
 *
 *  @param authorizeCompletedAction  授权完成的回调
 *  @param loginPageWillAppearAction 授权界面即将推出的回调
 *  @param isNeeded                  isNeeded = YES: 如果用户使用的是联通蜂窝网络, SDK将从网络层获取用户手机号码, 如果用户使用的是非联通蜂窝网络, SDK将不进行此项操作; isNeeded = NO: SDK将不进行取号操作
 */
- (void)manualAuthorizationWithCompleted:(WPUAuthorizeCompletedAction)authorizeCompletedAction
            andLoginPageWillAppearAction:(WPULoginPageWillAppearAction)loginPageWillAppearAction
                 accessPhoneNumberNeeded:(BOOL) isNeeded;

/**
 *  自动授权，授权界面将在加载完毕后以弹窗的模式出现(若应用安全级别为0，则进行静默取号授权)
 *
 *  @param authorizeCompletedAction  授权完成的回调
 *  @param loginPageWillAppearAction 授权界面即将弹出的回调
 *  @param timeInterval              在timeInterval间隔后调用此方法
 */
- (void)autoAuthorizationWithCompleted:(WPUAuthorizeCompletedAction)authorizeCompletedAction
          andLoginPageWillAppearAction:(WPULoginPageWillAppearAction)loginPageWillAppearAction
                     delayTimeInterval:(NSInteger)timeInterval;

/**
 *  获取用户信息
 *  
 *  @param getUserDataCompletedAction 用户信息获取完毕的回调
 */
- (void)getUserDataWithCompletedAction:(WPUGetUserDataCompletedAction) getUserDataCompletedAction;

/**
 *  SSO handle
 *
 *  @param 应用被打开时传入的url
 */
- (void)handleURL:(NSURL *)url;

/**
 *  清除授权信息
 */
- (void)clearAuthInfo;

@end
