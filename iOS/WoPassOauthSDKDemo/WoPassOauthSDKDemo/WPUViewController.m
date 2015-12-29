//
//  ViewController.m
//  WoPassOauth
//
//  Created by htz on 15/6/23.
//  Copyright (c) 2015年 unisk. All rights reserved.
//

#import "WPUViewController.h"
#import "WPUAuthorizationManager.h"
#import "WPUChangeInfoViewController.h"


#define SCREEN_WIDTH ([UIScreen mainScreen].bounds.size.width)
#define SCREEN_HEIGHT ([UIScreen mainScreen].bounds.size.height)

#define kLabelH 50
#define kLabelW 250

@interface WPUViewController ()

@property (nonatomic, strong)UILabel *statusLabel;

@end

@implementation WPUViewController

#pragma mark - Constructors and Life cycle

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self setupButton];
    
    self.statusLabel.text = @"我是回调状态";
}


- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}




#pragma mark - Private Method

- (void)setupButton {
    
    NSArray *titleArray = @[@"验证授权", @"按级别授权", @"获取用户信息", @"清除登录信息", @"修改APPID APPSecret"];
    for (NSInteger i = 0; i < titleArray.count; i ++) {
        
        UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
        [button setTitle:titleArray[i] forState:UIControlStateNormal];
        [self.view addSubview:button];
        button.frame = CGRectMake(0, 0, 170, 40);
        button.tag = i;
        [button addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
        button.frame = CGRectMake((SCREEN_WIDTH - button.frame.size.width * 1.2) / 2, (80 * i) + 50, button.frame.size.width * 1.2, button.frame.size.height * 1.2);
        button.backgroundColor = [UIColor orangeColor];
        button.layer.cornerRadius = 7;
        button.layer.masksToBounds = YES;
    }
}



#pragma mark - Event Reponse



- (void)clickButton:(UIButton *)button {
    
    __weak typeof(self) weakSelf = self;
    
    if (button.tag < 4) {
        
        self.statusLabel.text = @"等待响应...";
    }
    
    switch (button.tag) {
        case 0: {
            
            button.userInteractionEnabled = YES;
            
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
            
            button.userInteractionEnabled = YES;
            
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
            
            button.userInteractionEnabled = YES;
            [[WPUAuthorizationManager sharedManager] getUserDataWithCompletedAction:^(id userData, id authInfo,NSString *msg) {
                
                button.userInteractionEnabled = YES;
                NSLog(@"%@", userData);
                UIAlertView *alert = [[UIAlertView alloc] initWithTitle:msg message:[NSString stringWithFormat:@"%@", userData] delegate:self cancelButtonTitle:@"确定" otherButtonTitles: nil];
                [alert show];
                weakSelf.statusLabel.text = msg;
            }];
        }
            break;
        case 3: {
            button.userInteractionEnabled = YES;
            [[WPUAuthorizationManager sharedManager] clearAuthInfo];
            self.statusLabel.text = @"授权信息清除成功";
        }
            break;
            
        case 4: {
            
            button.userInteractionEnabled = YES;
            WPUChangeInfoViewController *vc = [[WPUChangeInfoViewController alloc] init];
            [self presentViewController:vc animated:YES completion:nil];
        }
            
        default:
            break;
    }
}


#pragma mark - Delegate


#pragma mark - Getter and Setter

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



@end
