var mongoose = require('mongoose');
var Schema = mongoose.Schema;

mongoose.connect('mongodb://localhost/railway',{useNewUrlParser:true});
var produtSchema = new Schema({ origin_city: String, destination_city: String, date: String,time:String,purchased:String});
module.exports = mongoose.model('work', produtSchema);



