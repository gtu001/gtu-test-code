const process =  require("process"); 


class RootDirHolder {
	constructor(rootDirName, rootFileName){
		this.baseDir = rootDirName;
		this.fileName = rootFileName;
		global.rootDirHolder = this;
		console.log(`system root dir : ${this.baseDir}`);
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
	console.log(">>>"+process.cwd());
}

// main();