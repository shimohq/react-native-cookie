# react-native-cookie

A cookie manager module for react-native(Supports for both iOS and Android).

# Installation

```bash
# install library from npm
npm install react-native-cookie --save
# link native code
react-native link react-native-cookie
```

# Usage

## Import

```javascript
import Cookie from 'react-native-cookie';
```

## Get

`Cookie.get(url:String, name?: String): Promise<Object|String>`

```javascript

// get all cookies which are belonged to 'http://bing.com/'
Cookie.get('http://bing.com/').then((cookie) => console.log(cookie));

// get cookie named 'foo' from 'http://bing.com/'
Cookie.get('http://bing.com/', 'foo').then((cookie) => console.log(cookie));

```

## Set

`Cookie.set(url:String, name: String, value: any, options?: Object): Promise`

### Options
The following options are available for now

* domain
Specifies the value for the Domain Set-Cookie attribute. By default, no domain is set, and most clients will consider the cookie to apply to only the current domain.

* expires
Specifies the Date object to be the value for the Expires Set-Cookie attribute. By default, no expiration is set, and most clients will consider this a "non-persistent cookie" and will delete it on a condition like exiting a web browser application.

* path
Specifies the value for the Path Set-Cookie attribute. By default, the path is considered the "default path". By default, no maximum age is set, and most clients will consider this a "non-persistent cookie" and will delete it on a condition like exiting a web browser application

```javascript
// set cookie 'foo=bar' for 'http://bing.com/'
Cookie.set('http://bing.com/', 'foo', 'bar').then(() => console.log('success'));

// set cookie 'foo=bar' for 'http://bing.com/' with options:
Cookie.set('http://bing.com/', 'foo', 'bar', {
    path: 'ditu',
    domain: 'cn.bing.com'
}).then(() => console.log('success'));

```

## Clear

`Cookie.clear(url?: String): Promise`

```javascript
// clear all cookies for all domains
Cookie.clear();

// clear all cookies for 'http://bing.com'
Cookie.clear('http://bing.com');
```
