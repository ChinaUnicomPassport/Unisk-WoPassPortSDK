# ReadMe
沃+通行证iOS、andriod平台SDK为第三方应用开发者提供了简单易用的API接口，在联通开放平台上申请成为开发者并通过审核后，即可方便的使用授权登录等功能。也请大家将使用中遇到的问题、需求以及bug尽量提交至GitHub，我们将努力为大家打造一个方便易用的SDK，谢谢！
# 名词解释
| 名词        | 说明    | 
| --------    | :-----  | 
| client_id      | 第三方申请到的AppKey。|
| client_secret  | 第三方申请到的AppSecret。|
| redirect_uri   | 第三方申请时填写的回调地址。| 
# 接入文档
### 1、iOS接入文档
https://github.com/ChinaUnicomPassport/Unisk-WoPassPortSDK/blob/master/iOS/wo%2B%E9%80%9A%E8%A1%8C%E8%AF%81iOS%E5%B9%B3%E5%8F%B0%E8%AF%B4%E6%98%8ESDK%E6%96%87%E6%A1%A3.pdf
### 2、andriod接入文档
https://github.com/ChinaUnicomPassport/Unisk-WoPassPortSDK/blob/master/andriod/wo%2B%E9%80%9A%E8%A1%8C%E8%AF%81Android%E5%B9%B3%E5%8F%B0%E8%AF%B4%E6%98%8ESDK%E6%96%87%E6%A1%A3.doc
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
### 2、bitcode
添加了对bitcode的支持



