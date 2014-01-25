package geoscript.workspace

import org.geotools.data.DataStore
import org.geotools.data.postgis.PostgisNGDataStoreFactory

/**
 * A PostGIS Workspace connects to a PostGIS database.
 * <p><blockquote><pre>
 * PostGIS postgis = new PostGIS("database", "localhost", "5432", "public", "user", "password")
 * </pre></blockquote></p>
 * @author Jared Erickson
 */
class PostGIS extends Database {

    /**
     * Create a new PostGIS Workspace with a name, host, port, schema, user, and password.
     * <p><blockquote><pre>
     * PostGIS postgis = new PostGIS("database", "localhost", "5432", "public", "user", "password")
     * </pre></blockquote></p>
     * @param name The database name
     * @param host The host name
     * @param port The port
     * @param schema The database schema
     * @param user The user name
     * @param password The password
     * @param estimatedExtent Whether to estimate the extent or not
     */
    PostGIS (String name, String host, String port, String schema, String user, String password, boolean estimatedExtent = false) {
        super(createDataStore(name, host, port, schema, user, password, estimatedExtent, false, ""))
    }
    
    /**
     * Create a new PostGIS with just a database name using defaults for other values.
     * <p><blockquote><pre>
     * PostGIS postgis = new PostGIS("database", user: 'me', password: 'supersecret'
     * </pre></blockquote></p>
     * @param options The options for connecting to a PostGIS database (host, port, schema, user, password,
     * estimatedExtent, createDatabase, and createDatabaseParams)
     * @param name The database name
     */
    PostGIS (Map options = [:], String name) {
        this(name, options.get("host","localhost"), options.get("port","5432"), options.get("schema","public"),
            options.get("user",System.getProperty("user.name")), options.get("password",null),
            options.get("estimatedExtent",false) as boolean,
            options.get("createDatabase", false) as boolean, options.get("createDatabaseParams","")
        )
    }

    /**
     * Get the format
     * @return The workspace format name
     */
    String getFormat() {
        return "PostGIS"
    }

    /**
     * Create a new PostGIS DataStore with a name, host, port, schema user, password,
     * and whether to estimate the extent or not
     */
    private static DataStore createDataStore(String name, String host, String port, String schema,
        String user, String password, boolean estimatedExtent, boolean createDatabase, String createDatabaseParams) {
        Map params = [:]
        params.put("database", name)
        params.put("host", host)
        params.put("port", port)
        params.put("schema", schema)
        params.put("user", user)
        params.put("passwd", password)
        params.put("Estimated extends", String.valueOf(estimatedExtent))
        params.put("dbtype", "postgis")
        params.put("create database", createDatabase)
        params.put("create database params", createDatabaseParams)
        PostgisNGDataStoreFactory f = new PostgisNGDataStoreFactory()
        f.createDataStore(params)
    }

    /**
     * Delete the database.
     * @param name The database name
     * @param host The host
     * @param port The port
     * @param user The user name
     * @param password The password
     */ 
    static void deleteDatabase(String name, String host, String port, String user, String password) {
        PostgisNGDataStoreFactory f = new PostgisNGDataStoreFactory()
        f.dropDatabase([
            database: name,
            host: host,
            port: port,
            user: user,
            password: password
        ])
    }

    /**
     * Delete the database.
     * @param options The named parameters
     * <ul>
     *   <li> host = The host (localhost by default)</li>
     *   <li> port = The port (5432 by default)</li>
     *   <li> user = The user name (the system user name by default)</li>
     *   <li> password = The password (null by default)</li>
     * </ul>
     * @param name The database name
     */ 
    static void deleteDatabase(Map options = [:], String name) {
        deleteDatabase(name, options.get("host","localhost"), options.get("port","5432"),
            options.get("user",System.getProperty("user.name")), options.get("password",null)
        )
    }
}
