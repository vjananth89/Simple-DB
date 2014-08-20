package simpledb;  

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The Catalog keeps track of all available tables in the database and their
 * associated schemas.
 * For now, this is a stub catalog that must be populated with tables by a
 * user program before it can be used -- eventually, this should be converted
 * to a catalog that reads a catalog table from disk.
 */

public class Catalog {

    protected Vector<DbFile> dbFiles;
    protected Vector<TupleDesc> tds;
    protected Vector<String> tableNames;
    protected Vector<Integer> dbFileIds;
	
    /**
     * Constructor.
     * Creates a new, empty catalog.
     */
    public Catalog() {
		dbFiles = new Vector<DbFile>();
		tds = new Vector<TupleDesc>();
		tableNames = new Vector<String>();
		dbFileIds = new Vector<Integer>();
    }

    /**
     * Add a new table to the catalog.
     * This table's contents are stored in the specified DbFile.
     * @param file the contents of the table to add;  file.getId() is the identifier of
     *    this file/tupledesc param for the calls getTupleDesc and getFile
     * @param name the name of the table -- may be an empty string.  May not be null.  If a name
     * conflict exists, use the last table to be added as the table for a given name.
     * @param pkeyField the name of the primary key field
     */
    public void addTable(DbFile file, String name, String pkeyField) {
		dbFileIds.add(file.getId());
		dbFiles.add(file);
		tds.add(file.getTupleDesc());
		tableNames.add(name);
    }

    /**
     * Add a new table to the catalog.
     * This table's contents are stored in the specified DbFile.
     * @param file the contents of the table to add;  file.getId() is the identifier of
     *    this file/tupledesc param for the calls getTupleDesc and getFile
     * @param name the name of the table -- may be an empty string.  May not be null.  If a name
     * conflict exists, use the last table to be added as the table for a given name.
     */
    public void addTable(DbFile file, String name) {
        addTable(file,name,"");
    }

    /**
     * Return the id of the table with a specified name,
     * @throws NoSuchElementException if the table doesn't exist
     */
    public int getTableId(String name) {
		if (name == null)
			throw new NoSuchElementException("Specified name is null");
		if (tableNames == null)
			throw new NoSuchElementException("All table names are null");
		int index = tableNames.indexOf(name);
		if (index < 0) {
			throw new NoSuchElementException("Table with specified name was not found");
		}
		return dbFileIds.get(index);
    }

    /**
     * Returns the tuple descriptor (schema) of the specified table
     * @param tableid The id of the table, as specified by the DbFile.getId()
     *     function passed to addTable
     */
    public TupleDesc getTupleDesc(int tableid) throws NoSuchElementException {
    // assignment 1 code implemented here
    	if(dbFileIds.contains(tableid))
    	 return dbFiles.get(dbFileIds.indexOf(tableid)).getTupleDesc();
     throw new NoSuchElementException();
    }

    /**
     * Returns the DbFile that can be used to read the contents of the
     * specified table.
     * @param tableid The id of the table, as specified by the DbFile.getId()
     *     function passed to addTable
     */
    public DbFile getDbFile(int tableid) throws NoSuchElementException {
        // assignment 1 code implemented here
    if(dbFileIds.contains(tableid))
    	return dbFiles.get(dbFileIds.indexOf(tableid));
    throw new NoSuchElementException();
    }

    /** Delete all tables from the catalog */
    public void clear() {
        dbFileIds.clear();
        dbFiles.clear();
        tds.clear();
        tableNames.clear();
    }

    public String getPrimaryKey(int tableid) {
        // assignment 1 code implemented here
    	if (dbFileIds.contains(tableid))
    		return tableNames.get(dbFileIds.indexOf(tableid));
        return null;
    }

    public Iterator<Integer> tableIdIterator() {
        // assignment 1 code implemented here
    	return dbFileIds.iterator();
 
    }

    public String getTableName(int id) {
        // assignment 1 code implemented here
    	if(dbFileIds.contains(id))
    		return tableNames.get(dbFileIds.indexOf(id));
        return null;
    }
    
    /**
     * Reads the schema from a file and creates the appropriate tables in the database.
     * @param catalogFile
     */
    public void loadSchema(String catalogFile) {
        String line = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(catalogFile)));

            while ((line = br.readLine()) != null) {
                //assume line is of the format name (field type, field type, ...)
                String name = line.substring(0, line.indexOf("(")).trim();
                //System.out.println("TABLE NAME: " + name);
                String fields = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim();
                String[] els = fields.split(",");
                ArrayList<String> names = new ArrayList<String>();
                ArrayList<Type> types = new ArrayList<Type>();
                String primaryKey = "";
                for (String e : els) {
                    String[] els2 = e.trim().split(" ");
                    names.add(els2[0].trim());
                    if (els2[1].trim().toLowerCase().equals("int"))
                        types.add(Type.INT_TYPE);
                    else if (els2[1].trim().toLowerCase().equals("string"))
                        types.add(Type.STRING_TYPE);
                    else {
                        System.out.println("Unknown type " + els2[1]);
                        System.exit(0);
                    }
                    if (els2.length == 3) {
                        if (els2[2].trim().equals("pk"))
                            primaryKey = els2[0].trim();
                        else {
                            System.out.println("Unknown annotation " + els2[2]);
                            System.exit(0);
                        }
                    }
                }
                Type[] typeAr = types.toArray(new Type[0]);
                String[] namesAr = names.toArray(new String[0]);
                TupleDesc t = new TupleDesc(typeAr, namesAr);
                HeapFile tabHf = new HeapFile(new File(name + ".dat"), t);
                addTable(tabHf,name,primaryKey);
                System.out.println("Added table : " + name + " with schema " + t);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (IndexOutOfBoundsException e) {
            System.out.println ("Invalid catalog entry : " + line);
            System.exit(0);
        }
    }
}

