
import { NativeModules } from 'react-native';

const { VWO } = NativeModules;

VWO.variationForKeyWithDefaultValue = (key, defaultValue) => new Promise((resolve) => {
	try {
		let val = VWO.variationForKey(key);
		resolve(val);
	} catch (e) {
		resolve(defaultValue);
	}
});


export default VWO;
