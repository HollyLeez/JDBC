package main.com.hlliz.dao;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import main.com.hlliz.bean.Customer;

/**
 * 此接口用于规范Customers表的常用操作
 */
public interface CustomersDao {
    /**
     * 将customer对象添加到数据表中
     * @param conn
     * @param customer
     */
    void insert(Connection conn, Customer customer);

    /**
     * 根据指定id删除一条记录
     * @param conn
     * @param id
     */
    void deleteById(Connection conn, int id);

    /**
     * 针对内存中的customer对象去修改数据库中的记录
     * @param conn
     * @param customer
     */
    void updateById(Connection conn, Customer customer);

    /**
     * 根据指定id查询customer对象
     * @param conn
     * @param id
     */
    Customer getCustomerById(Connection conn, int id);

    /**
     * 查询customer表中所有记录
     * @param conn
     * @return
     */
    List<Customer> getAll(Connection conn);

    /**
     * 查询有多少条记录
     * @param conn
     * @return
     */
    Long getCount(Connection conn);

    /**
     * 返回最大的birth
     * @param conn
     * @return
     */
    Date getMaxBirth(Connection conn);
}
