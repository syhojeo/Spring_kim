package hello.core;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//lombok 사용

//getter, setter 따로 만들지 않아도 어노테이션만으로 사용가능
@Getter
@Setter
@ToString
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("asdf");

        String name = helloLombok.getName();
        System.out.println("name = " + name);
        //toString
        System.out.println("helloLombok = " + helloLombok);
    }
}
