import { NativeModules } from 'react-native';

const { VWO } = NativeModules;

VWO.objectForKey = (key, defaultValue) => new Promise((resolve) => {
	VWO.__objectForKey__(key)
		.then((result) => {
            if (result === null || result === undefined) {
                resolve(defaultValue)
            } else {
                resolve(results)
            }
        })
		.catch((err) => {
			resolve(defaultValue);
		});
});

export default VWO;