package main.com.hlliz.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.com.hlliz.utils.JDBCUtils;

/**
 * 封装了针对于数据表的通用操作
 */
public abstract class BaseDao<T> {

    private Class<T> clazz = null;
    //获取当前BaseDao的子类继承父类中的泛型
    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();//this代表创建的子类对象
        ParameterizedType paramType = (ParameterizedType) genericSuperclass;
        Type[] typeArguments = paramType.getActualTypeArguments();//获取父类泛型参数
        clazz = (Class<T>) typeArguments[0];//泛型的第一个参数
    }
    /**
     * 通用增删改操作2.0（考虑事务）
     */
    public int update(Connection conn, String sql, Object ...agrs){
        PreparedStatement ps = null;
        try {
            //1.预编译sql语句
            ps = conn.prepareStatement(sql);
            //2.填充占位符
            for(int i=0; i<agrs.length; i++){
                ps.setObject(i+1,agrs[i]);
            }
            //3.执行操作
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4.关闭资源，不关闭连接，因为关闭连接会自动提交
            JDBCUtils.closeResources(null,ps);
        }
        return 0;
    }

    /**
     * 通用查询
     * @return list
     */
    public List<T> getInstance(Connection conn, String sql, Object ...args){
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
            JDBCUtils.closeResources(null,ps,rs);
        }
        return null;
    }

    /**
     * 聚合函数sql通用方法
     */
    public <E> E getValue(Connection conn, String sql, Object ...args){
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            for(int i=0; i<args.length; i++){
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            if(rs.next()){
                return (E) rs.getObject(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(null,ps,rs);
        }
        return null;
    }
}
