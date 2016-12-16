import React, { Component } from 'react';
import { AppRegistry, StyleSheet, WebView } from 'react-native';
import Cookie from 'react-native-cookie';

const url = 'http://bing.com/';

export default class cookie extends Component {
    render() {
        Cookie.get(url).then((data) => {
            console.log(`get cookie from ${url}: ${JSON.stringify(data)}`);
        });
        Cookie.set(url, 'foo', 'bar').then(() => {
            console.log(`set cookie 'foo=bar' for ${url}`);
        });

        Cookie.clear(url).then(() => {
            console.log(`clear all cookie from ${url}`);
        });

        return (
            <WebView
                style={styles.container}
                source={{uri: url}}
            />
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1
    }
});

AppRegistry.registerComponent('cookie', () => cookie);


