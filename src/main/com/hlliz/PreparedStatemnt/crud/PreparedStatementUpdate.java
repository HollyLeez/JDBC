package main.com.hlliz.PreparedStatemnt.crud;

import main.com.hlliz.utils.JDBCUtils;
import org.junit.Test;

import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

public class PreparedStatementUpdate {
    //向customer表中添加一条数据
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            //读取配置文件
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(is);
            //获取配置信息
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");
            String driver = properties.getProperty("driver");
            //加载驱动
            Class.forName(driver);
            //获取连接
            connection = DriverManager.getConnection(url, user, password);
            System.out.println(connection);
            //预编译sql语句，返回PreparedStatement实例
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            ps = connection.prepareStatement(sql);
            //填充占位符
            ps.setString(1,"张惠妹");
            ps.setString(2,"amei@163.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse("1972-08-09");
            ps.setDate(3,new Date(date.getTime()));
            //执行sql
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //资源关闭
            if (ps!=null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection!=null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 修改数据操作
     */
    @Test
    public void updateTest() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取数据库连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句，获取PreparedStatement实例
            String sql = "update customers set name=? where id=?";
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            ps.setString(1, "贝多芬");
            ps.setInt(2, 18);
            //4.执行
            ps.execute();
        } catch(Exception e){
            e.printStackTrace();
        } finally {
            //5.资源关闭
            JDBCUtils.closeResources(conn,ps);
        }
    }

    public void update(String sql, Object ...agrs){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for(int i=0; i<agrs.length; i++){
                ps.setObject(i+1,agrs[i]);
            }
            //4.执行操作
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closeResources(conn,ps);
        }
    }
    @Test
    public void testUpdate(){
//        String sql = "delete from customers where id=?";
//        update(sql,6);
        String sql = "update `order` set order_name=? where order_id=?";
        update(sql,"DD",2);
    }
}
