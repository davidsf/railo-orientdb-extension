package net.ringgi.railo.extension.orientdb.graph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.impls.orient.OrientElement;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import net.ringgi.railo.extension.orientdb.support.ODocumentImplSupport;
import railo.runtime.PageContext;
import railo.runtime.dump.DumpData;
import railo.runtime.dump.DumpProperties;
import railo.runtime.dump.DumpTable;
import railo.runtime.exp.PageException;
import railo.runtime.type.Collection;
import railo.runtime.type.Struct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class OrientVertexImpl extends ODocumentImplSupport {

    private final OrientVertex orientVertex;

    public OrientVertexImpl(OrientVertex orientVertex) {
        if(orientVertex == null) throw new RuntimeException();
        this.orientVertex = orientVertex;
    }

    @Override
    public int size() {
        // TODO: Implement size()
        return 0;
    }

    @Override
    public Key[] keys() {
        List<Key> list=new ArrayList<Key>();
        for (String field : orientVertex.getPropertyKeys()) {
            list.add(caster.toKey(field,null));
        }
        return list.toArray(new Key[list.size()]);
    }

    @Override
    public Iterator<Key> keyIterator() {
        // TODO: Implement keyIterator()
        return null;
    }


    @Override
    public Object remove(Key key) throws PageException {
        // TODO: Implement remove()
        return null;
    }

    @Override
    public Object removeEL(Key key) {
        // TODO: Implement removeEL()
        return null;
    }

    @Override
    public void clear() {
        // TODO: Implement clear()
    }

    @Override
    public Object get(String key) throws PageException {
        if(orientVertex.getProperty(key) != null) return toCFML(orientVertex.getProperty(key));
        throw exp.createApplicationException("There is no key ["+key+"] in the DBObject");
    }

    @Override
    public Object get(String key, Object defaultValue) {
        if(orientVertex.getProperty(key) != null) return toCFML(orientVertex.getProperty(key));
        return defaultValue;
    }

    @Override
    public Object set(String s, Object o) throws PageException {
        // TODO: Implement set()
        return null;
    }

    @Override
    public Object setEL(String s, Object o) {
        // TODO: Implement setEL()
        return null;
    }

    @Override
    public Collection duplicate(boolean b) {
        // TODO: Implement duplicate()
        return null;
    }

    @Override
    public boolean containsKey(String s) {
        // TODO: Implement containsKey()
        return false;
    }


    @Override
    public Iterator<String> keysAsStringIterator() {
        // TODO: Implement keysAsStringIterator()
        return null;
    }

    @Override
    public Object call(PageContext pageContext, Key methodName, Object[] args) throws PageException {

        // http://www.orientechnologies.com/javadoc/latest/index.html?com/tinkerpop/blueprints/impls/orient/OrientVertex.html
        // TODO: Implement more methods of the API

        //getOrientVertex()
        if (methodName.equals("getOrientVertex")) {
            checkArgLength("getOrientVertex", args, 0, 0);
            return getOrientVertex();
        }


        //getElementType()
        else if (methodName.equals("getElementType")) {
            checkArgLength("getClusters", args, 0, 0);
            return orientVertex.getElementType();
        }

        //getId()
        else if (methodName.equals("getId")) {
            checkArgLength("getClusters", args, 0, 0);
            return toCFML(orientVertex.getId());
        }
        //getIdentity()
        else if (methodName.equals("getIdentity")) {
            checkArgLength("getClusters", args, 0, 0);
            return toCFML(orientVertex.getIdentity());
        }
        //getLabel()
        else if (methodName.equals("getLabel")) {
            checkArgLength("getLabel", args, 0, 0);
            return orientVertex.getLabel();
        }



        //getBaseClassName()
        //getConnectionClass()

        //getVertices()

        //addEdge()
        //countEdges()
        //getEdges()
        else if (methodName.equals("getEdges")) {
            checkArgLength("getEdges", args, 0, 1);
            if (args.length > 0) {
                String direction = caster.toString(args[0]);
                if (direction.equals("in")) return toCFML(orientVertex.getEdges(Direction.IN));
                if (direction.equals("out")) return toCFML(orientVertex.getEdges(Direction.OUT));
            }
            return toCFML(orientVertex.getEdges(Direction.BOTH));
        }

        //createLink()
        //getConnectionDirection()
        //getConnectionFieldName()

        //query()
        //traverse()
        //remove()

        //getPropertyKeys()
        else if (methodName.equals("getPropertyKeys")) {
            checkArgLength("getPropertyKeys", args, 1, 1);
            return toCFML(orientVertex.getPropertyKeys());
        }
        //getProperty()
        else if (methodName.equals("getProperty")) {
            checkArgLength("getProperty", args, 1, 1);
            return toCFML(orientVertex.getProperty(caster.toString(args[0])));
        }
        //setProperty()
        else if (methodName.equals("setProperty")) {
            checkArgLength("setProperty", args, 2, 2);
            orientVertex.setProperty(caster.toString(args[0]), toOrient(args[1]));
            orientVertex.getGraph().commit();
            return this;
        }


        String supportedFunctions= "getOrientVertex,getElementType,getId,getIdentity,getLabel," +
                "getEdges," +
                "getPropertyKeys,getProperty,setProperty";
        throw exp.createExpressionException("function ["+methodName+"] is not supported, supported functions are ["+supportedFunctions+"]");
    }

    @Override
    public Object callWithNamedValues(PageContext pageContext, Key key, Struct struct) throws PageException {
        // TODO: Implement callWithNamedValues()
        return null;
    }

    @Override
    public Set keySet() {
        // TODO: Implement keySet()
        //return obj.keySet();
        return null;
    }

    @Override
    public long sizeOf() {
        // TODO: Implement sizeOf()
        return 0;
    }

    @Override
    public DumpData toDumpData(PageContext pageContext, int maxlevel, DumpProperties dp) {

        DumpTable table = new DumpTable("struct","#339933","#8e714e","#000000");
        table.setTitle("OrientVertex");
        maxlevel--;

        table.appendRow(2,
                __toDumpData("@label", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientVertex.getLabel()), pageContext, maxlevel, dp)
        );
        table.appendRow(2,
                __toDumpData("@id", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientVertex.getId()), pageContext, maxlevel, dp)
        );
        table.appendRow(2,
                __toDumpData("@version", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientVertex.getRecord().getVersion()), pageContext, maxlevel, dp)
        );

        DumpTable table2 = new DumpTable("struct","#339933","#8e714e","#000000");
        Key[] keys = keys();
        int maxkeys = dp.getMaxKeys();
        int index=0;
        for(int i=0; i<keys.length; i++) {
            if(maxkeys<=index++)break;
            table2.appendRow(2,
                    __toDumpData(keys[i].toString(),pageContext,maxlevel,dp),
                    __toDumpData(get(keys[i].toString(), null), pageContext,maxlevel,dp)
            );
        }
        table.appendRow(1,
                __toDumpData("fields", pageContext, maxlevel, dp),
                table2
        );

        table.appendRow(1,
                __toDumpData("in", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientVertex.countEdges(Direction.IN)), pageContext, maxlevel, dp)
        );
        table.appendRow(1,
                __toDumpData("out", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientVertex.countEdges(Direction.OUT)), pageContext, maxlevel, dp)
        );


        return table;
    }

    public OrientVertex getOrientVertex () {
        return orientVertex;
    }

}
