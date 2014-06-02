package net.ringgi.railo.extension.orientdb.graph;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import net.ringgi.railo.extension.orientdb.support.ODocumentImplSupport;
import railo.runtime.PageContext;
import railo.runtime.dump.DumpData;
import railo.runtime.dump.DumpProperties;
import railo.runtime.dump.DumpTable;
import railo.runtime.exp.PageException;
import railo.runtime.type.Collection;
import railo.runtime.type.Struct;

import java.util.*;

public class OrientEdgeImpl extends ODocumentImplSupport {

    private final OrientEdge orientEdge;

    public OrientEdgeImpl(OrientEdge orientEdge) {
        if(orientEdge == null) throw new RuntimeException();
        this.orientEdge = orientEdge;
    }

    @Override
    public int size() {
        // TODO: Implement size()
        return 0;
    }

    @Override
    public Key[] keys() {
        List<Key> list=new ArrayList<Key>();
        for (String field : orientEdge.getPropertyKeys()) {
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
        if(orientEdge.getProperty(key) != null) return toCFML(orientEdge.getProperty(key));
        throw exp.createApplicationException("There is no key ["+key+"] in the DBObject");
    }

    @Override
    public Object get(String key, Object defaultValue) {
        if(orientEdge.getProperty(key) != null) return toCFML(orientEdge.getProperty(key));
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

        // http://www.orientechnologies.com/javadoc/latest/index.html?com/tinkerpop/blueprints/impls/orient/OrientEdge.html
        // TODO: Implement more methods of the API

        //getOrientEdge()
        if (methodName.equals("getOrientEdge")) {
            checkArgLength("getOrientEdge", args, 0, 0);
            return getOrientEdge();
        }


        //getElementType()
        else if (methodName.equals("getElementType")) {
            checkArgLength("getClusters", args, 0, 0);
            return toCFML(orientEdge.getElementType());
        }

        //getId()
        else if (methodName.equals("getId")) {
            checkArgLength("getClusters", args, 0, 0);
            return toCFML(orientEdge.getId());
        }
        //getIdentity()
        else if (methodName.equals("getIdentity")) {
            checkArgLength("getClusters", args, 0, 0);
            return toCFML(orientEdge.getIdentity());
        }
        //getLabel()
        else if (methodName.equals("getLabel")) {
            checkArgLength("getClusters", args, 0, 0);
            return toCFML(orientEdge.getLabel());
        }
        //isLightweight
        else if (methodName.equals("isLightweight")) {
            checkArgLength("getClusters", args, 0, 0);
            return orientEdge.isLightweight();
        }
        //convertToDocument()


        //getClassName()
        //getBaseClassName()
        //equals()


        //getVertex()
        //getInVertex()
        //getOutVertex()

        //getPropertyKeys()
        else if (methodName.equals("fieldNames")) {
            checkArgLength("getClusters", args, 0, 0);
            return toCFML(Arrays.asList(orientEdge.getPropertyKeys()));
        }
        //getProperty()
        else if (methodName.equals("getProperty")) {
            checkArgLength("getProperty", args, 1, 1);
            return toCFML(orientEdge.getProperty(caster.toString(args[0])));
        }
        //setProperty()
        else if (methodName.equals("setProperty")) {
            checkArgLength("setProperty", args, 2, 2);
            if (orientEdge.isLightweight()) orientEdge.convertToDocument();
            orientEdge.setProperty(caster.toString(args[0]), toOrient(args[0]));
            orientEdge.getGraph().commit();
            return this;
        }

        //getRecord()

        //remove()
        //removeProperty()

        //isFunctions


        String supportedFunctions= "getOrientEdge,getElementType,getId,getIdentity,getLabel,isLightweight" +
                "getProperty,setProperty";
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
        table.setTitle("OrientEdge");

        table.appendRow(2,
                __toDumpData("@label", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientEdge.getLabel()), pageContext, maxlevel, dp)
        );
        table.appendRow(2,
                __toDumpData("@id", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientEdge.getId()), pageContext, maxlevel, dp)
        );
        table.appendRow(2,
                __toDumpData("@version", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientEdge.getRecord().getVersion()), pageContext, maxlevel, dp)
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
                __toDumpData("isLightweight", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientEdge.isLightweight()), pageContext, maxlevel, dp)
        );
        table.appendRow(1,
                __toDumpData("outVertex", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientEdge.getOutVertex()), pageContext, maxlevel, dp)
        );
        table.appendRow(1,
                __toDumpData("inVertex", pageContext, maxlevel, dp),
                __toDumpData(toCFML(orientEdge.getInVertex()), pageContext, maxlevel, dp)
        );


        return table;
    }

    public OrientEdge getOrientEdge() {
        return orientEdge;
    }
}
