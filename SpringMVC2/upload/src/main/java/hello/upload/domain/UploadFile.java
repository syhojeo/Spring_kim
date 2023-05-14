package hello.upload.domain;

import lombok.Data;

//UploadFile
@Data
public class UploadFile {

    /*
        별도의 파일 명이 필요한 이유
        고객이 올리는 파일의 이름이 같은 경우 이름이 충돌 날 수 있기 때문에
        서버 내부에서 따로 중복되지 않는 파일명으로 저장해서 관리해야 문제가 발생하지 않는다
     */
    private String uploadFileName; //고객이 업로드한 파일명
    private String storeFileName; //서버내부에서 관리하는 파일명

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
