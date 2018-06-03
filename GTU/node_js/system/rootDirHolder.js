

const DEFAULT_ROOT = "D:/workstuff/gtu-test-code/GTU/node_js/";

class RootDirHolder {
	
	constructor(rootDirName = DEFAULT_ROOT, rootFileName = ""){
		this.baseDir = rootDirName;
		this.fileName = rootFileName;
		global.rootDirHolder = this;
		console.log(`project root dir : ${this.baseDir}`);
	}

	path(relactivePath){
		return this.baseDir + "/" + relactivePath;
	}
}

module.exports.RootDirHolder = RootDirHolder;

function main(){
	// __filename
	// __dirname
	new RootDirHolder(__dirname, __filename);
	console.log(rootDirHolder.path("xxxxxxx"));
	
	new RootDirHolder();
	console.log(rootDirHolder.path("xxxxxxx"));
}

// main();