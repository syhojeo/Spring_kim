package hello.upload.controller;

import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemForm {

    private Long itemId;
    private String itemName;
    //MultipartFile 객체는 @ModelAttribute 를 통해 ArgumentResolver가 알아서 받아온다
    private MultipartFile attachFile;
    //다중 파일 업로드를 위해 ItemForm의 imagefile을 MultipartFile의 리스트 형식으로 선언
    private List<MultipartFile> imageFiles;
}
