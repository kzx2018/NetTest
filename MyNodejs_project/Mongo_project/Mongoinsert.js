var MongoClient = require('mongodb').MongoClient;
var url = "mongodb://localhost:27017/";
 
MongoClient.connect(url, { useNewUrlParser: true }, function(err, db) {
    if (err) throw err;
    var dbo = db.db("railway");
    var myobj =  [
        { origin_city: 'Beijing', destination_city: 'Shanghai', date: '2019-11-2',time:'8',purchased:'0'},
	{ origin_city: 'Beijing', destination_city: 'Shanghai', date: '2019-11-2',time:'10',purchased:'0'},
	{ origin_city: 'Beijing', destination_city: 'Guangzhou', date: '2019-11-2',time:'11',purchased:'0'},	
	{ origin_city: 'Beijing', destination_city: 'Shanghai', date: '2019-11-3',time:'8',purchased:'0'},
	{ origin_city: 'Beijing', destination_city: 'Guangzhou', date: '2019-11-3',time:'10',purchased:'0'},
	{ origin_city: 'Beijing', destination_city: 'Guangzhou', date: '2019-11-3',time:'11',purchased:'0'},
	{ origin_city: 'Shanghai', destination_city: 'Beijing', date: '2019-11-2',time:'8',purchased:'0'},
	{ origin_city: 'Shanghai', destination_city: 'Guangzhou', date: '2019-11-2',time:'10',purchased:'0'},
	{ origin_city: 'Shanghai', destination_city: 'Guangzhou', date: '2019-11-2',time:'11',purchased:'0'},
	{ origin_city: 'Shanghai', destination_city: 'Beijing', date: '2019-11-3',time:'8',purchased:'0'},
	{ origin_city: 'Shanghai', destination_city: 'Beijing', date: '2019-11-3',time:'10',purchased:'0'},
	{ origin_city: 'Shanghai', destination_city: 'Guangzhou', date: '2019-11-3',time:'11',purchased:'0'},
	{ origin_city: 'Guangzhou', destination_city: 'Shanghai', date: '2019-11-2',time:'8',purchased:'0'},
	{ origin_city: 'Guangzhou', destination_city: 'Beijing', date: '2019-11-2',time:'10',purchased:'0'},
	{ origin_city: 'Guangzhou', destination_city: 'Shanghai', date: '2019-11-2',time:'11',purchased:'0'},
	{ origin_city: 'Guangzhou', destination_city: 'Beijing', date: '2019-11-3',time:'8',purchased:'0'},
	{ origin_city: 'Guangzhou', destination_city: 'Beijing', date: '2019-11-3',time:'10',purchased:'0'},
	{ origin_city: 'Guangzhou', destination_city: 'Shanghai', date: '2019-11-3',time:'11',purchased:'0'},

       
       ];
    dbo.collection("works").insertMany(myobj, function(err, res) {
        if (err) throw err;
        console.log("插入的文档数量为: " + res.insertedCount);
        db.close();
    });
});
