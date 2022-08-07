package model;

public interface IRoom {
    // interface又叫接口，它是一个抽象类型，是抽象方法的集合（不能被实例化）
    // 它可以被class通过implements关键词实现继承
    public String getRoomNumber();
    public Double getRoomPrice();
    public RoomType getRoomType();
    public Boolean isFree();

}
