//
//  WPUChangeInfoViewController.m
//  WoPassOauth
//
//  Created by htz on 15/11/18.
//  Copyright © 2015年 unisk. All rights reserved.
//
#define SCREEN_WIDTH ([UIScreen mainScreen].bounds.size.width)
#define SCREEN_HEIGHT ([UIScreen mainScreen].bounds.size.height)
#define kFieldWidth 200
#define kFieldHeight 40
#define kButtonWidth 200
#define kButtonHeight 40


#import "WPUChangeInfoViewController.h"
#import "WPUAuthorizationManager.h"

typedef NS_ENUM(NSUInteger, WPUTextFieldType) {
    WPUTextFieldTypeID = 0xff,
    WPUTextFieldTypeURI,
    WPUTextFieldTypeSecret,
    WPUTextFieldTypeOption,
};

@interface WPUChangeInfoViewController ()

@end

@implementation WPUChangeInfoViewController

#pragma mark - Constructors and Life cycle

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    [self setupTextView];
    [self setuoButton];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


#pragma mark - Private Method

- (void)setupTextView {
    
    //    @"client_id" : @"abc",
    //    @"redirect_uri" :     @"http://www.anywhere",
    //    @"client_secret" : @"123"
    NSArray *array = @[@"client_id", @"redirect_uri", @"client_secret", @"authOption"];
    for (NSInteger i = 0; i < array.count; i ++ ) {
        UITextField *textField = [[UITextField alloc] init];
        textField.tag = WPUTextFieldTypeID + i;
        textField.placeholder = array[i];
        textField.frame = CGRectMake((SCREEN_WIDTH - kFieldWidth) / 2, (80 * i) + 50, kFieldWidth, kFieldHeight);
        textField.backgroundColor = [UIColor lightGrayColor];
        [self.view addSubview:textField];
        
        if (i == 3) {
            textField.keyboardType = UIKeyboardTypeNumberPad;
        }
    }
}


- (void)setuoButton {
    
    UIButton *button = [UIButton buttonWithType:UIButtonTypeCustom];
    [button setTitle:@"好了" forState:UIControlStateNormal];
    [self.view addSubview:button];
    [button addTarget:self action:@selector(clickButton:) forControlEvents:UIControlEventTouchUpInside];
    button.frame = CGRectMake((SCREEN_WIDTH - kButtonWidth) / 2, SCREEN_HEIGHT * 0.7, kButtonWidth, kButtonHeight);
    button.backgroundColor = [UIColor orangeColor];
    button.layer.cornerRadius = 7;
    button.layer.masksToBounds = YES;
    
    [self.view addSubview:button];
}


#pragma mark - Event Reponse

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    
    [self.view endEditing:YES];
}

- (void)clickButton:(UIButton *) button {
    
    //    @"client_id" : @"abc",
    //    @"redirect_uri" :     @"http://www.anywhere",
    //    @"client_secret" : @"123"
    NSString *ID = [[self.view viewWithTag:WPUTextFieldTypeID] text];
    NSString *uri = [[self.view viewWithTag:WPUTextFieldTypeURI] text];
    NSString *sercret = [[self.view viewWithTag:WPUTextFieldTypeSecret] text];
    NSString *option = [[self.view viewWithTag:WPUTextFieldTypeOption] text];

    NSDictionary *params = nil;
    
    params = @{
               @"client_id" : ID,
               @"redirect_uri" : uri,
               @"client_secret" : sercret
               };
    
        
    [[WPUAuthorizationManager sharedManager] setWithAuthParams:params options:[option integerValue]];
    
    [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
}



#pragma mark - Delegate









#pragma mark - Getter and Setter







#pragma mark - Public
@end
