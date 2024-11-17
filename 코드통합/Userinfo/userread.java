package Userinfo;

import java.util.ArrayList;
import java.util.Scanner;

public class userread {
    String id;
    String password;
    String name;
    String birthdate;
    int score;

    // 데이터를 읽어오는 메서드
    void read(Scanner scan) {
        // 데이터가 있으면 읽어오고 없으면 리턴
        if (scan.hasNext()) {
            id = scan.next();
        } else {
            return;
        }

        if (scan.hasNext()) {
            password = scan.next();
        } else {
            return;
        }

        if (scan.hasNext()) {
            name = scan.next();
        } else {
            return;
        }

        if (scan.hasNext()) {
            birthdate = scan.next();
        } else {
            return;
        }

        // score 필드도 읽어옴, 없으면 기본값 0으로 설정
        if (scan.hasNextInt()) {
            score = scan.nextInt();
        } else {
            score = 0; // score 값이 없다면 기본값 0
        }
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
