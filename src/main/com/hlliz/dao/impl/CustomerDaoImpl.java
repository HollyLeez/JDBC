package main.com.hlliz.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import main.com.hlliz.bean.Customer;
import main.com.hlliz.dao.BaseDao;
import main.com.hlliz.dao.CustomersDao;

public class CustomerDaoImpl extends BaseDao<Customer> implements CustomersDao {

    @Override
    public int update(Connection conn, String sql, Object... agrs) {
        return super.update(conn, sql, agrs);
    }

    @Override
    public List<Customer> getInstance(Connection conn, String sql, Object ...args){
        return super.getInstance(conn, sql, args);
    }

    @Override
    public <E> E getValue(Connection conn, String sql, Object... args) {
        return super.getValue(conn, sql, args);
    }

    @Override
    public void insert(Connection conn, Customer customer) {
        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        super.update(conn, sql, customer.getName(),customer.getEmail(),customer.getBirth());
    }

    @Override
    public void deleteById(Connection conn, int id) {
        String sql = "delete from customers where id=?";
        super.update(conn, sql, id);
    }

    @Override
    public void updateById(Connection conn, Customer customer) {
        String sql = "update customers set name=?,email=?,birth=? where id=?";
        super.update(conn, sql, customer.getName(),customer.getEmail(),customer.getBirth(),customer.getId());
    }

    @Override
    public Customer getCustomerById(Connection conn, int id) {
        String sql="select id,name,email,birth from customers where id=?";
        List<Customer> list = super.getInstance(conn, sql, id);
        if(list!=null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Customer> getAll(Connection conn) {
        return null;
    }

    @Override
    public Long getCount(Connection conn) {
        String sql = "select count(*) from customers";
        return super.getValue(conn, sql);
    }

    @Override
    public Date getMaxBirth(Connection conn) {
        String sql = "select max(birth) from customers";
        return super.getValue(conn, sql);
    }
}
