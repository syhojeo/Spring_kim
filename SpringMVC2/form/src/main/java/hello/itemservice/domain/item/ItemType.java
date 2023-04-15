package hello.itemservice.domain.item;

public enum ItemType {

    BOOK("도서"), FOOD("음식"), ETC("기타");

    private final String description;

    //enum 안의 스트링을 description으로
    private ItemType(String description) {
        this.description = description;
    }

    //타임리프의 프로퍼티 접근법을 위해 getter 추가
    public String getDescription() {
        return description;
    }
}
