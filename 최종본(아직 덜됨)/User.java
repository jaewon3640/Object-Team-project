package united;

public class User {
    private String id;
    private String password;
    private String name;
    private String birthdate;
    private int chipNum=0;
    private int score = 0;

    public User(String id, String password, String name, String birthdate) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.birthdate = birthdate;
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

    public int getChipNum() {
        return chipNum;
    }

    public void setChipNum(int chipNum) {
        this.chipNum = chipNum;
    }

    public int getScore() { // getScore 메서드 추가
        return score;
    }

    public void setScore(int score) { // setScore 메서드 추가
        this.score = score;
    }

    // Setter 메서드들
    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }
}