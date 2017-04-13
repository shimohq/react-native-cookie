
#import "RNCookieManager.h"
#import "RCTConvert.h"

@implementation RNCookieManager

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(getCookie:(NSURL *)url
                   resolver:(RCTPromiseResolveBlock)resolve
                   rejecter:(RCTPromiseRejectBlock)reject)
{
    NSMutableDictionary *result = [NSMutableDictionary dictionary];
    NSArray *cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookiesForURL:url];
    if ([cookies count]) {
        for (NSHTTPCookie *cookie in cookies) {
            [result setObject:cookie.value forKey:cookie.name];
        }
        resolve(result);
    } else {
        resolve(NULL);
    }

}

RCT_EXPORT_METHOD(setCookie:(NSURL *) url
                       name:(NSString *)name
                      value:(NSString *)value
                      props:(NSDictionary *)props
                   resolver:(RCTPromiseResolveBlock)resolve
                   rejecter:(RCTPromiseRejectBlock)reject)
{
    NSMutableDictionary *cookieProperties = [NSMutableDictionary dictionary];

    [cookieProperties setObject:url.host forKey:NSHTTPCookieOriginURL];
    [cookieProperties setObject:name forKey:NSHTTPCookieName];
    [cookieProperties setObject:value forKey:NSHTTPCookieValue];

    NSString *domain = [RCTConvert NSString:props[@"domain"]];
    [cookieProperties setObject:domain ? domain : url.host forKey:NSHTTPCookieDomain];

    NSString *path = [RCTConvert NSString:props[@"path"]];
    [cookieProperties setObject:path ? path : @"/" forKey:NSHTTPCookiePath];

    NSNumber *expires = [RCTConvert NSNumber:props[@"expires"]];
    if (expires) {
        [cookieProperties setObject:[NSDate dateWithTimeIntervalSince1970:[expires doubleValue]] forKey:NSHTTPCookieExpires];
    };

    NSHTTPCookie *cookie = [NSHTTPCookie cookieWithProperties:cookieProperties];
    [[NSHTTPCookieStorage sharedHTTPCookieStorage] setCookie:cookie];

    resolve(NULL);
}

RCT_EXPORT_METHOD(clearCookieFromURL:(NSURL *)url
                     resolver:(RCTPromiseResolveBlock)resolve
                     rejecter:(RCTPromiseRejectBlock)reject)
{

    NSHTTPCookieStorage *cookieStorage = [NSHTTPCookieStorage sharedHTTPCookieStorage];
    NSArray *cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookiesForURL:url];
    if ([cookies count]) {
        for (NSHTTPCookie *cookie in cookies) {
            [cookieStorage deleteCookie:cookie];
        }
    }

    resolve(NULL);
}

RCT_EXPORT_METHOD(clearCookies:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{

    NSHTTPCookieStorage *cookieStorage = [NSHTTPCookieStorage sharedHTTPCookieStorage];
    for (NSHTTPCookie *cookie in cookieStorage.cookies) {
        [cookieStorage deleteCookie:cookie];
    }

    resolve(NULL);
}

@end
