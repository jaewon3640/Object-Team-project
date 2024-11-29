package united;

import java.util.Scanner;

public class userread {
    private String id;
    private String password;
    private String name;
    private String birthdate;
    private int score;

    // 파일에서 데이터를 읽어오는 메서드
    public void read(Scanner scanner) {
        String line = scanner.nextLine();
        String[] parts = line.split(" "); // 공백 기준으로 분리
        if (parts.length == 5) {
            this.id = parts[0];
            this.password = parts[1];
            this.name = parts[2];
            this.birthdate = parts[3];
            this.score = Integer.parseInt(parts[4]); // 점수는 숫자로 변환
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

    // Setter 메서드
    public void setScore(int score) {
        this.score = score;
    }
}