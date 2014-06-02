package net.ringgi.railo.extension.orientdb.support;

import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import railo.runtime.PageContext;
import railo.runtime.exp.PageException;
import railo.runtime.type.Collection;
import railo.runtime.type.Objects;
import railo.runtime.type.Struct;

public abstract class OrientDBImplSupport extends CastableSupport implements Objects {

    protected final OrientGraphFactory factory;

    public OrientDBImplSupport(String dbUri, String dbUsername, String dbPassword, boolean useConnectionPool, int poolMin, int poolMax) {

        OrientGraphFactory factory = new OrientGraphFactory(dbUri, dbUsername, dbPassword);
        if (useConnectionPool) factory.setupPool(poolMin, poolMax);
        factory.setTransactional(true);

        this.factory = factory;
    }

    @Override
    public Object callWithNamedValues(PageContext pc, Collection.Key methodName, Struct args) throws PageException {
        throw new UnsupportedOperationException("named arguments are not supported yet!");
    }
}
