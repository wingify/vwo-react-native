
# vwo-react-native

# Getting started

`$ npm install https://github.com/wingify/vwo-react-native`

## Automatic installation

`$ react-native link vwo-react-native`

### iOS
1. Add VWO Dependency to your ios/podfile file `pod 'VWO', '~>2.0.0-beta7'`
2. `cd ios && pod install`

### Android
1. Add this in your `android/build.gradle` file
   ```groovy
   maven {
            url 'https://raw.githubusercontent.com/wingify/vwo-mobile-android/hybrid/'
         }
   ```

## Manual installation


### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `vwo-react-native` and add `VWOReactNative.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libVWOReactNative.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Add VWO Dependency to your ios/podfile file `pod 'VWO', '~>2.0.0-beta7'`
5. `cd ios && pod install`
6. Run your project (`Cmd+R`)<

### Android

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
4. Add this in your `android/build.gradle` file
   ```groovy
   maven {
            url 'https://raw.githubusercontent.com/wingify/vwo-android-snapshot/hybrid/'
         }
   ```


## Documentation

http://developers.vwo.com/v3/reference#react-native-guide
