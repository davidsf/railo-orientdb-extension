Railo OrientDB Extension
========================

A Railo Extension for OrientTechnologies OrientDB Document and Graph database (http://www.orientechnologies.com) in the style of the Railo MongoDB Extension. The Extension implements the OrientDB Java (native) API.


Deploy
------

Just copy the **/dist/railo-orientdb-extension.re** to your **/railo-server/context/deploy/** directory and after successful deployment restart Railo.


Usage
-----

Following files explain how to use the extension from Railo:

    test-orientdb-document.cfm
    test-orientdb-graph.cfm


Build
-----

To build a new version of the extension just run
    
    gradle build deployLocal


Dependencies
------------

Railo 4 (tested with 4.2)
OrientDB Version 1.7
TinkerPop 2.5


OrientDB Documentation
----------------------

Getting Started

    http://www.orientechnologies.com/getting-started/
    
Graph & Document Database Documentation

    https://github.com/orientechnologies/orientdb/wiki/Document-Database
    https://github.com/orientechnologies/orientdb/wiki/Graph-Database-Tinkerpop
    
Overall Documentation and API

    https://github.com/orientechnologies/orientdb/wiki
    http://www.orientechnologies.com/javadoc/latest/index.html


License
-------

Copyright 2014 Roland Ringgenberg

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
