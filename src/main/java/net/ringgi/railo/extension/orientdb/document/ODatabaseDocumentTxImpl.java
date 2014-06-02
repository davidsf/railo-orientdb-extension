package net.ringgi.railo.extension.orientdb.document;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.iterator.ORecordIteratorCluster;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import net.ringgi.railo.extension.orientdb.support.OrientDBImplSupport;
import railo.runtime.PageContext;
import railo.runtime.dump.DumpData;
import railo.runtime.dump.DumpProperties;
import railo.runtime.dump.DumpTable;
import railo.runtime.exp.PageException;
import railo.runtime.type.Collection;

import java.net.UnknownHostException;
import java.util.Map;

public class ODatabaseDocumentTxImpl extends OrientDBImplSupport {

    private static final long serialVersionUID = 545309638905742275L;

    public ODatabaseDocumentTxImpl(String dbUri, String dbUsername, String dbPassword, boolean useConnectionPool, int poolMin, int poolMax) {
        super(dbUri, dbUsername, dbPassword, useConnectionPool, poolMin, poolMax);
    }

    // Called by Railo
    public static ODatabaseDocumentTxImpl getInstance(String dbName, String host, String dbUsername, String dbPassword, boolean useConnectionPool, int poolMin, int poolMax, String storageMode, String localPath) throws UnknownHostException {

        String databaseUri;

        if (storageMode.equals("plocal")) {
            databaseUri = "plocal:"+localPath+dbName;
        } else if (storageMode.equals("memory")) {
            databaseUri = "memory:"+dbName;
        } else {
            databaseUri = "remote:"+host+"/"+dbName;
        }

        return new ODatabaseDocumentTxImpl(databaseUri, dbUsername, dbPassword, useConnectionPool, poolMin, poolMax);
    }

    // Interfaces
    @Override
    public Object get(PageContext pc, Collection.Key key, Object defaultValue) {
        return toCFML(loadDocuments(key.getString()));
    }

    @Override
    public Object get(PageContext pc, Collection.Key key) throws PageException {
        return toCFML(loadDocuments(key.getString()));
    }

    @Override
    public Object set(PageContext pc, Collection.Key propertyName, Object value) throws PageException {
        return saveDocument(propertyName.getString(), toOrient(value));
    }

    @Override
    public Object setEL(PageContext pc, Collection.Key propertyName, Object value) {
        return saveDocument(propertyName.getString(), toOrient(value));
    }

    @Override
    public DumpData toDumpData(PageContext pageContext, int maxlevel, DumpProperties dumpProperties) {
        DumpTable table = new DumpTable("struct","#339933","#8e714e","#000000");
        table.setTitle("OrientDB Document API");
        table.setComment("http://www.orientechnologies.com/javadoc/latest/index.html?com/orientechnologies/orient/core/db/package-summary.html");
        return table;
    }

    @Override
    public Object call(PageContext pc, Collection.Key methodName, Object[] args) throws PageException {

        // http://www.orientechnologies.com/javadoc/latest/index.html?com/orientechnologies/orient/core/db/document/ODatabaseDocumentTx.html
        // TODO: Implement more methods of the API

        // getOrientDBFactory()
        if (methodName.equals("getOrientDBFactory")) {
            checkArgLength("getName", args, 0, 0);
            return factory;
        }

        ODatabaseDocumentTx db = factory.getDatabase();
        //if (!db.exists()) db.create();

        try {

            //getName()
            if (methodName.equals("getName")) {
                checkArgLength("getName", args, 0, 0);
                return db.getName();
            }
            //getType()
            else if (methodName.equals("getType")) {
                checkArgLength("getType", args, 0, 0);
                return db.getType();
            }
            //getMetadata()
            else if (methodName.equals("getMetadata")) {
                checkArgLength("getMetadata", args, 0, 0);
                return db.getMetadata();
            }

            //getClusterNames()
            else if (methodName.equals("getClusterNames")) {
                checkArgLength("getClusterNames", args, 0, 0);
                return toCFML(db.getClusterNames());
            }
            //getClusters()
            else if (methodName.equals("getClusters")) {
                checkArgLength("getClusters", args, 0, 0);
                return toCFML(db.getClusters());
            }
            //browseCluster()
            else if (methodName.equals("browseCluster")) {
                checkArgLength("browseCluster", args, 1, 1);
                return toCFML(db.browseCluster(caster.toString(args[0])));
            }
            //dropCluster()
            else if (methodName.equals("dropCluster")) {
                checkArgLength("dropCluster", args, 1, 1);
                return db.dropCluster(caster.toString(args[0]), true);
            }

            //countClass()
            else if (methodName.equals("countClass")) {
                checkArgLength("countClass", args, 1, 1);
                return toCFML(db.countClass(caster.toString(args[0])));
            }
            //browseClass()
            else if (methodName.equals("browseClass")) {
                checkArgLength("browseClass", args, 1, 1);
                return toCFML(db.browseClass(caster.toString(args[0])));
            }


            // load()
            else if (methodName.equals("load")) {
                checkArgLength("load", args, 1, 1);
                String orid = caster.toString(args[0]);
                if (!orid.contains("#")) orid = "#" + caster.toString(args[0]);
                OIdentifiable rec = db.load( new ORecordId(orid) );
                return toCFML(rec.getRecord());
            }
            // query()
            else if (methodName.equals("query")) {
                checkArgLength("query", args, 1, 1);
                return toCFML(db.query(new OSQLSynchQuery<ODocument>(caster.toString(args[0]))));
            }
            // command();
            else if (methodName.equals("command")) {
                checkArgLength("command", args, 1, 1);
                return toCFML(db.command(new OCommandSQL(caster.toString(args[0]))).execute());
            }

            // serverInfo()
            // schema()

            // securityRoles()
            // securityUsers()

            String supportedFunctions= "getOrientDBFactory,getName,getType,getMetadata,getClusterNames,getClusters,browseCluster,browseClass,load,query,command";
            throw exp.createExpressionException("function ["+methodName+"] is not supported, supported functions are ["+supportedFunctions+"]");

        } finally {
            factory.close();
        }
    }

    private ODocument saveDocument(String clusterName, Object document) {

        ODocument doc;
        ODatabaseDocumentTx db = factory.getDatabase();

        try {

            if(document instanceof ODocument) {
                doc = (ODocument) document;
                doc.save();
            }
            else if(document instanceof Map) {
                Map map = (Map) document;
                doc = new ODocument(clusterName);
                doc.fields(map);
                doc.save();
            }
            else {
                doc = new ODocument(clusterName);
                doc.save();
            }

        } finally {
            db.close();
        }

        return doc;
    }

    private ORecordIteratorCluster loadDocuments(String clusterName) {
        ODatabaseDocumentTx db = factory.getDatabase();
        try {
            return db.browseCluster(clusterName);
        } finally {
            factory.close();
        }
    }

}
