import { NativeModules } from 'react-native';
#forks
const { VWO } = NativeModules;

VWO.objectForKey = (key, defaultValue) => new Promise((resolve) => {
	VWO.__objectForKey__(key)
		.then((result) => {
            if (result === null || result === undefined) {
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
