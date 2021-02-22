// Tucker Arnold
// CS310 - Software Engineering I

package databasetest;

import java.sql.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DatabaseTest {

    //Method named getJSONData for unit test
    public static JSONArray getJSONData(){
        
        JSONArray jsonA = new JSONArray();
        
        Connection conn = null;
        PreparedStatement pstSelect = null; 
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
        
        String query, key, value;
        
        boolean hasresults;
        int resultCount, columnCount, updateCount = 0;
        
        try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/db_test");
            String username = "root";
            String password = "CS488";
            
            /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            /* Open Connection */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (conn.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
                
                // Prepare Update Query
                
                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query);
                
                // Execute Update Query
                
                hasresults = pstSelect.execute();
                
                // Get New Key; Print To Console                
                 
                /* Prepare Select Query */
                
                query = "SELECT * FROM people";
                pstSelect = conn.prepareStatement(query);
                
                /* Execute Select Query */                
                
                hasresults = pstSelect.execute();                
                
                /* Get Results */                                
                while ( hasresults || pstSelect.getUpdateCount() != -1 ) {

                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstSelect.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();
                        
                        /* Get Column Names; Print as Table Header */
                        String[] columnNames = new String[columnCount];
                        
                        for (int i = 2; i <= columnCount; i++) {
                            columnNames[i-2] = metadata.getColumnLabel(i);
                        }
                        
                        /* Get Data; Print as Table Rows */
                        
                        while(resultset.next()) {                                                       
                        /* Loop Through ResultSet Columns; Print Values */
                        JSONObject jsonObject = new JSONObject();        

                            for (int i = 2; i <= columnCount; i++) {

                                value = resultset.getString(i);
                                jsonObject.put(columnNames[i-2], value);
                            }
                            jsonA.add(jsonObject);
                        }                        
                    }

                    else {
                        resultCount = pstSelect.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }
                    }
                    
                    /* Check for More Data */

                    hasresults = pstSelect.getMoreResults();
                }               
            }                        
            
            /* Close Database Connection */
            
            conn.close();            
        }
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstSelect != null) { try { pstSelect.close(); pstSelect = null; } catch (Exception e) {} }            
            
        }
        System.out.println(jsonA.toString());       
        return jsonA;        
    }
    
    public static void main(String[] args){
        getJSONData();
    }
}