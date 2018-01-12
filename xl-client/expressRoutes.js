var express = require('express');
var path = require('path');
var bodyParser = require('body-parser');
var myip = require('quick-local-ip');

module.exports = function(){
  var app = new express();
  app.set('views', __dirname + '/pages');
  app.engine('html', require('ejs').renderFile);
  app.use(bodyParser.json());
  app.use(bodyParser.urlencoded({ extended: true }));

  app.get('/game/:userID/:gameID',function(req, res){
    var ip = "localhost"//myip.getLocalIP4();
    res.render("index.html", {userData : req.params, ip: ip})
  })
  app.get('/createnew',function(req, res){
    var ip = "localhost"//myip.getLocalIP4();
    res.render("createnew.html", {userData : req.params, ip : ip});
  })

  app.get('/',function(req, res){
    res.redirect("/createnew")
  })
  return app;
}
