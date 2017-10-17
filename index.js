
import { NativeModules } from 'react-native';

const { VWO } = NativeModules;

VWO.variationForKeyWithDefaultValue = (key, defaultValue, callback) => {
    VWO.variationForKey(key, function (error, variation) {
        if(error) {
            callback(null, defaultValue);
        } else {
            callback(null, variation);
        }
    });
};


export default VWO;
