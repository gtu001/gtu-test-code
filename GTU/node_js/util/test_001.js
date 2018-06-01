const {RootDirHolder} = require("D:/workstuff/gtu-test-code/GTU/node_js/system/rootDirHolder.js");
new RootDirHolder();

const checkSelf = require(rootDirHolder.path("/util/checkSelf"));
const jsonUtil = require(rootDirHolder.path("/util/jsonUtil"));
const numberUtil = require(rootDirHolder.path("/util/numberUtil"));

const fs = require("fs");

console.log("<<<" + typeof(fs) + " , " + fs);
        console.log("----------------------------------------start");
        for(let i in fs){
            console.log("---" + i);
        }
        console.log("----------------------------------------end");