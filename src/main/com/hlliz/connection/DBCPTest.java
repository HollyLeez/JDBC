package main.com.hlliz.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.junit.Test;

public class DBCPTest {
    //方式一
    @Test
    public void testGetConnection() throws SQLException {
        //创建dbcp数据库连接池
        BasicDataSource source = new BasicDataSource();
        //设置基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC");
        source.setUsername("root");
        source.setPassword("666666");
        //设置连接池管理的相关属性
        source.setInitialSize(10);
        source.setMaxActive(10);
        //获取连接
        Connection connection = source.getConnection();
        System.out.println(connection);
    }
    //方式二、配置文件
    @Test
    public void getConnection1() throws Exception {
        Properties properties = new Properties();
        //方式1、类加载器
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("dbcp.properties");
        //方式2
//        FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
        properties.load(is);
        DataSource dataSource = BasicDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
