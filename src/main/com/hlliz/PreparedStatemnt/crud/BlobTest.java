package main.com.hlliz.PreparedStatemnt.crud;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.Test;

import main.com.hlliz.utils.JDBCUtils;

/**
 * 使用PreparedStatement操作blob类型数据
 */
public class BlobTest {
    @Test
    public void testInsert() {

        Connection conn = null;
        PreparedStatement ps = null;
        InputStream is = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1,"郑爽");
            ps.setString(2,"zhuangzirich@126.com");
            ps.setObject(3,"1993-01-01");
            is = ClassLoader.getSystemResourceAsStream("mao.png");
            ps.setBlob(4,is);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn,ps);
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    //读取Blob字段
    @Test
    public void testQuery(){
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = JDBCUtils.getConnection();
            String sql = "select photo from customers where id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,19);
            rs = ps.executeQuery();
            if(rs.next()){
                Blob photo = rs.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("luoluo.png");
                byte[] buffer = new byte[1024];
                int len;
                while((len = is.read(buffer)) != -1){
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn, ps, rs);
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
