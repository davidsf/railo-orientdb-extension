<cfsetting showdebugoutput="no">

<cfscript>

    dump("====== OrientDB Create new database =====")
    //railoOrientDB = OrientDBConnect("MyGraphDatabase", "orientDBServer:2424", "graph", "root", "root")
    //Unfortunately this doesn't work yet from within Railo, not clear why, create databases on server via bin/console orientdb> create database remote:localhost:2424/MyGraphDatabase root dbrootpass plocal graph

    dump("====== OrientDB Connect =====")
    railoOrientDB = OrientDBConnect("GratefulDeadConcerts", "orientDBServer:2424", "graph")
    dump(railoOrientDB)

    dump("------ OrientDB Factory -----")
    dump(var:railoOrientDB.getOrientDBFactory(), label:"graph.getOrientDBFactory()", expand:false)

    dump("------ orientDB Infos -------");
    dump(var:railoOrientDB.getName(), label:"db.getName()")

    dump("====== Create Vertexes and Edges =====")
    luca = railoOrientDB.addVertex("Person")
    //luca = railoOrientDB.getVertex("11:0")
    luca.setProperty("name", "Luca")
    dump(var:luca, label:"Person Luca")
    dump(luca.getId())
    //railoOrientDB.removeVertex(vertex)

    marko = railoOrientDB.addVertex("Person")
    //marko = railoOrientDB.getVertex("11:1")
    marko.setProperty("name", "Marko")
    dump(var:marko, label:"Person Marko")
    dump(marko.getId())
    //railoOrientDB.removeVertex(marko)

    //railoOrientDB.dropVertexType("Person")

    lucaIsFriendOfMarko = railoOrientDB.addEdge(luca, marko, "is_friend_of");
    //lucaIsFriendOfMarko.setProperty("meetingDate":now());
    dump(lucaIsFriendOfMarko)
    //edge = railoOrientDB.getEdge("11:0")
    //dump(edge)
    //dump(edge.getId())

    dump(var:luca.getEdges(), label:"Edges of Luca")
    dump(var:marko.getEdges(), label:"Edges of Marko")

    dump("------ orientDB Elements -------");
    //dump(var:railoOrientDB.getVertices(), label:"db.getVertices()", expand:false)
    //dump(var:railoOrientDB.getEdges(), label:"db.getEdges()", expand:false)
    //dump(var:railoOrientDB.getElement("9:1"), label:"getElement(Vertex id)", expand:false)
    //dump(var:railoOrientDB.getElement("11:0"), label:"getElement(Edge id)", expand:false)
    //dump(railoOrientDB.getElement("9:1").getOrientVertex())

</cfscript>

