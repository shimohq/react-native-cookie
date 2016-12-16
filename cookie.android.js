import { NativeModules } from 'react-native';
import cookie from 'cookie';

const CLEAR_RETRY_COUNT = 10;

export default {
    get(url:String, name: String): Promise<Object|String> {
        return NativeModules.RNCookieManager.getCookie(url).then((value: String): Object => {
            if (name && value) {
                return cookie.parse(value)[name] || null;
            } else {
                return value ? cookie.parse(value) : null;
            }
        });
    },

    set(url:String, name: String, value: any, options?: Object): Promise {
        return NativeModules.RNCookieManager.setCookie(url, cookie.serialize(name, value, options));
    },

    async clear(url?: String): Promise {
        await NativeModules.RNCookieManager.clearCookie(url || null);

        if (!url) {
            return null;
        }

        // A work around to ensure cookies has been cleared on Android
        const data = await this.get(url);

        return data ? new Promise((resolve, reject) => {
            let retryCount = 0;
            const self = this;
            async function retry() {
                retryCount++;
                await NativeModules.RNCookieManager.clearCookie(url);

                if (!await self.get(url)) {
                    resolve(null);
                } else if (retryCount <= CLEAR_RETRY_COUNT) {
                    retry();
                } else {
                    reject(null);
                }
            }
            retry();
        }) : null;
    }
};
