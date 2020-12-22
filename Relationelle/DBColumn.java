package Relationelle;
/**
 * @description Represents a column in the DB.
 *
 * @author MindFusion LLC
 * @version 1.0 $Date: 11/09/2016
 */
public class DBColumn {

    public String name;
    public String type;
    public String size;
    public boolean primaryKey;


    /**
     * Checks if this column is the primary key for the table.
     * @return true if this is the primary key; otherwise false.
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * Initializes a new instance of the DBColumn class
     * @param _name The name of the DB column.
     * @param _type The type of data stored in this column.
     * @param _size The max size of data that could be stored in this column.
     */
    public DBColumn(String _name, String _type, String _size) {
        name = _name;
        type = _type;
        size = _size;

        primaryKey = false;

    }
}
