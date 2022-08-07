package model;

// FreeRoom class继承自Room class, 它拥有与Room相同的属性和方法
// 属性：房间号，单双人房，房间价格；方法：4个
public class FreeRoom extends Room {
    // 注意此处的constructor被修改，创建FreeRoom类或对象时只需要2个参数；
    // super(roomNumber, (double) 0, enumeration)这部分是constructor for Room class, 此时价格等于0
    public FreeRoom(String roomNumber, RoomType enumeration) {
        super(roomNumber, (double) 0, enumeration);
    }

    // 改写string representation of object, 其中价格必然为0
    public String toString() {
        return "Room number: " + this.roomNumber + " " + this.enumeration + " bed room Price: $0 (Free)";
    }

}
