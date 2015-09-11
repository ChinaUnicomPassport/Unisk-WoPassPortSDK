//
//  ViewController.m
//  WoPassOauthSDKDemo
//
//  Created by htz on 15/9/8.
//  Copyright (c) 2015年 unisk. All rights reserved.
//

#import "ViewController.h"
#import "WPUAuthorizationManager.h"


#define SCREEN_WIDTH ([UIScreen mainScreen].bounds.size.width)
#define SCREEN_HEIGHT ([UIScreen mainScreen].bounds.size.height)

#define kLabelH 50
#define kLabelW 250

@interface ViewController ()

@property (nonatomic, strong)UILabel *statusLabel;

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self setupButton];
    
    self.statusLabel.text = @"我是回调状态";
}


- (UILabel *)statusLabel {
    if (!_statusLabel) {
        _statusLabel = [[UILabel alloc] init];
        _statusLabel.frame = CGRectMake((SCREEN_WIDTH - kLabelW) / 2, 450, kLabelW, kLabelH);
        _statusLabel.backgroundColor = [UIColor lightGrayColor];
        _statusLabel.textColor = [UIColor blackColor];
        _statusLabel.textAlignment = NSTextAlignmentCenter;
        [self.view addSubview:_statusLabel];
    }
    return _statusLabel;
}

- (void)setupButton {
    
    NSArray *titleArray = @[@"主动授权模式", @"自动授权模式", @"获取用户信息", @"清除登录信息"];
    for (NSInteger i = 0; i < titleArray.count; i ++) {
        
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        [button setTitle:titleArray[i] forState:UIControlStateNormal];
        [self.view addSubview:button];
        [button sizeToFit];
        button.tag = i;
        [button addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
        button.frame = CGRectMake((SCREEN_WIDTH - button.frame.size.width) / 2, (100 * i) + 50, button.frame.size.width * 1.2, button.frame.size.height * 1.2);
        button.backgroundColor = [UIColor orangeColor];
        button.layer.cornerRadius = 7;
        button.layer.masksToBounds = YES;
    }
}

- (void)clickButton:(UIButton *)button {
    
    __weak typeof(self) weakSelf = self;
    self.statusLabel.text = @"等待响应...";
    switch (button.tag) {
        case 0: {
            
            button.userInteractionEnabled = NO;
            
            [[WPUAuthorizationManager sharedManager] manualAuthorizationWithCompleted:^(id authInfo, NSString *msg) {
                
                button.userInteractionEnabled = YES;
                NSLog(@"%@", authInfo);
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:msg message:[NSString stringWithFormat:@"%@", authInfo] delegate:self cancelButtonTitle:@"确定" otherButtonTitles: nil];
                [alert show];
                weakSelf.statusLabel.text = msg;
            } andLoginPageWillAppearAction:^{
                
                NSLog(@"登录界面即将弹出");
                weakSelf.statusLabel.text = @"登录界面即将弹出";
            } accessPhoneNumberNeeded:YES];
        }
            break;
        case 1: {
            
            button.userInteractionEnabled = NO;
            
            [[WPUAuthorizationManager sharedManager] autoAuthorizationWithCompleted:^(id authInfo, NSString *msg) {
                
                button.userInteractionEnabled = YES;
                NSLog(@"%@", authInfo);
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:msg message:[NSString stringWithFormat:@"%@", authInfo] delegate:self cancelButtonTitle:@"确定" otherButtonTitles: nil];
                [alert show];
                weakSelf.statusLabel.text = msg;
            } andLoginPageWillAppearAction:^{
                
                NSLog(@"登录界面即将弹出");
                weakSelf.statusLabel.text = @"登录界面即将弹出";
            } delayTimeInterval:0];
        }
            break;
        case 2: {
            
            button.userInteractionEnabled = NO;
            [[WPUAuthorizationManager sharedManager] getUserDataWithCompletedAction:^(id userData, id authInfo, NSString *msg) {
                
                button.userInteractionEnabled = YES;
                NSLog(@"%@", userData);
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:msg message:[NSString stringWithFormat:@"%@", userData] delegate:self cancelButtonTitle:@"确定" otherButtonTitles: nil];
                [alert show];
                weakSelf.statusLabel.text = msg;
            }];
        }
            break;
        case 3: {
            
            [[WPUAuthorizationManager sharedManager] clearAuthInfo];
            self.statusLabel.text = @"授权信息清除成功";
        }
            break;
            
        default:
            break;
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


@end
