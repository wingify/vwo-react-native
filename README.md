
# vwo-react-native

## Getting started

`$ npm install vwo-react-native --save`

### Mostly automatic installation

`$ react-native link vwo-react-native`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `vwo-react-native` and add `VWOReactNative.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libVWOReactNative.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.vwo.VWOReactNativePackage;` to the imports at the top of the file
  - Add `new VWOReactNativePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':vwo-react-native'
  	project(':vwo-react-native').projectDir = new File(rootProject.projectDir, 	'../node_modules/vwo-react-native/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':vwo-react-native')
  	```


## Usage
```javascript
import VWOReactNative from 'vwo-react-native';

// TODO: What to do with the module?
VWOReactNative;
```
  