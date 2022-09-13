package hello.core.member;

public class Member {

    private Long id;
    private String name;

    public Member(Long id, String name, Grade gradle) {
        this.id = id;
        this.name = name;
        this.gradle = gradle;
    }

    private Grade gradle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Grade getGradle() {
        return gradle;
    }

    public void setGradle(Grade gradle) {
        this.gradle = gradle;
    }
}
