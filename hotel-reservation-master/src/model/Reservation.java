package model;

import java.util.Date;
import java.util.Objects;

public class Reservation {
    // Reservation有4个变量：customer, room (注意它们两个本身就是object) 入住和离开时间
    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
    // 写get method
    public Date getCheckInDate() {
        return this.checkInDate;
    }

    public Date getCheckOutDate() {
        return this.checkOutDate;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public IRoom getRoom() {
        return this.room;
    }

    // 这部分的时间关系还是有点混乱？
    // 本class的方法：isRoomReserved -> 判断某个房间是否可预约
    // dateA.before(dateB)表示的是：如果dateA早于dateB, 返回true, 否则返回false
    // dateA.after(dateB)表示的是：如果dateA晚于dateB, 返回true, 否则返回false
    // 注意：此处checkInDate.before(this.checkOutDate)里，dateA表示的是用户试图预约时输入的入住日期（它是Reservation的属性），
    // 而dateB表示的是这个房间允许的离开日期（它是Room object的属性）
    // 因此要令某个房间"可预约"，需要让它空出来的最早时间早于预约的入住日期，同时空出来的最晚时间晚于预约的离开日期
    // 在这个方法里，返回true说明这个房间在对应时间段已经被预约了，即顾客预约的入住日期早于房间可入住的日期，且预约的离开时期晚于房间可离开的日期
    public boolean isRoomReserved(Date checkInDate, Date checkOutDate) {
        // 原文：if (checkInDate.before(this.checkOutDate) && checkOutDate.after(this.checkInDate))
        // 修改后（自己凭感觉改的，不知道对不对）
        if (checkInDate.before(this.checkInDate) && checkOutDate.after(this.checkOutDate)) {
            return true;
        }
        return false;
    }
    // 字符串表达：预约的顾客，房间，入住时间，离开时间
    public String toString() {
        return "Reservation for " + this.customer +
                ", " + room +
                ", Check-In on: " + this.checkInDate +
                ", Check-Out on: " + this.checkOutDate;
    }

    // 同样常见的改写equals与hashCode, 不展开
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return customer.equals(that.customer) && room.equals(that.room) && checkInDate.equals(that.checkInDate) && checkOutDate.equals(that.checkOutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, room, checkInDate, checkOutDate);
    }
}
