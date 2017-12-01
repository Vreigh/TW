
var globalCount = 0;
var filePaths = [];

var file = require('file'),
	fs = require('fs'),
	async = require('async');

var startTime = new Date()
//asynchroniczne listowanie plików do filePaths
file.walkSync("PAM08", function(path, dirs, files) {
    async.eachSeries(files, function(f, cb) {
        filePaths.push(path + '/' + f);
        cb();
    })}
);
// synchroniczne przetwarzanie plików z filePaths
function loop(n){
  if(n > 0){
    var count = 0;
    fs.createReadStream(filePaths[n - 1]).on('data', function(chunk) {
        count += chunk.toString('utf8')
            .split(/\r\n|[\n\r\u0085\u2028\u2029]/g)
            .length-1;
    }).on('end', function() {
        globalCount += count;
        loop(n - 1);
    }).on('error', function(err) {
        console.error(err);
    });
  }else{
    console.log("Sum of all: " + globalCount);
    var endTime = new Date();
    var exTime = endTime - startTime;
    console.log("Program executed in: " + exTime + " ms");
  }
}

loop(filePaths.length);
