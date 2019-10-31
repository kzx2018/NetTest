var qs = require('querystring');

exports.sendHtml = function(res, html) { 
  res.setHeader('Content-Type', 'text/html');
  res.setHeader('Content-Length', Buffer.byteLength(html));
  res.end(html);
}

exports.parseReceivedData = function(req, cb) { 
  var body = '';
  req.setEncoding('utf8');
  req.on('data', function(chunk){ body += chunk });
  req.on('end', function() {
    var data = qs.parse(body);
    cb(data);
  });
};

exports.actionForm = function(id, path, label) { 
  var html = '<form method="POST" action="' + path + '">' +
    '<input type="hidden" name="id" value="' + id + '">' +
    '<input type="submit" value="' + label + '" />' +
    '</form>';
  return html;
};

exports.delete = function(db, req, res) {
  exports.parseReceivedData(req, function(work) { 
    db.query(
       "UPDATE work SET purchased=0 WHERE id=?", 
      [work.id], 
      function(err) {
        if (err) throw err;
		var html = "<h1>Delete Successfully!</h1>";
        exports.show(db, res, html); 
      }
    );
  });
};

exports.purchase = function(db, req, res) {
  var query = "SELECT * FROM work WHERE id=?" ;
  exports.parseReceivedData(req, function(work) { 
    db.query(
       query,[work.id], 
       function(err,result) {
        if (err) throw err;
	for(var i in result){
		var n = result[i].purchased
	}
   
	if(n==0)  {
		db.query( "UPDATE work SET purchased=1 WHERE id=?", [work.id], 
        	function(err) {if (err) throw err;
			var html = "<h1>Purchase ticket successfully!</h1>";
        		exports.show(db, res,html);  });
     	          }
	else {
	var html = "<h1>You have purchased this ticket!</h1>";
        exports.show(db, res,html); 
	     }
                           }
  
          );
      });
};


exports.page = function(res){
  var html = exports.workFormHtml();
  exports.sendHtml(res, html); 
};

exports.show = function(db, res, html){
  var query = "SELECT * FROM work " +
	"WHERE purchased=?";
    db.query(
      query,1,
      function(err,rows) {
        if (err) throw err;
		html += exports.workEDlistHtml(rows);
		exports.sendHtml(res, html);
      }
    );

};

exports.distinguish = function(db, req, res){
  var query = "SELECT * FROM work " +
	"WHERE origin_city=? and destination_city=? and date=?"+
    "ORDER BY date DESC";
  exports.parseReceivedData(req, function(work) { 
    db.query(
      query,[work.origin_city, work.destination_city, work.date], 
      function(err,rows) {
        if (err) throw err;
	var jslength=0;

	for(var js2 in rows){
	jslength++;
	}
	
        if(jslength==0) exports.errorpage(res);
        else{
	 var html =  exports.workHitlistHtml(rows); 
	 exports.sendHtml(res, html);
	}
      }
    );
  });
};


exports.errorpage = function(res)
{
var html = '<form method="GET" action="/bookingrailway">' +
	'<p>The tickets only for three cities(Beijing,Shanghai,Guangdong),and the two days(2019-11-2,2019-11-3)<p/>' +
	'<input type="submit" value="Try again" />' +
    '</form>';
  exports.sendHtml(res, html);
};

exports.workFormHtml = function() {
  var html ='<h1>     Welcome to Booking Railway Tickets</h1>' + 
    '<form method="POST" action="/railwaytable">' + 
    '<p>  From:<input name="origin_city" type="text"><p/>' +
    '<p>  To:<input name="destination_city" type="text"><p/>' + 
    '<p>  Date :<input name="date" type="text"><p/>' +
    '<input type="submit" value="Booking" />' +
    '</form>';
  return html;
};

exports.workPurchaseForm = function(id) { 
  return exports.actionForm(id, '/purchased', 'Purchase');
}
exports.workDeleteForm = function(id) { 
  return exports.actionForm(id, '/delete', 'Delete');
}
exports.workBackForm = function(id) { 
  return exports.actionForm(id, '/back', 'Back');
}



exports.workHitlistHtml = function(rows) {
  var html = '<table>';
    html += '<tr>';
    html += '<th>origin_city</th>';
    html += '<th>destination_city</th>';
	html += '<th>date</th>';
	html += '<th>hours</th>';
    html += '</tr>';
  for(var i in rows) { 
    html += '<tr>';
    html += '<td>' + rows[i].origin_city + '</td>';
    html += '<td>' + rows[i].destination_city + '</td>';
    html += '<td>' + rows[i].date + '</td>';
    html += '<td>' + rows[i].hours + '</td>';
    html += '<td>' + exports.workPurchaseForm(rows[i].id) + '</td>';
    html += '</tr>';
  }
  html += '</table>';
  html += '<form method="GET" action="/bookingrailway">';
  html += '<input type="submit" value="Back" />' ;
  return html;
};


exports.workEDlistHtml = function(rows) {
  var html = '<table>';
    html += '<tr>';
    html += '<th>origin_city</th>';
    html += '<th>destination_city</th>';
	html += '<th>date</th>';
	html += '<th>hours</th>';
    html += '</tr>';
  for(var i in rows) { 
    html += '<tr>';
    html += '<td>' + rows[i].origin_city + '</td>';
    html += '<td>' + rows[i].destination_city + '</td>';
    html += '<td>' + rows[i].date + '</td>';
    html += '<td>' + rows[i].hours + '</td>';
    html += '<td>' + exports.workDeleteForm(rows[i].id) + '</td>';
    html += '</tr>';
  }
  html += '</table>';
  html += '<form method="GET" action="/bookingrailway">';
  html += '<input type="submit" value="Back" />' ;
  return html;
};





/*
exports.showArchived = function(db, res) {
  exports.show(db, res, true); 
};
exports.workHitlistHtml = function(rows) {
  var html = '<table>';
  for(var i in rows) { 
    html += '<tr>';
    html += '<td>' + rows[i].origin_city + '</td>';
    html += '<td>' + rows[i].destination_city + '</td>';
    html += '<td>' + rows[i].date + '</td>';
    html += '<td>' + rows[i].hours + '</td>';
    if (!rows[i].purchased) { 
      html += '<td>' + exports.workPurchaseForm(rows[i].id) + '</td>';
    }
    html += '<td>' + exports.workDeleteForm(rows[i].id) + '</td>';
    html += '</tr>';
  }
  html += '</table>';
  return html;
};*/




/*
exports.add = function(db, req, res) {
  exports.parseReceivedData(req, function(work) { 
    db.query(
      "INSERT INTO work (origin_city, destination_city, date, hours) " + 
      " VALUES (?, ?, ?)",
      [work.hours, work.date, work.description], 
      function(err) {
        if (err) throw err;
        exports.show(db, res); 
      }
    );
  });
};


exports.purchase = function(db, req, res) {
  exports.parseReceivedData(req, function(work) { 
    db.query(
      "UPDATE work SET purchased=1 WHERE id=?", 
      [work.id], 
      function(err) {
        if (err) throw err;
		var html = "<h1>Purchase ticket successfully!</h1>";
        exports.show(db, res,html); 
      }
    );
  });
};

exports.nopurchase = function(db, req, res) {
  exports.parseReceivedData(req, function(work) { 
    db.query(
      "UPDATE work SET purchased=1 WHERE id=?", 
      [work.id], 
      function(err) {
        if (err) throw err;
		var html = "<h1>You have purchased this ticket!</h1>";
        exports.show(db, res,html); 
      }
    );
  });
};

*/
