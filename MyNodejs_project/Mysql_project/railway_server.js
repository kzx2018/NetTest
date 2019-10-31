var http = require('http');
var work = require('./railway');
var mysql = require('/home/kangzhaoxiang/node_modules/mysql');

var db = mysql.createConnection({
  host:     '127.0.0.1',
  user:     'root',
  password: '123456',
  database: 'railway'
});

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

db.query(
  "CREATE TABLE IF NOT EXISTS work (" 
  + "id INT(10) NOT NULL AUTO_INCREMENT, " 
  + "origin_city VARCHAR(50) NOT NULL,"
  + "destination_city VARCHAR(50) NOT NULL,"
  + "date DATE, " 
  + "hours DECIMAL(5,2) DEFAULT 0, " 
  + "purchased INT(1) DEFAULT 0, "  
  + "PRIMARY KEY(id))",
  function(err) { 
    if (err) throw err;
    console.log('Server started...');
    server.listen(4035, '127.0.0.1'); 
  }
);
