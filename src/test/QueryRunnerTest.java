package test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.xml.ws.handler.Handler;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.Test;

import main.com.hlliz.bean.Customer;
import main.com.hlliz.utils.JDBCUtils;
import main.com.hlliz.utils.JDBCWithinPoolUtils;

public class QueryRunnerTest {
    @Test
    public void testInsert()  {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCWithinPoolUtils.getConnection3();
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            int count = runner.update(connection, sql, "蔡徐坤", "caiKun@qq.com", "1999-05-01");
            System.out.println("添加了"+count+"条记录");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(connection,null);
        }
    }

    /**
     * BeanHandler封装返回对象
     */
    @Test
    public void testQuery1(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCWithinPoolUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id=?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);
            Customer customer = runner.query(conn, sql, handler, 19);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,null);
        }

    }

    /**
     * BeanListHandler封装返回多条对象记录
     */
    @Test
    public void testQuery2(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCWithinPoolUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id<?";
            BeanListHandler<Customer> handler = new BeanListHandler<>(Customer.class);
            List<Customer> list = runner.query(conn, sql, handler, 10);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,null);
        }

    }

    /**
     * MapHandler:将字段及值最为map的key/value返回
     */
    @Test
    public void testQuery3(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCWithinPoolUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id=?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(conn, sql, handler, 10);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,null);
        }

    }

    /**
     * MapListHandler：多条记录，每一条作为map返回
     */
    @Test
    public void testQuery4(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCWithinPoolUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id<?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> list = runner.query(conn, sql, handler, 10);
            for (Map<String, Object> map : list) {
                System.out.println(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,null);
        }
    }

    /**
     * ScalarHandler:用户查询聚合函数等特殊值
     */
    @Test
    public void testQuery5(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCWithinPoolUtils.getConnection3();
            String sql = "select count(*) from customers";
            ScalarHandler handler = new ScalarHandler();
            Long count = (Long) runner.query(conn, sql, handler);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,null);
        }
    }
    /**
     * ScalarHandler:用户查询聚合函数等特殊值
     */
    @Test
    public void testQuery6(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCWithinPoolUtils.getConnection3();
            String sql = "select max(birth) from customers";
            ScalarHandler handler = new ScalarHandler();
            Date date = (Date) runner.query(conn, sql, handler);
            System.out.println(date);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,null);
        }
    }

    /**
     * 自定义ResultSetHandler实现类
     */
    @Test
    public void testQuery7(){
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = JDBCWithinPoolUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id=?";
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet rs) throws SQLException {
                    if(rs.next()){
                        return new Customer(rs.getInt("id"),rs.getString("name"),
                                rs.getString("email"),rs.getDate("birth"));
                    }
                    return null;
                }
            };
            Customer customer = runner.query(conn, sql, handler, 19);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,null);
        }
    }
}
