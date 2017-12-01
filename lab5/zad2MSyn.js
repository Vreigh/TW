var globalCount = 0;
var filePaths = [];

var file = require('file'),
	fs = require('fs'),
	async = require('async');

function asyncCount(i){
  var count = 0;
  fs.createReadStream(filePaths[i]).on('data', function(chunk) {
      count += chunk.toString('utf8')
          .split(/\r\n|[\n\r\u0085\u2028\u2029]/g)
          .length-1;
  }).on('end', function() {
      globalCount += count;
      sharedCallback();
  }).on('error', function(err) {
      console.error(err);
  });
}

var startTime = new Date()
//asynchroniczne listowanie plików do filePaths
file.walkSync("PAM08", function(path, dirs, files) {
    async.eachSeries(files, function(f, cb) {
        filePaths.push(path + '/' + f);
        cb();
    })}
);

// asynchroniczne przetwarzanie plików z filePaths
var  n = filePaths.length;
var sharedCallback = function(){
  n--;
  if(n == 0){
    console.log("Sum of all: " + globalCount);
    var endTime = new Date();
    var exTime = endTime - startTime;
    console.log("Program executed in: " + exTime + " ms");
  }
}

for(var i=0; i<filePaths.length; i++){
  asyncCount(i);
}
