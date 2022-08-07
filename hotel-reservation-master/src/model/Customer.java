package model;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    // Customer class的3个属性：姓，名，邮箱
    private String firstName;
    private String lastName;
    private String email;
    // 正则表达式的pattern, 并编译它
    private final String emailRegex = "^(.+)@(.+).(.+)$";
    private final Pattern pattern = Pattern.compile(emailRegex);

    public Customer(String firstName, String lastName, String email) {
        // 使用语法pattern.matcher(email).matches()来判断邮箱是否符合正则表达式，结果返回true或false
        // 如果返回false，需要处理错误，打印参数错误
        if (!pattern.matcher(email).matches()) {
            throw new IllegalArgumentException("Error, invalid email");
        }
        // 如果返回true, 则正常创建
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
    // 写方法：获得姓名和邮箱，改写object的字符串表达
    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return this.email;
    }

    public String toString() {
        return "Customer: " + this.firstName + " " + this.lastName + " (" + this.email + ")";
    }

    // override equals方法（这个方法在很多class都用到，思路是一致的）
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        // 则如果当前object与this不是同一个object且属于Customer这个类，就比较它的三个属性
        return firstName.equals(customer.firstName) && lastName.equals(customer.lastName) && email.equals(customer.email);
    }
    // override hashCode方法（这个方法在很多class都用到，思路是一致的）
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }
}
