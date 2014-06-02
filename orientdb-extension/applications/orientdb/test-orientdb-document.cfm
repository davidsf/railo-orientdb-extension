<cfsetting showdebugoutput="no">

<cfscript>

    dump("====== OrientDB Create new database =====")
    //railoOrientDB = OrientDBConnect("MyODocumentDatabase", "orientDBServer:2424", "document", "root", "root-password-from-orient-server-config.xml");
    //Unfortunately this doesn't work yet from within Railo, not clear why, create databases on server via bin/console orientdb> create database remote:localhost:2424/MyODocumentDatabase root dbrootpass plocal document

    dump("====== OrientDB Connect =====")
    railoOrientDB = OrientDBConnect("GratefulDeadConcerts", "orientDBServer:2424")
    dump(var:railoOrientDB, expand:false)
    dump(var:railoOrientDB.getOrientDBFactory(), label:"The underlying Factory - handles pooled and thread save graph and document databases - after each action the connection is released", expand:false)

    dump("------ orientDB Infos -------");
    dump(var:railoOrientDB.getName(), label:"db.getName()")
    dump(var:railoOrientDB.getType(), label:"db.getType()")
    dump(var:railoOrientDB.getMetadata(), label:"db.getMetadata() - not further implemented yet", expand:false)

    dump("------ orientDB User -------")
    dump("------ orientDB Security -------")
    dump("------ orientDB Schema -------")

    dump("====== Classes / Clusters ======")
    dump(var:railoOrientDB.getClusterNames(), label:"db.getClusterNames()", expand:false)
    dump(var:railoOrientDB.getClusters(), label:"db.getClusters()", expand:false)
    dump(var:railoOrientDB.browseCluster("ORole"), label:"db.browseCluster('clusterName') - ORole", expand:false)
    dump(var:railoOrientDB.browseClass("OUser"), label:"db.browseClass('className') - OUser", expand:false)
    // As orientdb 2.0 will change how to handle classes and clusters, we wait with this
    //dump("------ orientDB load subcluster of class ------");
    //oDocumentCluster = railoOrientDB.browseCluster("european_customers")

    dump("------ create/delete class/cluster ------")
    dump(var:railoOrientDB.Customer = {}, label:"db.Customer = {} - creates class, cluster and first document - returned struct is wrong, should be the document", expand:false)
    dump(var:railoOrientDB.Customer = {"firstName":"John", "lastName":"Doe"}, label:"db.Customer = {'firstName':'John','lastName':'Doe'} - should still be a document :)", expand:false)
    customers = railoOrientDB.customer;
    dump(var:customers, label:"customers = db.customer", expand:false)
    dump(var:customers[1].delete(), label:"customers[2].delete()")

    dump("====== ODocument ======")
    customer = customers[2]

    dump("------ db.load() ------")
    dump(var:railoOrientDB.load("4:1"), label:"db.load('4:1')", expand:false)
    dump(var:railoOrientDB.load(customer.getIdentity()), label:"db.load(customer.getIdentity())", expand:false)
    dump(var:railoOrientDB.load(customer["@rid"]), label:"db.load(customer['@rid]')", expand:false)

    dump("------ oDocument methods ------")
    dump(var:customer.getIdentity(), label:"doc.getIdentity()", expand:false)
    dump(var:customer.fieldNames(), label:"doc.fieldNames()", expand:false)
    dump(var:customer.field("firstName"), label:"doc.field('firstName')", expand:false);

    dump("------ oDocument.field('key', 'value') ------")
    dump(var:customer.field("firstName", "Hans"), label:"doc.field(key, value) - update", expand:false)
    dump(var:customer.field("lastAccess", now()), label:"doc.field(key, value) - new field", expand:false)

    dump("====== struct ======")
    dump("====== struct ======")
    dump(var:customer.email = "john.doe@gmail.com", label:"doc.property = value", expand:false)
    railoOrientDB.Address = {"street":"Mainstreet","number":"18","postCode":"1234","city":"Ducktown"}
    address = railoOrientDB.browseCluster("Address")[1]
    dump(var:customer.address = address, label:"doc.property = oDocument", expand:false)
    dump(var:customer, label:"The complete customer", expand:false)
    dump(var:customer.address.city, label:"doc.property.property")
    dump(var:sizeOf(customer), label:"sizeOf(oDoc)")
    dump(var:structCount(customer), label:"structCount(oDoc)")

    dump("------ loop oDocument ------");
    loop collection="#customer#" index="n" item="v" {
        dump(var:v, label:n, expand:false)
    }

    dump("====== Queries ======")
    dump("------ orientDB.query() - select from V where name like '%HEY%' ------")
    dump(var:railoOrientDB.query("select from V where name like '%HEY%'"), label:"db.query()", expand:false)

    dump("====== Pivoting ======")
    dump("====== Traversing ======")
    dump("====== Indexing ======")
    dump("====== Transactions ======")

    dump("------ clean up ------");
    customer.delete()
    address.delete()
    //dump(var:railoOrientDB.browseCluster("customer"), label:"customer cluster", expand:false)
    //dump(var:railoOrientDB.browseCluster("address"), label:"address cluster", expand:false)

</cfscript>

