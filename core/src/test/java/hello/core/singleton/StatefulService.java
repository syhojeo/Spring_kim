package hello.core.singleton;

public class StatefulService {

    //싱글톤 문제를 해결하기 위해 공유 필드 없애기

    //private int price; //상태를 유지하는 필드

    public int order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        return price;
    }

//    public int getPrice() {
//        return price;
//    }
}
