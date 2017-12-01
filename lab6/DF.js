// Teoria Współbieżnośi, implementacja problemu 5 filozofów w node.js
// Opis problemu: http://en.wikipedia.org/wiki/Dining_philosophers_problem
// 1. Dokończ implementację funkcji podnoszenia widelca (Fork.acquire).
// 2. Zaimplementuj "naiwny" algorytm (każdy filozof podnosi najpierw lewy, potem
//    prawy widelec, itd.).
// 3. Zaimplementuj rozwiązanie asymetryczne: filozofowie z nieparzystym numerem
//    najpierw podnoszą widelec lewy, z parzystym -- prawy.
// 4. Zaimplementuj rozwiązanie z kelnerem (opisane jako "Arbitrator solution"
//    w wikipedii).
// 5. Zaimplementuj rozwiążanie z jednoczesnym podnoszeniem widelców:
//    filozof albo podnosi jednocześnie oba widelce, albo żadnego.
// 6. Uruchom eksperymenty dla różnej liczby filozofów i dla każdego wariantu
//    implementacji zmierz średni czas oczekiwania każdego filozofa na dostęp
//    do widelców. Wyniki przedstaw na wykresach.

var tmpTime = 0;

var Fork = function() {
    this.state = 1;
    return this;
}

Fork.prototype.acquire = function(cb) {
  this.BEBacquire(cb, 1);
}

Fork.prototype.BEBacquire = function(cb, delay){
  var self = this;
  setTimeout(function() {
      if(self.state == 1){
        self.state = 0;
        if(cb) cb();
      }else{
        self.BEBacquire(cb, 2*delay);
      }
  }, delay);
}

Fork.prototype.release = function() {
    this.state = 1;
}

var Philosopher = function(id, forks) {
    this.id = id;
    this.forks = forks;
    this.f1 = id % forks.length;
    this.f2 = (id+1) % forks.length;
    return this;
}

Philosopher.prototype.acquireBoth = function(cb){
  this.BEBacquireBoth(cb, 1);
}

Philosopher.prototype.BEBacquireBoth = function(cb, delay){
  var forks = this.forks,
      f1 = this.f1,
      f2 = this.f2,
      id = this.id,
      self = this;

  setTimeout(function() {
    if((forks[f1].state == 1) && (forks[f2].state == 1)){
      self.forks[f1].state = 0;
      self.forks[f2].state = 0;
      if(cb) cb();
    }else{
      self.BEBacquireBoth(cb, delay*2);
    }
  }, delay);
}

Philosopher.prototype.releaseBoth = function(){
  this.forks[this.f1].release();
  this.forks[this.f2].release();
}

Philosopher.prototype.startNaive = function(count, timeEating, cb) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        self = this;

    if(count > 0){
      var startTime = new Date();
      forks[f1].acquire(function(){
        forks[f2].acquire(function(){
          tmpTime += new Date() - startTime;
          setTimeout(function() {
              //console.log(self.id + " je, mniam, mniam, mniam");
              forks[f2].release();
              forks[f1].release();
              self.startNaive(count - 1, timeEating, cb);
          }, timeEating);
        })
      });
    }else{
      console.log(self.id + " skonczyl jesc, bylo dobre");
      cb();
    }
} // dziala - natychmiast sie zaklescza

Philosopher.prototype.startAsym = function(count, timeEating, cb) {
    var forks = this.forks,
        id = this.id;

    if(id%2 == 0){
      this.f2 = id % forks.length;
      this.f1 = (id+1) % forks.length;
    }

    this.startNaive(count, timeEating, cb);
} // działa

var Conductor = function(forks){
  this.forks = forks;
  this.queue = [];
  return this;
}

Conductor.prototype.ask = function(ph, cb){
  var forks = this.forks,
    f1 = ph.f1,
    f2 = ph.f2;

  if((forks[f1].state == 1) && (forks[f2].state == 1)){
    forks[f1].state = 0;
    forks[f2].state = 0;
    if(cb) cb();
  }else{
    this.queue.push([ph, cb]);
  }
}

Conductor.prototype.inform = function(ph){
  var forks = this.forks,
    f1 = ph.f1,
    f2 = ph.f2;

  forks[f1].state = 1;
  forks[f2].state = 1;

  for(var i=0; i<this.queue.length; i++){
    var ph1 = this.queue[i][0];
    var cb = this.queue[i][1];
    if((forks[ph1.f1].state == 1) && (forks[ph1.f2].state == 1)){
      this.queue.splice(i, 1);
      forks[ph1.f1].state = 0;
      forks[ph1.f2].state = 0;
      if(cb) cb();
    }
  }
}

Philosopher.prototype.startConductor = function(count, conductor, timeEating, cb) {
    var forks = this.forks,
        f1 = this.f1,
        f2 = this.f2,
        id = this.id,
        self = this;

    if(count > 0){
      var startTime = new Date();
      conductor.ask(self, function(){
        tmpTime += new Date() - startTime;
        setTimeout(function() {
          //console.log(self.id + " je, mniam, mniam, mniam");
          conductor.inform(self);
          self.startConductor(count - 1, conductor, timeEating, cb);
        }, timeEating);
      });
    }else{
      console.log(self.id + " skonczyl jesc, bylo dobre");
      cb();
    }
}


Philosopher.prototype.startBoth = function(count, timeEating, cb){
  var forks = this.forks,
      id = this.id,
      self = this;

      if(count > 0){
        var startTime = new Date();
        self.acquireBoth(function(){
          tmpTime += new Date() - startTime;
          setTimeout(function() {
            //console.log(self.id + " je, mniam, mniam, mniam");
            self.releaseBoth();
            self.startBoth(count - 1, timeEating, cb);
          }, timeEating);
        });
      }else{
        console.log(self.id + " skonczyl jesc, bylo dobre");
        cb();
      }
} // działa


var timeEating = 10;
var c = 25;
var times = [];

function measure(N, step, limit, measureCb){
  if(N < limit){
    var cbN = N;
    tmpTime = 0;

    var cb = function(){
      cbN--;
      if(cbN == 0){
        times.push( tmpTime / (N * c) );
        measure(N + step, step, limit, measureCb);
      }
    }

    var forks = [];
    var philosophers = [];
    var conductor = new Conductor(forks);

    for (var i = 0; i < N; i++) {
        forks.push(new Fork());
    }

    for (var i = 0; i < N; i++) {
        philosophers.push(new Philosopher(i, forks));
    }

    for (var i = 0; i < N; i++) {
        //philosophers[i].startNaive(c, timeEating);
        //philosophers[i].startAsym(c, timeEating, cb);
        //philosophers[i].startConductor(c, conductor, timeEating, cb);
        philosophers[i].startBoth(c, timeEating, cb);
    }
  }else{
    measureCb();
  }
}

var START = 5;
var STEP = 5;
var LIMIT = 100;
var fs = require('fs');

measure(START, STEP, LIMIT, function(){
  for(var i = 0; i < times.length; i++){
    fs.appendFileSync('data.csv', (i+1)*STEP + "," + times[i] + ",\"Both\"\n");
  }
});
