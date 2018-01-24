
import { NativeModules } from 'react-native';

const { VWO } = NativeModules;

VWO.variationForKeyWithDefaultValue = (key, defaultValue) => new Promise((resolve) => {
	VWO.variationForKey(key)
		.then(resolve)
		.catch((err) => {
			resolve(defaultValue);
		});
});


export default VWO;
