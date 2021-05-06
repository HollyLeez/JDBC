package main.com.hlliz.dao.impl;

import java.sql.Connection;
import java.sql.Date;

import org.junit.Test;

import main.com.hlliz.bean.Customer;
import main.com.hlliz.utils.JDBCUtils;

public class CustomerImplTest {
    private CustomerDaoImpl dao = new CustomerDaoImpl();
    @Test
    public void testInsert() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.insert(conn, new Customer(1,"猪包谷", "pigcorn@qq.com",new Date(4445553636666L)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn, null);
        }

    }
    @Test
    public void testDeleteById(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.deleteById(conn,20);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn, null);
        }
    }
    @Test
    public void testUpdateConnectionCustomer(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            dao.updateById(conn, new Customer(19, "爽子", "zhuangzirich@126.com", new Date(929889578357L)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn, null);
        }
    }
    @Test
    public void testGetCustomerById(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Customer customer = dao.getCustomerById(conn, 19);
            System.out.println(customer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn, null);
        }
    }
    @Test
    public void testGetCount(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Long count = dao.getCount(conn);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn, null);
        }
    }

    @Test
    public void testMax(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            Date birth = dao.getMaxBirth(conn);
            System.out.println(birth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResources(conn, null);
        }
    }
}
