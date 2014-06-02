package net.ringgi.railo.extension.orientdb.graph;

import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.graph.gremlin.OCommandGremlin;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import net.ringgi.railo.extension.orientdb.support.OrientDBImplSupport;
import railo.runtime.PageContext;
import railo.runtime.dump.DumpData;
import railo.runtime.dump.DumpProperties;
import railo.runtime.dump.DumpTable;
import railo.runtime.exp.PageException;
import railo.runtime.type.Collection;

import java.net.UnknownHostException;

public class OrientBaseGraphImpl extends OrientDBImplSupport {

    private static final long serialVersionUID = 2077993327129479054L;

    public OrientBaseGraphImpl(String dbUri, String dbUsername, String dbPassword, boolean useConnectionPool, int poolMin, int poolMax) {
        super(dbUri, dbUsername, dbPassword, useConnectionPool, poolMin, poolMax);
    }

    // Called by Railo
    public static OrientBaseGraphImpl getInstance(String dbName, String host, String dbUsername, String dbPassword, boolean useConnectionPool, int poolMin, int poolMax, String storageMode, String localPath) throws UnknownHostException {

        String databaseUri;

        if (storageMode.equals("plocal")) {
            databaseUri = "plocal:"+localPath+dbName;
        } else if (storageMode.equals("memory")) {
            databaseUri = "memory:"+dbName;
        } else {
            databaseUri = "remote:"+host+"/"+dbName;
        }

        return new OrientBaseGraphImpl(databaseUri, dbUsername, dbPassword, useConnectionPool, poolMin, poolMax);
    }

    // Interfaces
    @Override
    public Object get(PageContext pc, Collection.Key key, Object defaultValue) {
        return toCFML(loadDBClasses(key.getString()));
    }

    @Override
    public Object get(PageContext pc, Collection.Key key) throws PageException {
        return toCFML(loadDBClasses(key.getString()));
    }

    @Override
    public Object set(PageContext pc, Collection.Key propertyName, Object value) throws PageException {
        // TODO: Implement set()
        saveGraph(propertyName.getString(), toOrient(value));
        return null;
    }

    @Override
    public Object setEL(PageContext pc, Collection.Key propertyName, Object value) {
        // TODO: Implement set()
        saveGraph(propertyName.getString(), toOrient(value));
        return null;
    }

    @Override
    public DumpData toDumpData(PageContext pageContext, int maxlevel, DumpProperties dumpProperties) {
        DumpTable table = new DumpTable("struct","#339933","#8e714e","#000000");
        table.setTitle("OrientDB TinkerPop Blueprints API");
        table.setComment("http://www.orientechnologies.com/javadoc/latest/index.html?com/tinkerpop/blueprints/impls/orient/package-summary.html");
        return table;
    }

    @Override
    public Object call(PageContext pc, Collection.Key methodName, Object[] args) throws PageException {

        //http://www.orientechnologies.com/javadoc/latest/index.html?com/tinkerpop/blueprints/impls/orient/OrientBaseGraph.html
        // TODO: Implement more methods of the API

        // getOrientDBFactory()
        if (methodName.equals("getOrientDBFactory")) {
            checkArgLength("getOrientDBFactory", args, 0, 0);
            return factory;
        }

        OrientBaseGraph graph = factory.get();

        try {

            // getName()
            if (methodName.equals("getName")) {
                checkArgLength("getName", args, 0, 0);
                return graph.getRawGraph().getName();
            }
            //getElement()
            else if (methodName.equals("getElement")) {
                checkArgLength("getElement", args, 1, 1);
                return toCFML(graph.getElement(caster.toString(args[0])));
            }
            //getRawGraph()
            else if (methodName.equals("getRawGraph")) {
                checkArgLength("getRawGraph", args, 0, 0);
                return toCFML(graph.getRawGraph());
            }


            //query()
            //command()
            else if (methodName.equals("command")) {
                checkArgLength("command", args, 1, 1);
                return toCFML(graph.command(new OCommandGremlin(caster.toString(args[0]))).execute());
            }
            //traverse()


            //countVertices()
            else if (methodName.equals("countVertices")) {
                checkArgLength("countVertices", args, 0, 0);
                return graph.countVertices();
            }
            //getVertexBaseType()
            else if (methodName.equals("getVertexBaseType")) {
                checkArgLength("getVertexBaseType", args, 0, 0);
                return toCFML(graph.getVertexBaseType());
            }
            //createVertexType()
            //addVertex()
            else if (methodName.equals("addVertex")) {
                checkArgLength("addVertex", args, 1, 1);
                OrientVertex vertex = graph.addVertex(caster.toString(args[0]), caster.toString(args[0]));
                graph.commit();
                return toCFML(vertex);
            }
            //getVertex()
            else if (methodName.equals("getVertex")) {
                checkArgLength("getVertex", args, 1, 1);
                String orid = caster.toString(args[0]);
                if (!orid.contains("#")) orid = "#" + caster.toString(args[0]);
                return toCFML(graph.getVertex(orid));
            }
            //getVertices()
            else if (methodName.equals("getVertices")) {
                checkArgLength("getVertices", args, 0, 0);
                return toCFML(graph.getVertices());
            }
            //getVerticesOfClass()
            else if (methodName.equals("getVerticesOfClass")) {
                checkArgLength("getVerticesOfClass", args, 1, 1);
                return toCFML(graph.getVerticesOfClass(caster.toString(args[0])));
            }
            //removeVertex()
            else if (methodName.equals("removeVertex")) {
                checkArgLength("removeVertex", args, 1, 1);
                graph.removeVertex((Vertex) toOrient(args[0]));
                graph.commit();
                return true;
            }
            //dropVertexType()
            else if (methodName.equals("dropVertexType")) {
                checkArgLength("dropVertexType", args, 1, 1);
                graph.dropVertexType(caster.toString(args[0]));
                graph.commit();
                return true;
            }


            //countEdges()
            else if (methodName.equals("countEdges")) {
                checkArgLength("countEdges", args, 0, 0);
                return graph.countEdges();
            }
            //getEdgeBaseType()
            else if (methodName.equals("getEdgeBaseType")) {
                checkArgLength("getEdgeBaseType", args, 0, 0);
                return toCFML(graph.getEdgeBaseType());
            }
            //createEdgeType()
            //addEdge()
            else if (methodName.equals("addEdge")) {
                checkArgLength("addEdge", args, 3, 3);
                OrientEdge edge = graph.addEdge(null, ((Vertex) toOrient(args[0])), ((Vertex) toOrient(args[1])), caster.toString(args[2]));
                graph.commit();
                return toCFML(edge);
            }
            //getEdge()
            else if (methodName.equals("getEdge")) {
                checkArgLength("getEdge", args, 1, 1);
                String orid = caster.toString(args[0]);
                if (!orid.contains("#")) orid = "#" + caster.toString(args[0]);
                return toCFML(graph.getEdge(orid));
            }
            //getEdges()
            else if (methodName.equals("getEdges")) {
                checkArgLength("getEdges", args, 0, 0);
                return toCFML(graph.getEdges());
            }
            //getEdgesOfClass()
            else if (methodName.equals("getEdgesOfClass")) {
                checkArgLength("getEdgesOfClass", args, 1, 1);
                return toCFML(graph.getEdgesOfClass(caster.toString(args[0])));
            }
            //getEdgeType()
            //removeEdge()
            else if (methodName.equals("removeEdge")) {
                checkArgLength("removeEdge", args, 1, 1);
                graph.removeEdge((Edge) toOrient(args[0]));
                graph.commit();
                return true;
            }
            //dropEdgeType()
            else if (methodName.equals("dropEdgeType")) {
                checkArgLength("dropEdgeType", args, 1, 1);
                graph.dropEdgeType(caster.toString(args[0]));
                graph.commit();
                return true;
            }

            // attach()
            // detach()

            //multiple isFunctions
            //indexes
            //transactions


            //isClosed()
            //shutdown()
            //drop()


            String supportedFunctions= "getOrientDBFactory,getName,getElement,getRawGraph," +
                    "countVertices,getVertexBaseType,addVertex,getVertex,getVertices,getVerticesOfClass,removeVertex,dropVertexType" +
                    "countEdges,getEdgeBaseType,addEdge,getEdge,getEdges,getEdgesOfClass,removeEdge,dropEdgeType";
            throw exp.createExpressionException("function ["+methodName+"] is not supported, supported functions are ["+supportedFunctions+"]");

        } finally {
            factory.close();
        }
    }

    private Object saveGraph(String clusterName, Object element) {

        OrientVertex vertex;
        OrientBaseGraph graph = factory.get();

        try {

            if(element instanceof OrientVertex) {
                vertex = (OrientVertex) element;
                vertex.save();
            }
            if(element instanceof OrientEdge) {
                OrientEdge edge = (OrientEdge) element;
                edge.save();
            }
            if(element instanceof ODocument) {
                ODocument doc = (ODocument) element;
                doc.save();
            }

        } finally {
            factory.close();
        }

        return true;
    }

    private ORecordIteratorClass loadDBClasses(String className) {
        OrientBaseGraph graph = factory.get();
        try {
            graph.getRawGraph().browseClass(className);
        } finally {
            factory.close();
        }
        throw new UnsupportedOperationException("named arguments are not supported yet!");
    }
}
