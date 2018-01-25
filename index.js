
import { NativeModules } from 'react-native';

const { VWO } = NativeModules;

VWO.variationForKeyWithDefaultValue = (key, defaultValue) => new Promise((resolve) => {
	VWO.variationForKey(key)
		.then((result) => {
            if (result === null) {
                resolve(defaultValue)
            } else {
                resolve(result)
            }
        })
		.catch((err) => {
			resolve(defaultValue);
		});
});


export default VWO;
