package com.steam.app.pdaOrder;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {

    String ip = "192.168.56.1";
    String classs = "net.sourceforge.jtds.jdbc.Driver";
    String db = "smartBonus_lite";
    String un = "sa";
    String password = "123456";

    private static String port = "57169";

    @SuppressLint("NewApi")
    public Connection CONN() {
        Connection conn = null;
        String ConnURL = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            Class.forName(classs);
            ConnURL = "jdbc:jtds:sqlserver://" + ip  + ":" + port + ";"
                    + "databaseName=" + db + ";user=" + un + ";password="
                    + password + ";";
            conn = DriverManager.getConnection(ConnURL);
            Log.e("OK", "ok");
        } catch (SQLException e) {
            Log.e("ERROR", e.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
        }
        return conn;
    }
}