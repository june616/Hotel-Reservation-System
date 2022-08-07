package model;

import java.util.HashMap;
import java.util.Map;

// create the room type enumeration
public enum RoomType {
    SINGLE (1),
    DOUBLE (2);

    // 以下这部分看不太懂，numberOfBeds是记录1个房间有多少张床（1或2）
    private int numberOfBeds;
    // 应该是设置了1个hash map（类似字典）叫做BY_NUMBER_OF_BEDS
    // 然后将以房间里的床数为key（即1或2）, 房间类型RoomType为value（即single或double）
    // BY_NUMBER_OF_BEDS可能是：{1:"single", 2:"double"}
    private static final Map<Integer, RoomType> BY_NUMBER_OF_BEDS = new HashMap<Integer, RoomType>();

    static {
        // 迭代room type这个object里的值
        for (RoomType roomType : values()) {
            // 这部分看不太懂
            // 往BY_NUMBER_OF_BEDS里添加pairs元素：key为roomType.numberOfBeds，value为roomType
            // roomType.numberOfBeds表示它是roomType class的1个属性
            // 添加的内容可能是:{1:roomType object, 2:roomType object}
            BY_NUMBER_OF_BEDS.put(roomType.numberOfBeds, roomType);
        }
    }
    // 这部分看不太懂
    // 赋值：将numberOfBeds赋值给RoomType，使它成为RoomType的1个属性
    RoomType(int numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }


    // valueforNumberOfBeds是RoomType这个class的method，它会返回BY_NUMBER_OF_BEDS这个字典里的值，即返回对应的roomType object
    public static RoomType valueforNumberOfBeds(int numberOfBeds) {
        return BY_NUMBER_OF_BEDS.get(numberOfBeds);
    }

}
