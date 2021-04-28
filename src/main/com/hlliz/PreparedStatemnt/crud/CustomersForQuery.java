package main.com.hlliz.PreparedStatemnt.crud;

import main.com.hlliz.bean.Customer;
import main.com.hlliz.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * 针对cutomers表的查询操作
 */
public class CustomersForQuery {
    @Test
    public void testQuery1(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth from customers where id=?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1,1);
            rs = ps.executeQuery();
            //处理结果集
            if(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                Date birth = rs.getDate(4);
                Customer customer = new Customer(id,name,email,birth);
                System.out.println(customer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,ps,rs);
        }

    }

    /**
     * 针对customers表的通用查询操作
     */
    public Customer queryForCustomers(String sql, Object ...args){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i=0; i<args.length; i++){
                ps.setObject(i+1,args[i]);
            }
            rs = ps.executeQuery();
            //获取结果集元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //通过ResultSetMetaData获取结果集中的列数
            int count = rsmd.getColumnCount();
            if(rs.next()){
                Customer customer = new Customer();
                for (int i=0; i<count; i++){
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnName(i + 1);
                    //通过反射，给customer对象指定的某个属性，赋值为value
                    Field field = Customer.class.getDeclaredField(columnName);
                    field.setAccessible(true);//给私有属性赋值
                    field.set(customer,columnValue);
                }
                return customer;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,ps,rs);
        }
        return null;
    }
    @Test
    public void testQueryForCustomers(){
        String sql = "select id,name,email,birth from customers where id=?";
        Customer customer = queryForCustomers(sql, 7);
        System.out.println(customer);
    }
}
