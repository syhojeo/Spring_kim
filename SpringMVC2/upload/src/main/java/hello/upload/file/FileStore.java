package hello.upload.file;

import hello.upload.domain.UploadFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    //다중 파일 저장
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    //단일 파일저장
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }
        //첨부된 파일의 파일명 얻기
        String originalFilename = multipartFile.getOriginalFilename();
        //첨부된 파일의 서버에서 사용할 파일명 만들기
        String storeFileName = createStoreFileName(originalFilename);
        //FullPath 경로에 파일 저장
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        //첨부시의 파일명과 서버에 저장하는 파일명 저장
        return new UploadFile(originalFilename, storeFileName);
    }

    /*
        image 파일명 정하기
        image.png 파일명으로 업로드 했다면
        앞의 이름은 UUID로 랜덤생성하고 뒤의 확장자만 빼온다
     */
    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);
        return ext;
    }
}
