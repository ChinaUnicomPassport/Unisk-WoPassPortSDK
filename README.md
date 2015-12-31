# ReadMe
沃+通行证iOS、andriod平台SDK为第三方应用开发者提供了简单易用的API接口，在联通开放平台上申请成为开发者并通过审核后，即可方便的使用授权登录等功能。也请大家将使用中遇到的问题、需求以及bug尽量提交至GitHub，我们将努力为大家打造一个方便易用的SDK，谢谢！
# 名词解释
| 名词        | 说明    | 
| --------    | :-----  | 
| NET取号          |通过运营商移动⺴络自动取号。|
| 隐式登录          |SDK在后台进⾏NET取号,取到手机号后以alert形式弹出loading,并进行授权,授权成功后loading消失,用户不参与交互。若取号失败则不进行弹窗。|
|一键登录           |SDK在后台进⾏NET取号,取到手机号后以alert形式弹出授权⻚面,⽤户确认登录后授权成功。若取号失败则不进行弹窗。|
|验证登录           |SDK在后台进⾏NET取号,不论取号成功与否,登录界面都将以modal的形式推出。|
| client_id      | 第三方申请到的AppKey。|
| client_secret  | 第三方申请到的AppSecret。|
| redirect_uri   | 第三方申请时填写的回调地址。| 

# 接入文档
### 1、iOS接入文档
https://github.com/ChinaUnicomPassport/Unisk-WoPassPortSDK/blob/master/iOS/沃通行证iOS平台说明SDK文档.pdf
### 2、andriod接入文档
https://github.com/ChinaUnicomPassport/Unisk-WoPassPortSDK/blob/master/andriod/中国联通沃登录Android平台说明SDK文档1231.pdf
# 适配iOS9
由于iOS9添加了部分新特性，为了确保本SDK的正常使用，需要进行以下配置：
### 1、添加对SSL的支持
新的iOS9默认为网络连接建立TLS 1.2 SSL，可通过以下两种方式进行解决：
- I.在info.plist中添加白名单
- 
    <key>NSAppTransportSecurity</key>
    <dict>
        <key>NSExceptionDomains</key>
        <dict>
            <key>api.wo.cn</key>
            <dict>
                <key>NSIncludesSubdomains</key>
                <true/>
                <key>NSThirdPartyExceptionAllowsInsecureHTTPLoads</key>
                <true/>
                <key>NSThirdPartyExceptionRequiresForwardSecrecy</key>
                <false/>
            </dict>
        </dict>
    </dict>

- II.可将NSAllowsArbitraryLoads设置为YES
-
    <key>NSAppTransportSecurity</key>
    <dict>
        <key>NSAllowsArbitraryLoads</key>
        <true/>
    </dict>

### 2、添加对应用跳转的支持
在info.plist中的LSApplicationQueriesSchemes加入woPassPort，如下：

-
    <key>LSApplicationQueriesSchemes</key>
    <array>
        <string>woPassPort</string>
    </array>

### 3、bitcode
添加了对bitcode的支持


