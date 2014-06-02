package net.ringgi.railo.extension.orientdb;

import net.ringgi.railo.extension.orientdb.document.ODatabaseDocumentTxImpl;
import net.ringgi.railo.extension.orientdb.graph.OrientBaseGraphImpl;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.PageContext;
import railo.runtime.exp.PageException;
import railo.runtime.ext.function.Function;

import java.net.UnknownHostException;

public class OrientDBConnect implements Function {

    private static final long serialVersionUID = 7056140672606141167L;

    public static Object call(PageContext pc, String dbName) throws PageException {
        return call(pc, dbName,  "localhost:2424", "document", "admin", "admin", true, 1, 10, "remote", null);
    }

    public static Object call(PageContext pc, String dbName, String host) throws PageException {
        return call(pc, dbName, host, "document", "admin", "admin", true, 1, 10, "remote", null);
    }

    public static Object call(PageContext pc, String dbName, String host, String dbType) throws PageException {
        return call(pc, dbName, host, dbType, "admin", "admin", true, 1, 10, "remote", null);
    }

    public static Object call(PageContext pc, String dbName, String host, String dbType, String username, String password) throws PageException {
        return call(pc, dbName, host, dbType, username, password, true, 1, 10, "remote", null);
    }

    public static Object call(PageContext pc, String dbName, String host, String dbType, String username, String password, boolean useConnectionPool) throws PageException {
        return call(pc, dbName, host, dbType, username, password, useConnectionPool, 1, 10, "remote", null);
    }

    public static Object call(PageContext pc, String dbName, String host, String dbType, String username, String password, boolean useConnectionPool, double poolMin, double poolMax) throws PageException {
        return call(pc, dbName, host, dbType, username, password, useConnectionPool, poolMin, poolMax, "remote", null);
    }

    public static Object call(PageContext pc, String dbName, String host, String dbType, String username, String password, boolean useConnectionPool, double poolMin, double poolMax, String storageMode) throws PageException {
        return call(pc, dbName, host, dbType, username, password, useConnectionPool, poolMin, poolMax, storageMode, null);
    }

    public static Object call(PageContext pc, String dbName, String host, String dbType, String username, String password, boolean useConnectionPool, double poolMin, double poolMax, String storageMode, String localPath) throws PageException {
        try {
            if (dbType.equals("graph")) {
                return OrientBaseGraphImpl.getInstance(dbName, host, username, password, useConnectionPool, (int) poolMin, (int) poolMax, storageMode, localPath);
            } else {
                return ODatabaseDocumentTxImpl.getInstance(dbName, host, username, password, useConnectionPool, (int) poolMin, (int) poolMax, storageMode, localPath);
            }
        }
        catch (UnknownHostException e) {
            throw CFMLEngineFactory.getInstance().getCastUtil().toPageException(e);
        }
    }

}
