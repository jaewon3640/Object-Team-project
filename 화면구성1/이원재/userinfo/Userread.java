package userinfo;

import java.util.ArrayList;
import java.util.Scanner;

public class Userread {
    String id;
    String password;
    String name;
    String birthdate;
    int score;

    // 데이터를 읽어오는 메서드
    void read(Scanner scan) {
        id = scan.next();
        password = scan.next();
        name = scan.next();
        birthdate = scan.next();
    }

    // 사용자 정보를 출력하는 메서드
    void print() {
        System.out.printf("ID: %s, Password: %s, Name: %s, Birthdate: %s\n",
                id, password, name, birthdate);
    }

    // Getter methods
    public String getId() { return id; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getBirthdate() { return birthdate; }
    public int getScore() { return score; }
}
