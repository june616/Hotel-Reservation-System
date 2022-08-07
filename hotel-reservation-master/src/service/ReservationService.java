package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.*;

public class ReservationService {
    // 创建哈希表来储存IRoom(但注意的是，IRoom是接口，无法实例化，但继承了它的Room object可以)，所以这个表就是用来存酒店房间的
    private static final Map<String, IRoom> roomMap = new HashMap<String, IRoom>();
    // 创建哈希表来储存Reservation object(Reservation有4个变量：customer, room (注意它们两个本身就是object) 入住和离开时间)
    // 所以这个表是用来存预定房间相关信息的
    private static final Map<String, Collection<Reservation>> reservationMap = new HashMap<String, Collection<Reservation>>();

    // method: 添加房间，接收参数为room object
    // 具体操作是在房间哈希表里添加pairs: key为room object里的房号属性，value为room object本身
    public static void addRoom(IRoom room) {
        roomMap.put(room.getRoomNumber(), room);
    }

    // method:通过房间号，获取对应的room object
    // 具体操作是在房间哈希表里通过key=房号，来获得对应的room object
    public static IRoom getRoom(String roomNumber) {
        return roomMap.get(roomNumber);
    }

    // method: 预约房间 -> 传入的参数为reservation class实例化所需的4个变量（要预约的顾客，要预约的房间，入住和离开时间）
    public static Reservation reserveRoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        // validate room is not already reserved
        // 检查这个房间在这个入住和离开的时间段是否"可预约"，如果返回true说明该房间不能被预约，返回null
        if (isRoomReserved(room, checkInDate, checkOutDate)) {
            return null;
        }
        // 如果该房间可预约，则创建Reservation object
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        // 通过调用getCustomerReservations方法来获取这位顾客此前预约的内容（包括入住离开时间和房间等信息）
        Collection<Reservation> customerReservations = getCustomerReservations(customer);
        // 如果这位顾客此前没有预约过，为她创建1个新的linked list, 用来储存本次的Reservation object
        if (customerReservations == null) {
            customerReservations = new LinkedList<>();
        }
        // 如果这位顾客此前已经预约过，就将本次Reservation object存进已经有的linked list里
        customerReservations.add(reservation);
        // 与此同时，需要在预约哈希表里添加本次预约的内容：key为顾客的邮箱，value为本次的Reservation object
        reservationMap.put(customer.getEmail(), customerReservations);
        // 最后返回本次的Reservation object
        return reservation;
    }

    // method: 查找该时间段内可以预约的房间 -> 传入的参数为入住和离开时间
    public static Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        // 通过调用getAllReservedRooms方法查找所有在这个时间段里已经被预约了的房间，并将这些room object存进reservedRooms集合
        // get all rooms reserved within the check-in and check-out dates
        Collection<IRoom> reservedRooms = getAllReservedRooms(checkInDate, checkOutDate);
        // get all available rooms (all rooms that are not reserved)
        // 新建1个链表叫availableRooms用于储存这个时间段里没有被预约的房间
        Collection<IRoom> availableRooms = new LinkedList<>();
        // 通过调用getAllRooms方法，获取并迭代所有的房间对象
        for (IRoom room : getAllRooms()) {
            // 如果迭代到的room object不存在于reservedRooms集合里，那么就将这个room object添加到availableRooms链表里
            if (!reservedRooms.contains(room)) {
                availableRooms.add(room);
            }
        }
        // 最终返回availableRooms链表
        return availableRooms;
    }

    // method: 通过顾客的邮箱，获取对方所有预约信息
    // 获得客户预约名单，通过传入Customer object, 获取顾客的邮箱
    // 然后通过邮箱，在预约哈希表里获取对应的reservation object(即通过顾客的邮箱，获知对方在什么时候预约了什么房间)
    public static Collection<Reservation> getCustomerReservations(Customer customer) {
        return reservationMap.get(customer.getEmail());
    }

    // method: 获取所有预约信息
    public static Collection<Reservation> getAllReservations() {
        // 首先创建一个新的链表叫allReservations, 用于储存预约的信息
        Collection<Reservation> allReservations = new LinkedList<>();
        // 迭代哈希表里的values, 即各个reservation object, 然后将它们添加到这个链表中
        for (Collection<Reservation> customerReservations : reservationMap.values()) {
            allReservations.addAll(customerReservations);
        }
        // 最后返回allReservations链表
        return allReservations;
    }

    // method: 获取所有的房间
    public static Collection<IRoom> getAllRooms() {
        // 通过获取房间哈希表的所有值，返回所有的room object
        return roomMap.values();
    }

    // method: 查找所有在这个时间段里已经被预约了的房间（即顾客无法再预约这些已满的房间）
    private static Collection<IRoom> getAllReservedRooms(Date checkInDate, Date checkOutDate) {
        // 首先创建一个新的链表叫reservedRooms, 用于储存这些已满的房间
        Collection<IRoom> reservedRooms = new LinkedList<>();
        // 调用getAllReservations方法，获取所有reservation object, 然后迭代它们
        for (Reservation reservation : getAllReservations()) {
            // 迭代各个reservation object, 对它们使用isRoomReserved方法
            if (reservation.isRoomReserved(checkInDate, checkOutDate)) {
                // 如果结果返回true, 通过getRoom方法获取对应的room object, 然后存进reservedRooms链表
                // 如果结果返回false, 什么都不用做
                reservedRooms.add(reservation.getRoom());
            }
        }
        // 最后返回reservedRooms链表
        return reservedRooms;
    }

    // method:判断1个房间在某个时间段里是否已经被预约
    private static boolean isRoomReserved(IRoom room, Date checkInDate, Date checkOutDate) {
        // get all rooms reserved within the check-in and check-out dates
        // 查找在某个时间段里已经被预约了的所有房间，并将它们存进集合reservedRooms里
        // 但不太理解这里两个方法互相调用的逻辑？
        Collection<IRoom> reservedRooms = getAllReservedRooms(checkInDate, checkOutDate);
        // 判断目前关注的room object是否存在于集合reservedRooms里，如果是，说明已经被预约；否则说明未预约
        if (reservedRooms.contains(room)) {
            return true;
        }
        return false;
    }

}
