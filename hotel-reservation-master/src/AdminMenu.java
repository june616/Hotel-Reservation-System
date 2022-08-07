import api.AdminResource;
import api.HotelResource;
import model.*;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

// User interface部分，创建main menu和admin menu, 以便使用者可以通过命令行与程序进行交互
// 菜单会在console展示出不同的选择，然后使用Scanner class读取使用者的反应
// 注意：main menu是给顾客用的，admin menu是给管理者用的

public class AdminMenu {

    // 打印为管理者准备的选择指令
    public static void displayOptions() {
        System.out.println("Admin Menu");
        System.out.println();
        System.out.println("==========================================================");
        System.out.println("1. See all customers");
        System.out.println("2. See all rooms");
        System.out.println("3. See all reservations");
        System.out.println("4. Add a room");
        System.out.println("5. Add test data");
        System.out.println("6. Back to main menu");
        System.out.println("==========================================================");
        System.out.println("Please select a number for the menu option");
    }

    // 使用scanner来读取用户输入的内容
    // 这部分的switch没有看太懂，感觉跟while循环差不多？
    public static boolean executeOption(Scanner scanner, Integer selection) {
        boolean keepAdminRunning = true;
        // 以下每个数字都对应了选项的任务
        switch (selection) {
            case 1:
                // case 1: 获取所有顾客信息
                getAllCustomers();
                break;
            case 2:
                // case 2: 获取所有房间信息
                getAllRooms();
                break;
            case 3:
                // case 3: 获取所有预约信息
                getAllReservations();
                break;
            case 4:
                // case 4: 添加房间
                addRooms(scanner);
                break;
            case 5:
                // case 5: 添加测试数据
                addTestData();
                break;
            case 6:
                // case 6: 结束程序，将keepRunning的状态设定为false
                keepAdminRunning = false;
                break;
            default:
                // 默认情况下会打印提示，要求用户输入数字
                System.out.println("Please enter a number between 1 and 6\n");
        }
        // 不懂为什么要返回它？
        return keepAdminRunning;
    }

    // 方法：获取所有顾客信息
    private static void getAllCustomers() {
        // 调用AdminResource里的getAllCustomers方法，获得所有Customer object并存进集合allCustomers
        Collection<Customer> allCustomers = AdminResource.getAllCustomers();
        // 如果集合allCustomers为空，打印相关信息
        if (allCustomers.isEmpty()) {
            System.out.println("There are no customers");
        } else {
            // 如果集合allCustomers不为空，则迭代每个Customer object并打印其字符串表达
            for (Customer customer : allCustomers) {
                System.out.println(customer.toString());
            }
        }
        System.out.println();
    }

    // 方法：添加房间整体操作
    private static void addRooms(Scanner scanner) {
        boolean keepAddingRooms;
        // 这是1个do while循环，意味着do{}里的内容会先执行1次，然后检查while{}里的条件是否为true, 如果为true, 那么do{}里的内容会持续执行
        // 无论while{}里的条件是否为true, do{}里的内容都会至少执行1次
        do {
            // 调用addRoom方法，添加1个新房间
            addRoom(scanner);
            // 打印信息，询问用户是否需要再次添加新房间
            System.out.println("Would you like to add another room? Enter y/yes, or any other character for no: ");
            // 阅读用户输入的内容，存为choice
            String choice = scanner.nextLine();
            // 如果用户输入的是y或yes(注意choice.equalsIgnoreCase表示不关注大小写)，说明用户想要再次添加新房间
            if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
                // 将keepAddingRooms设置为true
                keepAddingRooms = true;
            } else {
                // 如果用户输入的是其他内容，说明用户不想要再次添加新房间
                // 将keepAddingRooms设置为false
                keepAddingRooms = false; // for any other input, stop adding rooms
            }
            // 接下来检查keepAddingRooms是否为true, 如果为真，会一直执行do{}里的内容
        } while (keepAddingRooms);
    }

    // 方法：得知用户想要添加新房间后，要求用户输入房号、价格、类型，检查输入内容是否有效，创建新的room object并添加到房间哈希表
    private static void addRoom(Scanner scanner) {
        // get room number input
        String roomNumber = null;
        boolean validRoomNumber = false;
        // 只要validRoomNumber等于false, while循环会持续执行
        while (!validRoomNumber) {
            // 打印信息提示用户输入添加的新房间的房号
            System.out.println("Enter room number: ");
            // 阅读用户输入内容，存为roomNumber
            roomNumber = scanner.nextLine();
            // 调用HotelResource的getRoom方法，该方法通过房号获取对应的room object，并存进变量roomExists
            IRoom roomExists = HotelResource.getRoom(roomNumber);
            // 如果roomExists为空，说明当前没有这个房间，允许创建
            if (roomExists == null) { // room doesn't exists, continue
                // 将validRoomNumber改为true -> 说明当前输入的房号是valid的
                validRoomNumber = true;
            } else { // room exists, either continue and edit it's price and type, or enter a new room number
                // 如果roomExists不为空，说明当前已经存在这个房间，不允许创建
                // 打印提示信息，要求用户选择：1.更新该房间的信息；2.输入新的房号
                System.out.println("That room already exists. Enter y/yes to update it, or any other character to enter another room number: ");
                // 阅读用户输入内容，存为choice
                String choice = scanner.nextLine();
                // 如果用户输入y或yes, 说明用户想要更新该房间的信息，将validRoomNumber改为true -> 说明当前输入的房号是valid的
                if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
                    validRoomNumber = true;
                }
            }
        }
        // get valid price input
        double price = 0.00;
        boolean validPrice = false;
        // 只要validPrice等于false, while循环会持续执行
        while (!validPrice) {
            try {
                // 打印信息提示用户输入添加的新房间的价格
                System.out.println("Enter price per night: ");
                // 阅读用户输入内容，存为price
                price = Double.parseDouble(scanner.nextLine());
                // 如果price小于0，提示用户重新输入
                if (price < 0) {
                    System.out.println("The price must be greater or equal than 0.00");
                } else {
                    // 如果price大于0，将validPrice改为true -> 说明当前输入的价格是valid的
                    validPrice = true;
                }
            } catch (Exception ex) {
                // 如果在处理用户输入的内容时出现了问题，要求用户重新输入
                System.out.println("Please enter a valid price");
            }
        }
        // get valid room type input
        RoomType roomType = null;
        boolean validRoomType = false;
        // 只要validRoomType等于false, while循环会持续执行
        while (!validRoomType) {
            try {
                // 打印信息提示用户输入添加的新房间的类型
                System.out.println("Enter room type (1 for single bed, 2 for double bed): ");
                // 阅读用户输入内容，使用Integer.parseInt()转换成整数
                // 调用RoomType的valueforNumberOfBeds方法，通过传入用户输入的数字，返回对应的room type object(single/double)，存进roomType
                roomType = RoomType.valueforNumberOfBeds(Integer.parseInt(scanner.nextLine()));
                // 如果roomType为空，说明没有对应的room type object, 提示用户重新输入
                if (roomType == null) {
                    System.out.println("Please enter a valid room type");
                } else {
                    // 如果roomType不为空，将validRoomType改为true -> 说明当前输入的类型是valid的
                    validRoomType = true;
                }
            } catch (Exception ex) {
                // 如果在处理用户输入时出现了问题，要求用户重新输入
                System.out.println("Please enter a valid room type");
            }
        }
        // create and add the room
        // 在确认用户输入的房号、价格、类型都valid之后，可以使用它们来创建1个新的room object, 代表用户新添加的房间
        Room newRoom = new Room(roomNumber, price, roomType);
        // 调用AdminResource的addRoom方法，将这个新的room object添加到房间哈希表中
        AdminResource.addRoom(newRoom);
    }

    // 方法：获取所有房间信息
    private static void getAllRooms() {
        // 调用AdminResource的getAllRooms方法，获取所有的room object并存进集合allRooms
        Collection<IRoom> allRooms = AdminResource.getAllRooms();
        // 如果allRooms为空，说明当前没有房间，打印相关信息
        if (allRooms.isEmpty()) {
            System.out.println("There are no rooms");
        } else {
            // 如果allRooms不为空，说明当前已经有房间，需要迭代allRooms, 打印每个room object的字符串表达
            for (IRoom room : allRooms) {
                System.out.println(room.toString());
            }
        }
        System.out.println();
    }

    // 方法：获取所有预约信息
    private static void getAllReservations() {
        // 调用AdminResource的getAllReservations方法，获取所有的reservation object并存进集合allReservations
        Collection<Reservation> allReservations = AdminResource.getAllReservations();
        // 如果allReservations为空，说明当前没有预约，打印相关信息
        if (allReservations.isEmpty()) {
            System.out.println("There are no reservations");
        } else {
            // 如果allReservations不为空，说明当前已经有预约，需要迭代allReservations, 打印每个reservation object的字符串表达
            for (Reservation reservation : allReservations) {
                System.out.println(reservation.toString());
            }
        }
        System.out.println();
    }

    // 方法：添加测试数据
    public static void addTestData() {
        // add some rooms
        String roomNumber = null;
        Double price = 0.00;
        RoomType roomType = null;
        // 从i = 1开始，递增i = 1->2->3 直到i>3为止
        for (int i = 1; i <= 3; i++) {
            // 房号等于1->2->3;Integer.toString()将整数变成字符串
            roomNumber = Integer.toString(i);
            // 如果房号为偶数，那么价格为200，房间类型为double
            if (i % 2 == 0) {
                price = 200.00;
                // 调用RoomType的valueforNumberOfBeds方法，即2对应的room type为double
                roomType = RoomType.valueforNumberOfBeds(2);
            } else {
                // 如果房号为奇数，那么价格为100，房间类型为single
                price = 100.00;
                // 调用RoomType的valueforNumberOfBeds方法，即1对应的room type为single
                roomType = RoomType.valueforNumberOfBeds(1);
            }
            // 根据对应的房号、价格、类型，创建3个room object(在for循环里每次执行1个)
            Room newRoom = new Room(roomNumber, price, roomType);
            // 调用AdminResource的addRoom方法，将这3个room object添加进房间哈希表
            AdminResource.addRoom(newRoom);
        }
        // add some customer accounts
        // 调用HotelResource的createCustomer方法，创建customer object, 并将这个object存进customerMap哈希表中
        HotelResource.createCustomer("test1@mail.com", "Peter", "Parker");
        HotelResource.createCustomer("test2@mail.com", "Clark", "Kent");
        HotelResource.createCustomer("test3@mail.com", "Tony", "Stark");
        HotelResource.createCustomer("test4@mail.com", "Bruce", "Wayne");
        HotelResource.createCustomer("test5@mail.com", "Steve", "Rogers");
        // book some rooms
        // 使用new Date()是为了获得目前的时间，并存为today
        Date today = new Date();
        // 创建calendar实例
        Calendar c = Calendar.getInstance();
        Date checkInDate = null;
        Date checkOutDate = null;
        // reservation 1
        // 将当前日期转换为calendar time(convert date to calendar)
        c.setTime(today);
        // 修改日历时间，让日期往后加2天
        c.add(Calendar.DATE, 2);
        // 将calendar time转换成日期格式(convert calendar to date)，并存为checkInDate -> 即测试的入住日期为今日的2天之后
        checkInDate = c.getTime();
        // 将测试的入住日期转换为calendar time(convert date to calendar)
        c.setTime(checkInDate);
        // 修改日历时间，让日期往后加5天
        c.add(Calendar.DATE, 5);
        // 将calendar time转换成日期格式(convert calendar to date)，并存为checkOutDate -> 即测试的离开日期为入住日期的5天之后
        checkOutDate = c.getTime();
        // 调用HotelResource的bookRoom方法，通过传入要预约的顾客邮箱，要预约的房间，入住和离开时间，完成本次房间预约
        // 注意此处还调用了HotelResource的getRoom方法，通过房号获取对应的room object
        HotelResource.bookRoom("test1@mail.com", HotelResource.getRoom("1"), checkInDate, checkOutDate);
        // reservation 2
        // 将当前日期转换为calendar time(convert date to calendar)
        c.setTime(today);
        // 修改日历时间，让日期往后加4天
        c.add(Calendar.DATE, 4);
        // 将calendar time转换成日期格式(convert calendar to date)，并存为checkInDate -> 即测试的入住日期为今日的4天之后
        checkInDate = c.getTime();
        // 将测试的入住日期转换为calendar time(convert date to calendar)
        c.setTime(checkInDate);
        // 修改日历时间，让日期往后加10天
        c.add(Calendar.DATE, 10);
        // 将calendar time转换成日期格式(convert calendar to date)，并存为checkOutDate -> 即测试的离开日期为入住日期的10天之后
        checkOutDate = c.getTime();
        // 调用HotelResource的bookRoom方法，通过传入要预约的顾客邮箱，要预约的房间，入住和离开时间，完成本次房间预约
        // 注意此处还调用了HotelResource的getRoom方法，通过房号获取对应的room object
        HotelResource.bookRoom("test3@mail.com", HotelResource.getRoom("2"), checkInDate, checkOutDate);
        // reservation 3
        // 将当前日期转换为calendar time(convert date to calendar)
        c.setTime(today);
        // 修改日历时间，让日期往后加5天
        c.add(Calendar.DATE, 5);
        // 将calendar time转换成日期格式(convert calendar to date)，并存为checkInDate -> 即测试的入住日期为今日的5天之后
        checkInDate = c.getTime();
        // 将测试的入住日期转换为calendar time(convert date to calendar)
        c.setTime(checkInDate);
        // 修改日历时间，让日期往后加3天
        c.add(Calendar.DATE, 3);
        // 将calendar time转换成日期格式(convert calendar to date)，并存为checkOutDate -> 即测试的离开日期为入住日期的3天之后
        checkOutDate = c.getTime();
        // 调用HotelResource的bookRoom方法，通过传入要预约的顾客邮箱，要预约的房间，入住和离开时间，完成本次房间预约
        // 注意此处还调用了HotelResource的getRoom方法，通过房号获取对应的room object
        HotelResource.bookRoom("test4@mail.com", HotelResource.getRoom("3"), checkInDate, checkOutDate);

        System.out.println("Test data added!");
    }

}
