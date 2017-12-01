
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

console.log(filePaths.length);

// asynchroniczne przetwarzanie plików z filePaths
async.eachSeries(filePaths, function(file, cb) {
    var count = 0;
    fs.createReadStream(file).on('data', function(chunk) {
        count += chunk.toString('utf8')
            .split(/\r\n|[\n\r\u0085\u2028\u2029]/g)
            .length-1;
    }).on('end', function() {
				globalCount += count;
        cb();
    }).on('error', function(err) {
        console.error(err);
        cb(err);
    });
}, function() {
        console.log("Sum of all: " + globalCount);
        var endTime = new Date();
        var exTime = endTime - startTime;
        console.log("Program executed in: " + exTime + " ms");
    });
