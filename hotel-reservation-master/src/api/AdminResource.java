package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;

// 注意：api package里的是resource class, 它是UI组件和services的中介;
// resource class用于定义application programming interface(api)
// 而api能够很好地分开前端和后端软件
// 前端：(user interface) 包含main menu和admin menu(前者为顾客，后者为管理者)
// 中介：(resources -> api) 包含hotel reservation resource和admin resource
// 后端：(services) 包含reservation service和customer service
// 底层数据：(models) 包含freeRoom, room, reservation, customer

// 注意：resource class作为前端与后端的媒介，其各种方法的内容就是调用后端service写到的各种方法！
// admin需要的功能：获取所有顾客账户，获取所有房间信息，获取所有预约信息，往应用里添加房间
public class AdminResource {

    // 方法：通过顾客邮箱，获取对应customer object
    public static Customer getCustomer(String email) {
        // 调用CustomerService里的getCustomer方法
        return CustomerService.getCustomer(email);
    }

    // 方法：添加room object
    public static void addRoom(IRoom room) {
        // 调用ReservationService里的addRoom方法（在房间哈希表里添加pairs）
        ReservationService.addRoom(room);
    }

    // 方法：获取所有房间信息
    public static Collection<IRoom> getAllRooms() {
        // 调用ReservationService里的getAllRooms方法
        // 具体内容为通过获取房间哈希表的所有值，返回所有的room object
        return ReservationService.getAllRooms();
    }

    // 方法：获取所有顾客信息
    public static Collection<Customer> getAllCustomers() {
        // 调用CustomerService里的getAllCustomers方法
        // 返回所有Customer object
        return CustomerService.getAllCustomers();
    }

    // 方法：获取所有预约信息
    public static Collection<Reservation> getAllReservations() {
        // 调用ReservationService里的getAllReservations方法
        // 返回所有reservation object
        return ReservationService.getAllReservations();
    }

}
