package net.ringgi.railo.extension.orientdb.document;

import com.orientechnologies.orient.core.record.impl.ODocument;
import net.ringgi.railo.extension.orientdb.support.ODocumentImplSupport;
import railo.runtime.PageContext;
import railo.runtime.dump.DumpData;
import railo.runtime.dump.DumpProperties;
import railo.runtime.dump.DumpTable;
import railo.runtime.exp.PageException;
import railo.runtime.type.Collection;

import java.util.*;

public class ODocumentImpl extends ODocumentImplSupport {

    private final ODocument oDocument;

    public ODocumentImpl(ODocument oDocument) {
        if(oDocument == null) throw new RuntimeException();
        this.oDocument = oDocument;
    }

    //-----------------------------------------------
    // object size methods
    @Override
    public int size() {
        return oDocument.fields();
    }

    @Override
    public long sizeOf() {
        // TODO: Implement sizeOf()
        return 0;
    }

    //------------------------------------------------
    // object key methods
    @Override
    public Key[] keys() {
        List<Key> list=new ArrayList<Key>();
        for (String field : oDocument.fieldNames()) {
            list.add(caster.toKey(field,null));
        }
        return list.toArray(new Key[list.size()]);
    }

    @Override
    public Iterator<Key> keyIterator() {
        return new KeyIterator(caster, Arrays.asList(oDocument.fieldNames()).iterator());
    }

    @Override
    public Set keySet() {
        return new HashSet<String>(Arrays.asList(oDocument.fieldNames()));
    }

    @Override
    public boolean containsKey(String key) {
        return oDocument.containsField(key);
    }

    @Override
    public Iterator<String> keysAsStringIterator() {
        return Arrays.asList(oDocument.fieldNames()).iterator();
    }

    @Override
    public Object remove(Key key) throws PageException {
        if(oDocument.containsField(key.getString()))
            return oDocument.removeField(key.getString());
        throw exp.createApplicationException("There is no key ["+key+"] in the oDocument");
    }

    @Override
    public Object removeEL(Key key) {
        return oDocument.removeField(key.getString());
    }

    @Override
    public void clear() {
        oDocument.clear();
    }

    //------------------------------------------------
    // object get and set
    @Override
    public Object get(String key) throws PageException {
        if (key.equals("@rid")) return toCFML(oDocument.getIdentity());
        if (key.equals("@version")) return toCFML(oDocument.getVersion());
        if (key.equals("@class")) return toCFML(oDocument.getClassName());
        if (oDocument.containsField(key)) return toCFML(oDocument.field(key));
        /*
        if (oDocument.containsField(key)) {
            Object el = oDocument.field(key);
            if (el instanceof ODocument && ((ODocument) el).getIdentity().equals(oDocument.getIdentity())) {
                return toCFML(((ODocument) el).getIdentity());
            }
            return toCFML(el);
        }*/
        throw exp.createApplicationException("There is no key ["+key+"] in the DBObject");
    }

    @Override
    public Object get(String key, Object defaultValue) {
        if(key.equals("@rid")) return toCFML(oDocument.getIdentity());
        if(key.equals("@version")) return toCFML(oDocument.getVersion());
        if(key.equals("@class")) return toCFML(oDocument.getClassName());
        if (oDocument.containsField(key)) return toCFML(oDocument.field(key));
        /*
        if (oDocument.containsField(key)) {
            Object el = oDocument.field(key);
            if (el instanceof ODocument && ((ODocument) el).getIdentity().equals(oDocument.getIdentity())) {
                return toCFML(((ODocument) el).getIdentity());
            }
            return toCFML(el);
        }*/
        return defaultValue;
    }

    @Override
    public Object set(String key, Object value) throws PageException {
        oDocument.field(key, toOrient(value));
        oDocument.getDatabase().save(oDocument);
        return oDocument;
    }

    @Override
    public Object setEL(String key, Object value) {
        oDocument.field(key, toOrient(value));
        oDocument.getDatabase().save(oDocument);
        return oDocument;
    }

    @Override
    public final Collection duplicate(boolean deepCopy) {
        return new ODocumentImpl(oDocument.copy());
    }


    //------------------------------------------------
    // API Implementation underlying object
    @Override
    public Object call(PageContext pageContext, Key methodName, Object[] args) throws PageException {

        //http://www.orientechnologies.com/javadoc/latest/index.html?com/orientechnologies/orient/core/record/impl/ODocument.html
        // TODO: Implement more methods of the API

        //getODocument()
        if (methodName.equals("getODocument")) {
            checkArgLength("getODocument", args, 0, 0);
            return getODocument();
        }

        // getIdentity()
        else if (methodName.equals("getIdentity")) {
            checkArgLength("getIdentity", args, 0, 0);
            return toCFML(oDocument.getIdentity());
        }

        // fieldNames()
        else if (methodName.equals("fieldNames")) {
            checkArgLength("getClusters", args, 0, 0);
            return toCFML(Arrays.asList(oDocument.fieldNames()));
        }
        // field()
        else if (methodName.equals("field")) {
            checkArgLength("field", args, 1, 2);
            if (args.length == 1) {
                return toCFML(oDocument.field(caster.toString(args[0])));
            } else {
                oDocument.field(caster.toString(args[0]), toOrient(args[1]));
                oDocument.getDatabase().save(oDocument);
                return this;
            }
        }
        // delete();
        else if (methodName.equals("delete")) {
            checkArgLength("delete", args, 0, 0);
            oDocument.delete();
            return true;
        }

        String supportedFunctions= "getODocument,fieldNames,field,getIdentity,delete";
        throw exp.createExpressionException("function ["+methodName+"] is not supported, supported functions are ["+supportedFunctions+"]");

    }

    //------------------------------------------------
    // Helper
    @Override
    public DumpData toDumpData(PageContext pageContext, int maxlevel, DumpProperties dp) {

        DumpTable table = new DumpTable("struct","#339933","#8e714e","#000000");
        table.setTitle("ODocument");
        maxlevel--;
        table.appendRow(2,
                __toDumpData("@class", pageContext, maxlevel, dp),
                __toDumpData(toCFML(oDocument.getClassName()), pageContext, maxlevel, dp)
        );

        table.appendRow(2,
                __toDumpData("@rid", pageContext, maxlevel, dp),
                __toDumpData(toCFML(oDocument.getIdentity()), pageContext, maxlevel, dp)
        );
        table.appendRow(2,
                __toDumpData("@version", pageContext, maxlevel, dp),
                __toDumpData(toCFML(oDocument.getVersion()), pageContext, maxlevel, dp)
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
        return table;
    }

    public ODocument getODocument() {
        return oDocument;
    }
}
