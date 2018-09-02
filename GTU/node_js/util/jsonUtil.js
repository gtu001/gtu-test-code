const fs = require('fs');


class JsonUtil {
	static readJson_sync(file){
		return JSON.parse(fs.readFileSync(file, 'utf8'));
	};

	static readJson_async(file){
		var obj;
		fs.readFile(file, 'utf8', function (err, data) {
			if (err) throw err;
			obj = JSON.parse(data);
		});
	};
}

module.exports.JsonUtil = JsonUtil;