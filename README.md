# ☰ Satori

TBD

## Satori examples

__Credentials__

Use the following credentials to sign in example applications.

<table>
<tr>
<th>Username</th><th>Password</th>
</tr>
<tr>
<td>admin</td><td>123</td>
</tr>
<tr>
<td>user</td><td>123</td>
</tr>
<tr>
<td>darkhelmet</td><td>ludicrousspeed</td>
</tr>
</table>
 

### Jersey webapps

__Events__

Multi-user event manager example application.

Features

* Shiro based security
* Internationalization
* Handlebars templating
* BVal validation
* JQuery+Bootstrap (ui, date/time, ajax, gmaps, &c.)
* Caching (Cache-control and ETags)
* And more cool things!

[![Screenshot 0](http://mfornos.github.com/satori/shots/event02s.png)](http://mfornos.github.com/satori/shots/event02.png)
[![Screenshot 1](http://mfornos.github.com/satori/shots/event00s.png)](http://mfornos.github.com/satori/shots/event00.png)
[![Screenshot 2](http://mfornos.github.com/satori/shots/event01s.png)](http://mfornos.github.com/satori/shots/event01.png)


__Todos__

One page Todo example application adapted from backbone.js.

Features

* Shiro based security
* Internationalization
* Underscore.js+Backbone.js
* Atmosphere Pub/Sub

[![Screenshot 0](http://mfornos.github.com/satori/shots/todos00s.png)](http://mfornos.github.com/satori/shots/todos00.png)

__Metro__

CIA fact book search and world map visualization.

[![Screenshot 0](http://mfornos.github.com/satori/shots/metro00s.png)](http://mfornos.github.com/satori/shots/metro00.png)
[![Screenshot 1](http://mfornos.github.com/satori/shots/metro01s.png)](http://mfornos.github.com/satori/shots/metro01.png)

### Scalate webapps

__Track__

Simple task tracking application.

Features

* Jade templating
* File upload

[![Screenshot 0](http://mfornos.github.com/satori/shots/track00s.png)](http://mfornos.github.com/satori/shots/track00.png)

### Resteasy webapps

__Monsters__

Simple monsters social network skeleton.

Features

* Shiro based security
* Internationalization
* Handlebars templating
* BVal validation
* TinkerPop Orient GraphDB

[![Screenshot 0](http://mfornos.github.com/satori/shots/monsters00s.png)](http://mfornos.github.com/satori/shots/monsters00.png)


## Running the examples

First of all [fork the Satori repo](https://help.github.com/articles/fork-a-repo)

_satori-dropwizard-example_
    
     $ cd satori/examples/satori-dropwizard-example
     $ mvn package
     $ java -jar target/satori-dropwizard-example-0.0.1-SNAPSHOT.jar server src/main/resources/hello.yaml

_satori-example_

     $ cd satori/examples/satori-example
     $ mvn jetty:run

_satori-resteasy-example_
    
     $ cd satori/examples/satori-resteasy-example 
     $ mvn jetty:run
    
_satori-scalate-example_
    
     $ cd satori/examples/satori-scalate-example 
     $ mvn jetty:run

    
    