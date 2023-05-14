package hello.upload.domain;

import java.util.List;
import lombok.Data;

//Item 정보가 담긴 객체
@Data
public class Item {

    private Long id;
    private String itemName;
    private UploadFile attachFile; //첨부파일
    private List<UploadFile> imageFiles; //화면에 출력할 이미지 파일들
}
