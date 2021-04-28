package main.com.hlliz.PreparedStatemnt.crud;

import main.com.hlliz.bean.Order;
import main.com.hlliz.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 针对于order表的通用查询操作
 */
public class OrderForQuery {
    public Order usalQuerForOrder(String sql, Object ...agrs){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            ps = conn.prepareStatement(sql);
            for(int i=0; i<agrs.length; i++){
                ps.setObject(i+1, agrs[i]);
            }
            rs = ps.executeQuery();
            //获取结果集的元数据
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取有结果集的列数
            int count = rsmd.getColumnCount();
            if(rs.next()){
                Order order = new Order();
                for(int i=0; i<count; i++){
                    //获取每个列的值
                    Object columnValue = rs.getObject(i + 1);
                    //获取列的别名，如果没有别名就得到是列名
                    String columnLabel = rsmd.getColumnLabel(i + 1);
                    //通过反射给对象赋值
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);//设置可以给私有属性赋值
                    field.set(order,columnValue);
                }
                return order;
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,ps,rs);
        }
        return null;
    }
    @Test
    public void testUsalQuerForOrder(){
        String sql = "select order_id as orderId,order_name as orderName,order_date as orderDate from `order` where order_id=?";
        Order order = usalQuerForOrder(sql, 1);
        System.out.println(order);
    }
}
