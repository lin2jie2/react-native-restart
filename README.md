
# react-native-restart

## About

only test on Android, React Native 0.61.0-rc1

core code copy from `fengjundev/React-Native-Remote-Update`

## Usage
```javascript
import RNRestart from 'react-native-restart';

// jsBundleFile = null => /data/user/0/com.demo/files/index.android.bundle
// JavaScript: = RNFetchBlob.fs.dirs.DocumentDir + "/index.android.bundle"
// Java: = getFileDirs() + "/index.android.bundle"
RNRestart.restartApp(jsBundleFile = null);
```
