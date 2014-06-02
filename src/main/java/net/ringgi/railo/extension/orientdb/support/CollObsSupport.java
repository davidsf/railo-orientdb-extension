package net.ringgi.railo.extension.orientdb.support;

import com.orientechnologies.orient.core.record.impl.ODocument;
import net.ringgi.railo.extension.orientdb.document.ODocumentImpl;
import railo.runtime.PageContext;
import railo.runtime.dump.DumpProperties;
import railo.runtime.dump.DumpTable;
import railo.runtime.exp.PageException;
import railo.runtime.type.Collection;
import railo.runtime.type.Objects;
import railo.runtime.util.Cast;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;


public abstract class CollObsSupport extends CastableSupport implements Collection, Objects {

    private static final long serialVersionUID = -3719938412607167693L;

    @Override
    public Object clone() {
        return duplicate(true);
    }

    @Override
    public final Object get(Key key) throws PageException {
        return get(key.toString());
    }

    @Override
    public final Object get(Key key, Object defaultValue) {
        return get(key.toString(), defaultValue);
    }

    @Override
    public final Object get(PageContext pc, Key key) throws PageException {
        return get(key);
    }

    @Override
    public final Object get(PageContext pc, Key key, Object defaultValue) {
        return get(key,defaultValue);
    }

    @Override
    public final Object set(Key key, Object value) throws PageException {
        return set(key.toString(), value);
    }

    @Override
    public final Object setEL(Key key, Object value) {
        return setEL(key.toString(), value);
    }

    @Override
    public final Object set(PageContext pc, Key propertyName, Object value) throws PageException {
        return set(propertyName, value);
    }

    @Override
    public final Object setEL(PageContext pc, Key propertyName, Object value) {
        return setEL(propertyName, value);
    }

    @Override
    public final boolean containsKey(Key key) {
        return containsKey(key.toString());
    }

    @Override
    public final Iterator<?> getIterator() {
        // calls ODocumentImpl.keyIterator which creates new KeyIterator based on the oDocument fields
        return keyIterator();
    }

    @Override
    public final Iterator<Object> valueIterator() {
        return new ValueIterator(this, keyIterator());
    }

    @Override
    public final Iterator<Entry<Key, Object>> entryIterator() {
        return new EntryIterator(this, keyIterator());
    }

    public static class KeyIterator implements Iterator<Collection.Key> {

        private final Iterator it;
        private final Cast caster;

        public KeyIterator(Cast caster, Iterator it) {
            this.caster=caster;
            this.it=it;
        }

        public void remove() {
            it.remove();
        }

        public boolean hasNext() {
            return it.hasNext();
        }

        public Collection.Key next() {
            return caster.toKey(it.next(),null);
        }
    }

    public class ValueIterator implements Iterator<Object> {

        private final Iterator<Key> it;
        private final Collection coll;

        public ValueIterator(Collection coll,Iterator<Key> it) {
            this.coll=coll;
            this.it=it;
        }

        public void remove() {
            throw new UnsupportedOperationException("this operation is not suppored");
        }

        public boolean hasNext() {
            if (it != null) {
                return it.hasNext();
            } else {
                return false;
            }
        }

        public Object next() {
            // TODO: Hack!
            // The following tries to avoid having a stack overflow error caused by nesting oDocuments.
            // The problem is that we always create new oDocumentImpl objects for the nested oDocument.
            // Can we somehow give the already created ODocumentImpl object back?
            Object el = coll.get(it.next(), null);
            if (el instanceof ODocumentImpl) return ((ODocumentImpl) el).getODocument().getIdentity();
            return el;
            //return coll.get(it.next(),null);
            //return toCFML(coll.get(it.next(),null));
        }
    }

    public class EntryIterator implements Iterator<Entry<Key, Object>> {

        private final Iterator<Key> it;
        private final Collection coll;

        public EntryIterator(Collection coll,Iterator<Key> it) {
            this.coll=coll;
            this.it=it;
        }

        public void remove() {
            throw new UnsupportedOperationException("this operation is not suppored");
        }

        public boolean hasNext() {
            return it.hasNext();
        }

        public Entry<Key, Object> next() {
            Key k = it.next();
            return new EntryImpl(coll, k, coll.get(k, null)) ;
            //return new EntryImpl(coll, k, toCFML(coll.get(k,null))) ;
        }
    }

    public static class EntryImpl implements Entry<Key, Object> {

        private final Key k;
        private Object v;
        private Collection coll;

        public EntryImpl(Collection coll,Key k,Object v){
            this.coll=coll;
            this.k=k;
            this.v=v;
        }

        @Override
        public Key getKey() { return k; }

        @Override
        public Object getValue() { return v; }

        @Override
        public Object setValue(Object value) {
            coll.setEL(k, value);
            Object tmp=v;
            v=value;
            return tmp;
        }
    }

    /*
    public DumpTable _toDumpTable(String title,PageContext pageContext, int maxlevel, DumpProperties dp) {
        Key[] keys = keys();
        DumpTable table = new DumpTable("struct","#339933","#8e714e","#000000");
        if(size()>10 && dp.getMetainfo())table.setComment("Entries:"+size());
        table.setTitle(title);
        maxlevel--;
        int maxkeys=dp.getMaxKeys();
        int index=0;
        for(int i=0; i<keys.length; i++) {
            if(keyValid(dp,maxlevel,keys[i])){
                if(maxkeys<=index++)break;
                table.appendRow(2,
                        __toDumpData(keys[i].toString(),pageContext,maxlevel,dp),
                        __toDumpData(get(keys[i].toString(),null), pageContext,maxlevel,dp)
                );
            }
        }
        return table;
    }*/

    /*
    private static boolean keyValid(DumpProperties props,int level, Collection.Key key) {
        if(props.getMaxlevel()-level>1) return true;

        // show
        Set set = props.getShow();
        if(set!=null && !set.contains(key.getLowerString()))
            return false;

        // hide
        set = props.getHide();
        if(set!=null && set.contains(key.getLowerString()))
            return false;

        return true;
    }*/
}
