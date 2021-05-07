package main.com.hlliz.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import javax.sql.DataSource;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DruidTest {
    @Test
    public void getConnection() throws Exception {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");
        Properties properties = new Properties();
        properties.load(is);
        DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
