package Userinfo;

import java.util.Scanner;

public class userread {
    private String id;
    private String password;
    private String name;
    private String birthdate;
    int score;

    // 데이터 읽기 메서드
    public void read(Scanner scanner) {
        if (scanner.hasNext()) {
            id = scanner.next();
        }
        if (scanner.hasNext()) {
            password = scanner.next();
        }
        if (scanner.hasNext()) {
            name = scanner.next();
        }
        if (scanner.hasNext()) {
            birthdate = scanner.next();
        }
        if (scanner.hasNextInt()) {
            setScore(scanner.nextInt());
        }
    }

    // Getter 메서드들
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public int getScore() {
        return score;
    }

	public void setScore(int score) {
		this.score = score;
	}
}