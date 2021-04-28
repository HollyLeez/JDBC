package test;


import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCTest {
    //方式一
    @Test
    public void connectionTest() throws SQLException {
        //获取Driver实现类对象
        Driver driver = new com.mysql.jdbc.Driver();
        //jdbc:mysql  主协议：子协议
        String url = "jdbc:mysql://localhost:3306/spring_db?serverTimezone=UTC";
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");
        Connection conn = driver.connect(url,info);
        System.out.println(conn);
    }

    //方式二、对方式一的迭代，不出现第三方的api，程序有更好的可以执行
    @Test
    public void connectionTest2() throws Exception {
        //1.获取Driver实现类对象，用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/spring_db?serverTimezone=UTC";
        //3.设置用户名和密码
        Properties info = new Properties();
        info.setProperty("user","root");
        info.setProperty("password","123456");
        //4.获取连接
        Connection connect = driver.connect(url, info);
    }
    //方式三、使用DriverManager替换Driver
    @Test
    public void connectionTest3() throws Exception {
        //1.获取Driver实现类对象，用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //2.提供另外3个获取连接的基本信息
        String url = "jdbc:mysql://localhost:3306/spring_db?serverTimezone=UTC";
        String user = "root";
        String password = "123456";
        //注册驱动
        DriverManager.registerDriver(driver);
        //获取连接
        DriverManager.getConnection(url, user, password);
    }

    //方式四、只是加载驱动，不用再手动注册了
    @Test
    public void connectionTest4() throws Exception {
        //1.提供给三个基本信息
        String url = "jdbc:mysql://localhost:3306/spring_db?serverTimezone=UTC";
        String user = "root";
        String password = "123456";
        //2.加载Driver，在Mysql的Driver实现类里，有一个静态代码块已经注册过了，无需再手动注册
        Class.forName("com.mysql.jdbc.Driver");
//        Driver driver = (Driver) clazz.newInstance();
//        //注册驱动
//        DriverManager.registerDriver(driver);
        //获取连接
        DriverManager.getConnection(url, user, password);
    }
    //方式五、将数据库连接信息声明在配置文件中
    /**
     * 此种方式的好处：
     * 1、实现了数据与代码的分离，实现了解耦
     * 2、如果修改配置信息，避免重新打包程序
     */
    @Test
    public void connectionTest5() throws Exception {
        //读取配置文件
        InputStream is = JDBCTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
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
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
    }
}
