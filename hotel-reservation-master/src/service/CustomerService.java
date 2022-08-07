package service;

import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// 注意，本项目中的services是stateful的，意味着它有数据存储功能，可以保存数据
// 本项目中会使用collection进行储存、提取和处理数据
// collection是高度抽象出来的集合，主要分为set/list/queue等类型
public class CustomerService {
    // 创建hash map哈希表（本质由数组+链表组成）来储存Customer object
    private static Map<String, Customer> customerMap = new HashMap<String, Customer>();

    // method: 添加顾客
    public static void addCustomer(String email, String firstName, String lastName) {
        // 通过输入的参数，创建新的customer object
        Customer newCustomer = new Customer(firstName, lastName, email);
        // 将这个object存进customerMap哈希表中，以邮箱为key, customer object为value
        customerMap.put(newCustomer.getEmail(), newCustomer);
    }

    // method:通过顾客邮箱，获取对应的Customer object
    public static Customer getCustomer(String customerEmail) {
        return customerMap.get(customerEmail);
    }

    // method:获取所有顾客object（返回hash map里的所有values）
    public static Collection<Customer> getAllCustomers() {
        return customerMap.values();
    }

}
