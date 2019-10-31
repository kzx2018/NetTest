var http = require('http');
var work = require('./railway');
var mongoose = require('mongoose');
var db = require('./createdb.js');

var server = http.createServer(function(req, res) {
  switch (req.method) {
    case 'POST': 
      switch(req.url) {
        case '/railwaytable':
	  work.distinguish(db, req, res);
          break;
        case '/purchased':
          work.purchase(db, req, res);
          break;
        case '/delete':
          work.delete(db, req, res);
          break;
	
      }
      break;
    case 'GET': 
      switch(req.url) {
        case '/bookingrailway':
          work.page(res);
          break;
      }
      break;
  }
});
console.log('Server started...');
server.listen(4035, '127.0.0.1'); 


