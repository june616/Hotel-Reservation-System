package model;

import java.util.Objects;

// Room class实现了IRoom，而该接口包含getRoomNumber, getRoomPrice, getRoomType, isFree 4个抽象方法，需要在本class里override
public class Room  implements IRoom {

    protected String roomNumber;
    protected Double price;
    protected RoomType enumeration;

    public Room(String roomNumber, Double price, RoomType enumeration) {
        // Room class的三个属性：房间号，价格，房间类型（单人或双人床）
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }

    // 改写4个抽象方法
    @Override
    public String getRoomNumber() {
        return this.roomNumber;
    }

    @Override
    public Double getRoomPrice() {
        return this.price;
    }

    @Override
    public RoomType getRoomType() {
        return this.enumeration;
    }

    @Override
    public Boolean isFree() {
        // 判断价格是否等于0
        if (this.price == (double) 0) {
            return true;
        }
        return false;
    }

    // 此方法返回a string representation of an object
    // 返回房间号，单双人房，房间价格
    public String toString() {
        return "Room number: " + this.roomNumber + " " + this.enumeration + " bed room Price: $" + this.price;
    }

    // 这部分没看懂
    @Override
    public boolean equals(Object o) {
        // 判断当前object o是否与this是相同的object(refer to same memory location)？（this又是什么？）
        if (this == o) return true;
        // 判断当前object o是否为Room的instance(是否归属于Room这个类)，如果不是，返回false
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        // 如果当前object既不是与this相同的object，但又属于Room的object,就比较房间号、单双人房、房间价格3个属性是否都相等
        return roomNumber.equals(room.roomNumber) && price.equals(room.price) && enumeration == room.enumeration;
    }

    // hashCode返回integer hashed value of the given object
    // if two objects are same(refer to same memory location), their hash codes are same
    @Override
    public int hashCode() {
        return Objects.hash(roomNumber, price, enumeration);
    }
}
