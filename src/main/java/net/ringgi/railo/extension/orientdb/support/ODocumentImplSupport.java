package net.ringgi.railo.extension.orientdb.support;

import railo.runtime.PageContext;
import railo.runtime.exp.PageException;
import railo.runtime.type.Struct;

import java.util.Map;
import java.util.Set;

public abstract class ODocumentImplSupport extends CollObsSupport implements Struct {

    @Override
    public final boolean isEmpty() {
        return size()==0;
    }

    @Override
    public final boolean containsKey(Object key) {
        try {
            return containsKey(caster.toKey(key));
        } catch (PageException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final boolean containsValue(Object value) {
        throw new UnsupportedOperationException("this operation is not suppored");
    }

    @Override
    public Object callWithNamedValues(PageContext pc, Key methodName, Struct args) throws PageException {
        throw new UnsupportedOperationException("named arguments are not supported yet!");
    }

    @Override
    public final Object get(Object key) {
        try {
            return get(caster.toKey(key),null);
        } catch (PageException e) {
            throw new RuntimeException(e);
        }
    }

    public Object remove(Object key) {
        try {
            return remove(caster.toKey(key));
        } catch (PageException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object put(Object key, Object value) {
        try {
            return setEL(caster.toKey(key),value);
        } catch (PageException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public final void putAll(Map m) {
        putAll(this, m);
    }


    @Override
    public final Set entrySet() {
        Set entrySet = ObjectSupport.entrySet(this);
        return entrySet;
    }

    @Override
    public final java.util.Collection values() {
        java.util.Collection<?> values = ObjectSupport.values(this);
        return values;
    }

}
