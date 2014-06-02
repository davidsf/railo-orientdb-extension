package net.ringgi.railo.extension.orientdb.support;


import com.orientechnologies.common.collection.OMultiCollectionIterator;
import com.orientechnologies.orient.core.db.record.ridbag.ORidBag;
import com.orientechnologies.orient.core.id.ORecordId;
import com.orientechnologies.orient.core.iterator.ORecordIteratorClass;
import com.orientechnologies.orient.core.iterator.ORecordIteratorCluster;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OResultSet;
import com.tinkerpop.blueprints.impls.orient.OrientEdge;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import net.ringgi.railo.extension.orientdb.document.ODocumentImpl;
import net.ringgi.railo.extension.orientdb.graph.OrientEdgeImpl;
import net.ringgi.railo.extension.orientdb.graph.OrientVertexImpl;
import net.ringgi.railo.extension.orientdb.util.SimpleDumpData;
import railo.loader.engine.CFMLEngine;
import railo.loader.engine.CFMLEngineFactory;
import railo.runtime.PageContext;
import railo.runtime.dump.DumpData;
import railo.runtime.dump.DumpProperties;
import railo.runtime.dump.Dumpable;
import railo.runtime.exp.PageException;
import railo.runtime.type.Array;
import railo.runtime.type.Collection;
import railo.runtime.type.Collection.Key;
import railo.runtime.type.Struct;
import railo.runtime.util.Cast;
import railo.runtime.util.Creation;
import railo.runtime.util.Decision;
import railo.runtime.util.Excepton;

import java.util.*;
import java.util.Map.Entry;

public class ObjectSupport {

    private CFMLEngine engine;
    protected Cast caster;
    protected Excepton exp;
    protected Creation creator;
    protected Decision decision;

    public ObjectSupport(){
        engine= CFMLEngineFactory.getInstance();
        caster=engine.getCastUtil();
        exp=engine.getExceptionUtil();
        creator=engine.getCreationUtil();
        decision=engine.getDecisionUtil();
    }

    @Override
    public Object clone() {
        throw new UnsupportedOperationException("this operation is not supported");
    }

    public Object toCFML(Object obj) {
        if (obj instanceof ODocument) {
            return new ODocumentImpl((ODocument) obj);
        }
        if (obj instanceof ODocumentImpl) {
            return obj;
        }
        if (obj instanceof OrientVertex) {
            return new OrientVertexImpl((OrientVertex) obj);
        }
        if (obj instanceof OrientEdge) {
            return new OrientEdgeImpl((OrientEdge) obj);
        }
        if (obj instanceof ORidBag) return ((ORidBag) obj).toString();
        if (obj instanceof ORecordId) return ((ORecordId) obj).toString();
        if (obj instanceof OResultSet) {
            Iterator it = ((OResultSet) obj).iterator();
            Array arr = creator.createArray();
            while (it.hasNext()) {
                arr.appendEL(toCFML(it.next()));
            }
            return arr;
        }
        if (obj instanceof ORecordIteratorCluster) {
            ORecordIteratorCluster it = (ORecordIteratorCluster) obj;
            Array arr=creator.createArray();
            while(it.hasNext()){
                arr.appendEL(toCFML(it.next()));
            }
            return arr;
        }
        if (obj instanceof ORecordIteratorClass) {
            ORecordIteratorClass it = (ORecordIteratorClass) obj;
            Array arr=creator.createArray();
            while(it.hasNext()){
                arr.appendEL(toCFML(it.next()));
            }
            return arr;
        }
        if (obj instanceof OMultiCollectionIterator) {
            OMultiCollectionIterator it = (OMultiCollectionIterator) obj;
            Array arr=creator.createArray();
            while(it.hasNext()){
                arr.appendEL(toCFML(it.next()));
            }
            return arr;
        }
        //if(obj instanceof OMVRBTreeRIDSet) return toCFML(((OMVRBTreeRIDSet) obj).toDocument());
        /*
        if (obj instanceof Iterable) {
            Iterator it = ((Iterable) obj).iterator();
            Array arr=creator.createArray();
            while(it.hasNext()){
                arr.appendEL(toCFML(it.next()));
            }
            return arr;
        }*/
        if (obj instanceof List) {
            List list = (List)obj;
            Array arr=creator.createArray();
            Iterator it = list.iterator();
            while(it.hasNext()){
                arr.appendEL(toCFML(it.next()));
            }
            return arr;
        }
        if (obj instanceof Set) {
            Set set=(Set) obj;
            Iterator it = set.iterator();
            Array arr=creator.createArray();
            while(it.hasNext()){
                arr.appendEL(toCFML(it.next()));
            }
            return arr;
        }
        if (obj instanceof Map) {
            Map map = (Map)obj;
            Struct str=creator.createStruct();
            Iterator it = map.entrySet().iterator();
            Entry e;
            while(it.hasNext()){
                e = (Map.Entry)it.next();
                str.put(toCFML(e.getKey()), toCFML(e.getValue()));
            }
            return str;
        }
        if (obj instanceof Number && !(obj instanceof Double)) return CFMLEngineFactory.getInstance().getCastUtil().toDouble(obj,null);

        //if(obj!=null)print.e("toCFML:"+obj+":"+obj.getClass().getName());

        return obj;
    }

    public Object toOrient(Object obj) {
        if(obj instanceof ODocumentImpl) {
            return ((ODocumentImpl) obj).getODocument();
        }
        if(obj instanceof OrientVertexImpl) {
            return ((OrientVertexImpl) obj).getOrientVertex();
        }
        if(obj instanceof OrientEdgeImpl) {
            return ((OrientEdgeImpl) obj).getOrientEdge();
        }
        if(obj instanceof List) {
            List list = (List)obj;
            ArrayList rtn=new ArrayList();
            Iterator it = list.iterator();
            while(it.hasNext()){
                rtn.add(toOrient(it.next()));
            }
            return rtn;
        }
        if(obj instanceof Map) {
            Map map = (Map)obj;
            Map rtn=new HashMap();
            Iterator it = map.entrySet().iterator();
            Entry e;
            while(it.hasNext()){
                e = (Map.Entry)it.next();
                rtn.put(toOrient(e.getKey()), toOrient(e.getValue()));
            }
            return rtn;
        }

        // Maybe not needed?
        //if(obj instanceof Struct) return toDBObject((Struct)obj);

        return obj;
    }

    public DumpData __toDumpData(Object obj, PageContext pageContext, int maxlevel, DumpProperties dp) {
        if(obj instanceof ODocumentImpl) {
            //return ((ODocumentImpl) obj).toDumpData(pageContext, maxlevel, dp); //--> as an object can reference super objects this can end in a stack overflow
            return new SimpleDumpData(((ODocumentImpl) obj).getODocument().getIdentity().toString());
        }
        if(obj instanceof Dumpable) {
            return ((Dumpable) obj).toDumpData(pageContext, maxlevel, dp);
        }
        if(CFMLEngineFactory.getInstance().getDecisionUtil().isSimpleValue(obj)) {
            return new SimpleDumpData(caster.toString(obj, null));
        }
        // TODO: find out why we can have a null value here ??
        if (obj != null) {
            return new SimpleDumpData(obj.toString());
        }
        return new SimpleDumpData("");
    }

    public static Set<Entry<String, Object>> entrySet(Collection coll) {
        Iterator<Entry<Key, Object>> it = coll.entryIterator();
        Entry<Key, Object> e;
        HashSet<Entry<String, Object>> set=new HashSet<Entry<String, Object>>();
        while(it.hasNext()){
            e= it.next();
            set.add(new CollectionMapEntry(coll,e.getKey(),e.getValue()));
        }
        return set;
    }

    public static java.util.Collection<?> values(Collection coll) {
        ArrayList<Object> arr = new ArrayList<Object>();
        //Key[] keys = sct.keys();
        Iterator<Object> it = coll.valueIterator();
        while(it.hasNext()) {
            arr.add(it.next());
        }
        return arr;
    }

    public void putAll(Collection coll, Map map) {
        Iterator it = map.entrySet().iterator();
        Map.Entry entry;
        while(it.hasNext()) {
            entry=(Entry) it.next();
            coll.setEL(caster.toKey(entry.getKey(),null), entry.getValue());
        }
    }

    public static class CollectionMapEntry implements Map.Entry<String,Object> {

        private Collection.Key key;
        private Object value;
        private Collection coll;

        public CollectionMapEntry(Collection coll,Collection.Key key,Object value) {

            this.coll=coll;
            this.key=key;
            this.value=value;
        }

        @Override
        public String getKey() {
            return key.getString();
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object value) {
            Object old = value;
            coll.setEL(key, value);
            this.value=value;
            return old;
        }

    }

    //--------------------------------------------
    // Helper
    public int checkArgLength(String functionName,Object[] arguments,int min, int max) throws PageException {
        if(arguments==null) arguments=new Object[0];
        if(arguments.length<min)
            throw exp.createApplicationException("the function "+functionName+" needs at least "+min+" arguments, but you have defined only "+(arguments==null?0:arguments.length));
        if(arguments.length>max)
            throw exp.createApplicationException("the function "+functionName+" only support up to "+max+" arguments, but you have defined "+(arguments.length));
        return arguments.length;
    }

}
