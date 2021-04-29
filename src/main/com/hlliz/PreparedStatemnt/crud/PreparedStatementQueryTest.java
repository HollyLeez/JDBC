package main.com.hlliz.PreparedStatemnt.crud;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import main.com.hlliz.bean.Customer;
import main.com.hlliz.bean.Order;
import main.com.hlliz.utils.JDBCUtils;

/**
 * 使用PreparedStatement实现不同表的通用查询操作
 */
public class PreparedStatementQueryTest {

    public <T> List<T> getInstance(Class<T> clazz, String sql, Object ...args){
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
            ArrayList<T> list = new ArrayList<>();
            while(rs.next()){
                T t = clazz.newInstance();
                for (int i=0; i<count; i++){
                    Object columnValue = rs.getObject(i + 1);
                    //获取每个列的列名
                    String columnName = rsmd.getColumnLabel(i + 1);
                    //通过反射，给t对象指定的某个属性，赋值为value
                    Field field = clazz.getDeclaredField(columnName);
                    field.setAccessible(true);//给私有属性赋值
                    field.set(t,columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,ps,rs);
        }
        return null;
    }
    @Test
    public void testGetInstance(){
        String sql = "select order_id as orderId,order_name as orderName,order_date as orderDate " +
                "from `order` where order_id<?";
        List<Order> list = getInstance(Order.class, sql, 4);
        for (Order order : list) {
            System.out.println(order);
        }
    }
}
