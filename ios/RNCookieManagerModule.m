#import "RCTViewManager.h"
#import "RCTBridge.h"
#import "RCTView.h"

@interface RNCookieManager : RCTViewManager
@end


@implementation RNCookieManager

RCT_EXPORT_MODULE()
RCT_EXPORT_VIEW_PROPERTY(hidden, BOOL)

-(UIView *)view {
    return [[RCTView alloc] init];
}

@end
