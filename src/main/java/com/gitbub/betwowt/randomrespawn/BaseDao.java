package com.gitbub.betwowt.randomrespawn;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaseDao {
    private static Plugin plugin = Main.getMain();

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + plugin.getConfig().getString("mysql.host") + "/"
                            + plugin.getConfig().getString("mysql.database"), plugin.getConfig().getString("mysql.username"),
                    plugin.getConfig().getString("mysql.password"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 增删改方法
     * 增：insert into 表名 values (?)
     * 删：delete from 表名 where 列名 = ?
     * 改：update 表名 set 列名 = ?
     * ==>需要传入一个参数给?赋值,如果有多个?就需要传入对应个?参数值
     */
    public static int executeUpdate(String sql, Object... objs) throws SQLException {
        int count = 0;
        Connection conn = getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            if (objs != null) {
                for (int i = 0; i < objs.length; i++) {
                    pstmt.setObject(i + 1, objs[i]);
                }
            }
            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeAll(conn, pstmt, null);
        }
        return count;
    }

    /**
     * 查询列表方法,返回一个字符串集合,每个值用|分开,每行用,分开
     * QuerySize==查询的列的大小
     */
    public static List<String> SelectAll(String sql, int QuerySize) throws SQLException {
        List<String> list = new ArrayList();
        PreparedStatement pstm = null;
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            while (rs.next()) {
                String str = "";
                for (int i = 1; i < QuerySize+1; i++) {
                    if (i == QuerySize )
                        str = str + rs.getObject(i);
                    else
                        str = str + rs.getObject(i) + "|";
                }
                list.add(str);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstm, rs);
        }
        return list;
    }

    /**
     * 查询某个列是否存在
     */
    public static boolean IsExsit(String sql, Object... obj) throws SQLException {
        Connection conn = getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = conn.prepareStatement(sql);
            if (obj != null) {
                for (int i = 0; i < obj.length; i++) {
                    pstm.setObject(i + 1, obj[i]);
                }
            }
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(conn, pstm, rs);
        }
        return false;
    }



}
