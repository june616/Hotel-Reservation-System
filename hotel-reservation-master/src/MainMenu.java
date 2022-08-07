import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

// User interface部分，创建main menu和admin menu, 以便使用者可以通过命令行与程序进行交互
// 菜单会在console展示出不同的选择，然后使用Scanner class读取使用者的反应
// 注意：main menu是给顾客用的，admin menu是给管理者用的

public class MainMenu {

    // 打印为顾客准备的选择指令
    public static void displayOptions() {
        System.out.println("Welcome to the Hotel Reservation Application");
        System.out.println();
        System.out.println("==========================================================");
        System.out.println("1. Find and reserve a room");
        System.out.println("2. See my reservations");
        System.out.println("3. Create an Account");
        System.out.println("4. Admin");
        System.out.println("5. Exit");
        System.out.println("==========================================================");
        System.out.println("Please select a number for the menu option");
    }

    // 使用scanner来读取用户输入的内容
    // 这部分的switch没有看太懂，感觉跟while循环差不多？
    public static boolean executeOption(Scanner scanner, Integer selection) {
        boolean keepRunning = true;
        // 以下每个数字都对应了选项的任务
        switch (selection) {
            case 1:
                // case 1: 查找并预约房间
                findAndReserveRoom(scanner);
                break;
            case 2:
                // case 2: 获取该顾客的所有预约信息
                getCustomerReservations(scanner);
                break;
            case 3:
                // case 3: 为该顾客创建账户
                createAccount(scanner);
                break;
            case 4:
                // case 4: 跳转到admin菜单
                runAdminMenu(scanner);
                break;
            case 5:
                // case 5: 结束程序，将keepRunning的状态设定为false
                keepRunning = false;
                break;
            default:
                // 默认情况下会打印提示，要求用户输入数字
                System.out.println("Please enter a number between 1 and 5\n");
        }
        // 不懂为什么要返回它？
        return keepRunning;
    }

    // 方法：展示admin菜单
    private static void runAdminMenu(Scanner scanner) {
        // 此时keepAdminRunning为true
        boolean keepAdminRunning = true;
        while (keepAdminRunning) {
            // admin menu正常运行的情况：
            try {
                // 展示admin菜单选项
                AdminMenu.displayOptions();
                // scanner.nextLine()表示阅读console里用户输入的整行字符串
                // parseInt表示将阅读到的字符串转化成整数，之后再存进adminSelection变量里
                int adminSelection = Integer.parseInt(scanner.nextLine());
                // 调用AdminMenu里的executeOption方法来判断keepAdminRunning是否为true，即是否要继续运行admin menu
                keepAdminRunning = AdminMenu.executeOption(scanner, adminSelection);
            } catch (Exception ex) {
                // 如果出现了运行错误：可能是用户输入的内容不是数字，无法转换成整数，或数字超过了1-6，因此要打印提示，要求用户输入1-6整数
                System.out.println("Please enter a number between 1 and 6\n");
            }
        }
    }

    // 方法：创建账号
    private static String createAccount(Scanner scanner) {
        // 提示用户输入
        System.out.println("First name: ");
        // 阅读用户输入的内容，存为firstName
        String firstName = scanner.nextLine();
        // 提示用户输入
        System.out.println("Last name: ");
        // 阅读用户输入的内容，存为lastName
        String lastName = scanner.nextLine();
        String email = null;
        boolean validEmail = false;
        // 只要validEmail始终为false, 就持续提示用户输入邮箱地址，直到用户输入正确格式的邮箱为止
        while (!validEmail) {
            try {
                // 提示用户输入
                System.out.println("Email (format: name@example.com): ");
                // 阅读用户输入的内容，存为email
                email = scanner.nextLine();
                // 调用HotelResource的createCustomer方法；而HotelResource作为前后端中介，又会调用CustomerService里的addCustomer方法
                // 它会创建新的customer object并存进顾客哈希表中
                HotelResource.createCustomer(email, firstName, lastName);
                // 打印创建成功的信息
                System.out.println("Account created successfully!\n");
                // 说明邮箱地址有效，改为true, 不会再执行此while循环
                validEmail = true;
            } catch (IllegalArgumentException ex) {
                // 如果因为邮箱地址无效而创建失败，打印相应的信息
                System.out.println(ex.getLocalizedMessage());
            }
        }
        // while循环结束后，返回对应的邮箱地址
        return email;
    }

    // 方法：获取顾客所有预约信息
    private static void getCustomerReservations(Scanner scanner) {
        // 提示用户输入邮箱
        System.out.println("Please enter your Email (format: name@example.com): ");
        // 阅读用户输入的内容，存为email
        String email = scanner.nextLine();
        // 调用HotelResource的getCustomer方法；而HotelResource作为前后端中介，又会调用CustomerService里的getCustomer方法
        // 它会通过顾客邮箱，获取对应的Customer object
        Customer customer = HotelResource.getCustomer(email);
        // 如果获取的Customer object为空，说明没有存该顾客的数据
        if (customer == null) {
            System.out.println("Sorry, no account exists for that email");
            return;
        }
        // 如果获取的Customer object不为空，调用HotelResource的getCustomerReservations方法
        // 具体内容：通过顾客的邮箱，获取对方所有预约信息（在预约哈希表里获取该顾客对应的reservation object并返回）
        // 最后将对应的reservation object存进集合reservations
        Collection<Reservation> reservations = HotelResource.getCustomerReservations(customer.getEmail());
        // 如果reservations为空，说明该顾客没有预约信息
        if (reservations.isEmpty()) {
            System.out.println("You don't have any reservations at the moment");
            return;
        }
        // 如果reservations不为空，迭代object并打印其字符串表达
        // reservation object的字符串表达：预约的顾客，房间，入住时间，离开时间
        for (Reservation reservation : reservations) {
            System.out.println(reservation.toString());
        }
    }

    // 方法：查找并预约房间
    private static void findAndReserveRoom(Scanner scanner) {
        // get valid check-in date input
        // 调用getValidCheckInDate方法，将用户输入的入住日期解析成可理解的日期格式，并检查日期是否有效
        Date checkInDate = getValidCheckInDate(scanner);
        // get valid check-out date input
        // 调用getValidCheckOutDate方法，将用户输入的离开日期解析成可理解的日期格式，并检查日期是否有效
        Date checkOutDate = getValidCheckOutDate(scanner, checkInDate);
        // display available rooms (if any) and ask if wants to book
        // 调用HotelResource的findRooms方法，查找该时间段内可以预约的room object，存进集合availableRooms
        // 由于HotelResource作为前后端中介，又会调用ReservationService里的findRooms方法，最终返回该时间段里可预约的所有room object
        Collection<IRoom> availableRooms = HotelResource.findRooms(checkInDate, checkOutDate);
        boolean wantsToBook = false;
        // 如果availableRooms为空，说明该时间段里没有房间可预约
        if (availableRooms.isEmpty()) {
            // recommend other dates: expand check-in/out dates by 7 days and check for available rooms again
            // 调用方法getRecommendedDate，将入住和离开日期都往后延7天
            Date newCheckInDate = getRecommendedDate(checkInDate);
            Date newCheckOutDate = getRecommendedDate(checkOutDate);
            // 再次调用HotelResource的findRooms方法，查找该新时间段内可以预约的room object，存进集合availableRooms
            availableRooms = HotelResource.findRooms(newCheckInDate, newCheckOutDate);
            // 如果新时间段里有可预约的房间
            if (!availableRooms.isEmpty()) {
                // 打印新时间段内可预约的信息
                System.out.println("There are no available rooms for those dates. Rooms available for alternative dates, check-in on " + newCheckInDate + " and check-out on " + newCheckOutDate);
                // 调用showAvailableRoomsAndAskToBook, 获取用户是否想预定房间的答案; true表示想预定
                wantsToBook = showAvailableRoomsAndAskToBook(scanner, availableRooms);
                // 如果用户想要预定这个新时段里的房间，需要将新的入住和离开日期用checkInDate和checkOutDate变量存起
                checkInDate = newCheckInDate;
                checkOutDate = newCheckOutDate;
            } else {
                // 如果新时间段里没有可预约的房间，打印相关信息
                System.out.println("There are no available rooms for those dates");
            }
        } else {
            // 如果availableRooms不为空，说明该时间段里有房间可预约，需要打印可预约房间的信息
            System.out.println("Available rooms for check-in on " + checkInDate + " and check-out on " + checkOutDate);
            // 调用showAvailableRoomsAndAskToBook, 获取用户是否想预定房间的答案; true表示想预定
            wantsToBook = showAvailableRoomsAndAskToBook(scanner, availableRooms);
        }
        // 如果用户不想要在她输入的时间段里预定房间，结束此方法
        if (!wantsToBook) {
            return;
        }
        // 如果用户想要在她输入的时间段里预定房间
        // ask if user has an account, if yes, ask for their email, else create a new account
        // 调用getCustomerForReservation方法，获取正在办理预约的顾客的customer object, 并存为customer
        Customer customer = getCustomerForReservation(scanner);
        // 如果该customer object为空，说明顾客的账户不存在
        if (customer == null) {
            System.out.println("Sorry, no account exists for that email");
            return;
        }
        // 如果该customer object不为空，说明顾客的账户存在
        // ask what room would they like to reserve, ask for room number, validate it's available
        // 调用getRoomForReservation方法，将顾客当前预定的有效的room object保存为room
        IRoom room = getRoomForReservation(scanner, availableRooms);
        // finally, book room and show reservation details
        // 调用HotelResource的bookRoom方法，传入要预约的顾客邮箱，要预约的room object，入住和离开时间
        // 返回的是本次预约的reservation object, 保存为reservation
        Reservation reservation = HotelResource.bookRoom(customer.getEmail(), room, checkInDate, checkOutDate);
        // 如果reservation object为空，说明本次预约失败（但前面已经有很多validation的步骤，这种情况应该不会发生）
        if (reservation == null) {
            System.out.println("Couldn't process your booking, the room is not available"); // this shouldn't happen as we validated that the room is available previously
        } else {
            // 如果reservation object不为空，说明本次预约成功，打印本次预约的相关信息
            System.out.println("Thank you! Your room was booked successfully!");
            System.out.println(reservation);
        }
    }

    // 方法：将用户输入的入住日期解析成可理解的日期格式，并检查日期是否有效
    private static Date getValidCheckInDate(Scanner scanner) {
        // 创建1个日期格式名为DateFor
        SimpleDateFormat DateFor = new SimpleDateFormat("MM/dd/yyyy");
        Date checkInDate = null;
        boolean validCheckInDate = false;
        // 只要validCheckInDate等于false, while循环会持续执行
        while (!validCheckInDate) {
            // 提示用户输入日期
            System.out.println("Check-in date (mm/dd/yyyy): ");
            // 阅读用户输入的内容，存为inputCheckInDate
            String inputCheckInDate = scanner.nextLine();
            try {
                // 试图将用户输入的日期解析为DateFor格式，并存为checkInDate
                checkInDate = DateFor.parse(inputCheckInDate);
                // 使用new Date()是为了获得目前的时间，并存为today
                Date today = new Date();
                // 检查用户输入的日期是否早于今日，如果是，需要打印出日期已经过期的信息
                if (checkInDate.before(today)) { // check-in date can't be in the past
                    System.out.println("The check-in date cannot be in the past");
                } else {
                    // 如果用户输入的日期晚于今日，说明该日期是有效的，将validCheckInDate改为true, while循环结束
                    validCheckInDate = true;
                }
            } catch (ParseException ex) {
                // 如果在将用户输入的日期解析为DateFor格式的过程中出现了错误，打印格式错误的信息
                System.out.println("Invalid date format, please use dd/mm/yyyy");
            }
        }
        // 最后结果返回解析为DateFor格式的用户的入住日期
        return checkInDate;
    }

    // 方法：将用户输入的离开日期解析成可理解的日期格式，并检查日期是否有效
    private static Date getValidCheckOutDate(Scanner scanner, Date checkInDate) {
        // 创建1个日期格式名为DateFor
        SimpleDateFormat DateFor = new SimpleDateFormat("MM/dd/yyyy");
        Date checkOutDate = null;
        boolean validCheckOutDate = false;
        // 只要validCheckInDate等于false, while循环会持续执行
        while (!validCheckOutDate) {
            // 提示用户输入日期
            System.out.println("Check-out date (mm/dd/yyyy): ");
            // 阅读用户输入的内容，存为inputCheckOutDate
            String inputCheckOutDate = scanner.nextLine();
            try {
                // 试图将用户输入的日期解析为DateFor格式，并存为checkOutDate
                checkOutDate = DateFor.parse(inputCheckOutDate);
                // 检查用户输入的checkOutDate是否早于checkInDate，如果是，需要打印日期不合理的信息
                if (checkOutDate.before(checkInDate)) { // check-out date can't be before the check-in date
                    System.out.println("The check-out date can't be before the check-in date");
                } else {
                    // 如果不是，说明该日期是有效的，将validCheckOutDate改为true, while循环结束
                    validCheckOutDate = true;
                }
            } catch (ParseException ex) {
                // 如果在将用户输入的日期解析为DateFor格式的过程中出现了错误，打印格式错误的信息
                System.out.println("Invalid date format, please use dd/mm/yyyy");
            }
        }
        return checkOutDate;
    }

    // 方法：在现有日期无房间可预约的情况下，将时间段往后延7天，得出新的日期（传入参数为原日期）
    private static Date getRecommendedDate(Date date) {
        // 创建calendar实例
        Calendar c = Calendar.getInstance();
        // 将参数原日期转换为calendar time(convert date to calendar)
        c.setTime(date);
        // 修改日历时间，让日期往后加7天
        c.add(Calendar.DATE, 7);
        // 将calendar time转换成日期格式(convert calendar to date)
        // 最终返回日期格式的新日期
        return c.getTime();
    }

    // 方法：打印当前时间段可预约的所有room信息，询问顾客是否想预定，如获得y/yes答案返回true, 否则返回false
    private static boolean showAvailableRoomsAndAskToBook(Scanner scanner, Collection<IRoom> availableRooms) {
        // 迭代所有当前时间段可预约的room object并打印它们的字符串表达
        for (IRoom room : availableRooms) {
            System.out.println(room.toString());
        }
        System.out.println();
        // 询问顾客是否想预定
        System.out.println("Would you like to book a room? Enter y/yes, or any other character for no:");
        // 阅读顾客打印在console的信息
        String choice = scanner.nextLine();
        // 如果顾客输入的是y或yes(注意choice.equalsIgnoreCase表示不关注大小写)，返回true
        if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
            return true;
        }
        // 如果顾客输入的是其他内容，返回false
        return false;
    }

    // 方法：获取正在办理预约的顾客的customer object
    private static Customer getCustomerForReservation(Scanner scanner) {
        String email = null;
        boolean hasAccount = false;
        // 询问用户是否已经创建账号
        System.out.println("Do you already have an account with us? Enter y/yes, or any other character for no:");
        // 阅读用户输入内容，存为choice
        String choice = scanner.nextLine();
        // 如果用户输入的是y或yes(注意choice.equalsIgnoreCase表示不关注大小写)，说明用户说自己已经创建账号
        if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
            hasAccount = true;
        }
        if (hasAccount) {
            // 如果用户说自己已经创建账号，打印信息提示用户输入邮箱
            System.out.println("Please enter your Email (format: name@example.com): ");
            // 阅读用户输入内容，存为email
            email = scanner.nextLine();
        } else {
            // 如果用户说自己没有创建账号，调用方法createAccount引导用户创建账户
            email = createAccount(scanner);
        }
        // 最终调用HotelResource的getCustomer方法，通过顾客邮箱，获取对应customer object并返回
        return HotelResource.getCustomer(email);
    }

    // 方法：询问顾客想要预定的房间号，检查该房间是否valid, 如果有效则办理预定，最终返回顾客当前预定的room object
    private static IRoom getRoomForReservation(Scanner scanner, Collection<IRoom> availableRooms) {
        IRoom room = null;
        String roomNumber = null;
        boolean validRoomNumber = false;
        // 只要validRoomNumber为false, while循环会一直执行
        while (!validRoomNumber) {
            // 询问顾客想要预定的房间号
            System.out.println("What room would you like to reserve? Enter the room number: ");
            // 阅读顾客输入答案，存进roomNumber
            roomNumber = scanner.nextLine();
            // 调用HotelResource的getRoom方法，通过房号获取对应的room object
            room = HotelResource.getRoom(roomNumber);
            // 如果该room object为空，说明该房间不存在，打印信息要求顾客再次输入
            if (room == null) { // room doesn't exists, ask again
                System.out.println("That room doesn't exists, please enter a valid room number");
            } else { // room exists, validate it's available（如果room object不为空，说明该房间存在）
                // 调用availableRooms.contains()方法，如果该房间位于availableRooms里，说明该房间在该时间段里是可预定的
                // 否则，说明该房间在该时间段里不可预定，打印信息要求顾客再次输入
                if (!availableRooms.contains(room)) { // room not available, ask again
                    System.out.println("That room is not available, please enter a valid room number");
                } else {
                    // 如果该房间在该时间段里是可预定的，将validRoomNumber改为true, 结束while循环
                    validRoomNumber = true;
                }
            }
        }
        // 最终返回顾客当前预定的room object
        return room;
    }

}
