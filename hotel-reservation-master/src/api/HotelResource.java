package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

// 注意：resource class作为前端与后端的媒介，其各种方法的内容就是调用后端service写到的各种方法！
// user需要的功能：创建账号，查找房间，预约房间，查看预约信息
public class HotelResource {

    // 方法：通过顾客邮箱，获取对应customer object
    public static Customer getCustomer(String email) {
        // 调用CustomerService里的getCustomer方法
        return CustomerService.getCustomer(email);
    }

    // 方法：创建用户账号（传入参数为客户邮箱和姓名）
    public static void createCustomer(String email, String firstName, String lastName) {
        // 调用CustomerService里的addCustomer方法 -> 将这个object存进customerMap哈希表中
        CustomerService.addCustomer(email, firstName, lastName);
    }

    // 方法：通过房号获取对应的room object
    public static IRoom getRoom(String roomNumber) {
        // 调用ReservationService里的getRoom方法
        return ReservationService.getRoom(roomNumber);
    }

    // 方法：预约房间（传入的参数为要预约的顾客邮箱，要预约的房间，入住和离开时间）
    public static Reservation bookRoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        // 首先通过调用CustomerService里的getCustomer方法，使用邮箱获取对应的customer object
        Customer customer = CustomerService.getCustomer(customerEmail);
        // 然后调用ReservationService里的reserveRoom方法，最后返回本次的Reservation object
        // 注意，如果该房间object在这个时间段里已经被预约，结果会返回none
        return ReservationService.reserveRoom(customer, room, checkInDate, checkOutDate);
    }

    // 方法：通过顾客的邮箱，获取对方所有预约信息
    public static Collection<Reservation> getCustomerReservations(String customerEmail) {
        // 首先通过调用CustomerService里的getCustomer方法，使用邮箱获取对应的customer object
        Customer customer = CustomerService.getCustomer(customerEmail);
        // 然后调用ReservationService里的getCustomerReservations方法
        // 在预约哈希表里获取该顾客对应的reservation object并返回
        return ReservationService.getCustomerReservations(customer);
    }

    // 方法：查找该时间段内可以预约的room object
    public static Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        // 调用ReservationService里的findRooms方法，最终返回该时间段里可预约的所有room object
        return ReservationService.findRooms(checkInDate, checkOutDate);
    }

}
