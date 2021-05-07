package main.com.hlliz.connection;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

public class C3P0Test {
    //方式一
    @Test
    public void testGetConnection() throws Exception {
        //获取c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test?serverTimezone=UTC&rewriteBatchedStatements=true");
        cpds.setUser("root");
        cpds.setPassword("666666");
        //设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        Connection conn = cpds.getConnection();
        System.out.println(conn);
        //销毁c3p0数据库连接池，一般不会用到
        DataSources.destroy(cpds);
    }
    //方式二、使用配置文件
    @Test
    public void testGetConnection1() throws SQLException {
        ComboPooledDataSource cpds = new ComboPooledDataSource("myC3p0Source");
        Connection connection = cpds.getConnection();
        System.out.println(connection);
    }
}
