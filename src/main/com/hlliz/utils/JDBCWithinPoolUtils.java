package main.com.hlliz.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JDBCWithinPoolUtils {
    //数据库连接池只提供一个
    private static ComboPooledDataSource cpds = new ComboPooledDataSource("myC3p0Source");

    /**
     * c3p0方式
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }

    /**
     * dbcp方式创建连接池
     * @return
     * @throws Exception
     */
    private static DataSource dataSource = null;
    static{
        try {
            Properties properties = new Properties();
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
            properties.load(is);
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection2() throws Exception {
        return dataSource.getConnection();
    }

    /**
     * Druid连接池
     * @return
     */
    private static DataSource dataSource1 = null;
    static {
        try {
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
            Properties properties = new Properties();
            properties.load(is);
            dataSource1 = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Connection getConnection3() throws SQLException {
        return dataSource1.getConnection();
    }
}
