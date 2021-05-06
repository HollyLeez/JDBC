package main.com.hlliz.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import main.com.hlliz.utils.JDBCUtils;

/**
 * 1、什么是事务：一组逻辑单元，一个或多个DML操作
 * 2、事务处理的原则：要么所有操作都提交，要么所有操作都回滚
 * 3、数据一旦提交就不可回滚
 * 4、哪些操作会导致数据的自动提交
 *      >DDL操作
 *      >DML执行就会提交，可以通过 set autocommit=false的方式取消DML操作的自动提交
 *      >关闭连接时会默认全部自动提交
 */
public class TransactionTest {

    /**
     * 通用增删改，version1.0
     */
    public int update(String sql, Object ...agrs){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.获取连接
            conn = JDBCUtils.getConnection();
            //2.预编译sql语句
            ps = conn.prepareStatement(sql);
            //3.填充占位符
            for(int i=0; i<agrs.length; i++){
                ps.setObject(i+1,agrs[i]);
            }
            //4.执行操作
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closeResources(conn,ps);
        }
        return 0;
    }

    /**
     * 未考虑数据事务情况的下的转账操作
     * 针对user_table来说：
     * AA用户给BB用户转账100
     */
    @Test
    public void testUpdate(){
        String sql1 = "update user_table set balance=balance-100 where user=?";
        update(sql1,"AA");
        //模拟异常
        System.out.println(10/0);
        String sql2 = "update user_table set balance=balance+100 where user=?";
        update(sql2,"BB");

        System.out.println("转账成功");
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
    @Test
    public void testUpdateWithTx() throws Exception {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            //取消数据的自动提交
            conn.setAutoCommit(false);
            System.out.println(conn.getAutoCommit());
            String sql1 = "update user_table set balance=balance-100 where user=?";
            update(conn, sql1,"AA");
            //模拟异常
            System.out.println(10/0);

            String sql2 = "update user_table set balance=balance+100 where user=?";
            update(conn, sql2,"BB");

            System.out.println("转账成功");
            //提交数据
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            conn.rollback();
        } finally {
            //还原为自动提交数据，主要针对于使用数据库连接池时
            conn.setAutoCommit(true);
            JDBCUtils.closeResources(conn, null);
        }
    }
}
