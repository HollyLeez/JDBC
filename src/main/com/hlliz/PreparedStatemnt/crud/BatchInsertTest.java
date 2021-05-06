package main.com.hlliz.PreparedStatemnt.crud;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.Test;

import main.com.hlliz.utils.JDBCUtils;

/**
 * 使用PreparedStatement实现批量数据的操作
 *
 * 向goods表中插入2w条数据
 * create TABLE goods(
 * id INT PRIMARY KEY auto_increment,
 * name VARCHAR(25)
 * );
 */

public class BatchInsertTest {
    //方式一、使用Statement插入，效率最低，这里不写了；
    //方式二、使用PreparedStatement
    @Test
    public void inserTest1(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods (name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i=1; i<=20000; i++){
                ps.setString(1,"name_"+i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("执行时间："+(end-start)+"毫秒");
            //执行时间：494019毫秒
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量插入方式三：
     * 1、addBatch()/executeBatch()/clearBatch()
     * 2、mysql服务器默认是关闭批处理的，我们需要用过一个参数，让mysql开启批处理的支持。
     *      ?rewriteBatchedStatements=true
     *      写在配置文件的url后面
     * 3、清空表，重新执行
     */
    @Test
    public void inserTest2(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods (name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i=1; i<=20000; i++){
                ps.setString(1,"name_"+i);
                //1、添加到batch
                ps.addBatch();
                //2、每多少条数据执行一次
                if(i % 500 == 0){
                    ps.executeBatch();
                    //3、清空batch
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("执行时间："+(end-start)+"毫秒");
            //执行时间：3222毫秒
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量插入方式四：设置连接不允许自动提交数据
     */
    @Test
    public void inserTest3(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            //设置不允许自动提交数据
            conn.setAutoCommit(false);

            String sql = "insert into goods (name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i=1; i<=20000; i++){
                ps.setString(1,"name_"+i);
                //1、添加到batch
                ps.addBatch();
                //2、每多少条数据执行一次
                if(i % 500 == 0){
                    ps.executeBatch();
                    //3、清空batch
                    ps.clearBatch();
                }
            }
            //提交数据
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println("执行时间："+(end-start)+"毫秒");
            //执行时间：1864毫秒
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
