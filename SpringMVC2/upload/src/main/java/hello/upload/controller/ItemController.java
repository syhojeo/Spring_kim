package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    //등록 폼을 보여준다
    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    //폼의 데이터를 저장하고 보여주는 화면으로 리다이렉트
    // redirectAttributes
    // PRG(Post Redirect Get) 패턴 사용시 리다이렉트에 Model을 통해 데이터를 넘기기 위한 클래스
    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes)
        throws IOException {

        //단일 첨부 파일 저장소에 저장
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());

        //다중 첨부 파일 저장소에 저장
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        //데이터베이스에 저장
        Item item = new Item();
        item.setItemName(form.getItemName());
        //데이터베이스엔 파일 이름만 저장 실제 이미지는 저장소에 저장
        item.setAttachFile(attachFile); //UploadFile 저장
        item.setImageFiles(storeImageFiles); //List<UploadFile> 저장
        itemRepository.save(item);

        //PRG 패턴 구현시 필요한 Model 정보 저장
        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}";
    }

    //위의 PRG 패턴의 종착지, 상품을 보여준다
    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        Item item = itemRepository.findById(id);
        model.addAttribute("item", item);
        return "item-view";
    }

    /*
        밑의 두가지 매핑은 모두 서버에 있는 데이터 (이미지, txt등)을 다운로드하는 컨트롤러 로직이며
        item-view.html 에서 요청하는 컨트롤러 로직이다
        둘다 사실상 messageBody에 Resource를 넣어 리턴하는 방법으로 같지만
        ResponseEntity를 사용할 경우에는 Header에 설정을 추가 할 수 있다는 장점이 있다(Content-Dispostion 설정 추가)
     */

    //attach 링크를 클릭했을때 동작하는 컨트롤러
    // 파일 다운로드 시 권한 체크같은 복잡한 상황까지 가정하여 itemId를 요청한다
    //ResponseEntity 사용하여 화면에 리턴값 바로 렌더링
    //but contentDisposition 헤더의 attachment 설정을 통해 렌더링하지 않고 첨부파일을 다운로드하도록 만듬
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId)
        throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);
        /*
            ResponseEntity를 사용할 경우 헤더 추가가 가능하다

            contentDisposition (body에 오는 컨텐츠의 기질/성향을 알려주는 속성)
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)에 attachment속성을 통해
            해당 리소스를 화면에 렌더링하는 것이 아닌
            리소스를 다운로드 받을 수 있도록 설정해준다
         */

        //파일명이 한글 같은 경우 깨질 수 있기 때문에 파일명의 encoding 과정이 필요하다
        //깨진 경우 파일 다운로드가 안되고 화면에 데이터가 깨진 상태로 렌더링되는 현상이 발생된다 (인코딩 필수!)
        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        //ContentDispostion 헤더를 위한 문자열 (attachment 속성을 추가해야 렌더링하지 않고 다운받는다)
        //파일명도 고객이 업로드한 파일이름으로 하는게 좋기 때문에 attachment뒤에 파일 이름 설정
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
            .body(resource);
    }
    
    // item-view.html 에서 th:src로 요청한 이미지를 읽어서 @ResponseBody로 이미지 바이너리를 반환
    //ResponseBody 사용 (MessageBody에 이미지를 바로 렌더링)
    @ResponseBody
    @GetMapping("/images/{filename}")
    //filename은 storeFilename이 넘어온다
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        //UrlResource를 통해 파일을 찾아오고 리턴한다
        //"file:/Users/../1231-244-asfa-dsf.png
        return new UrlResource("file:" + fileStore.getFullPath(filename));
        //위와같은 방법은 보안에 취약하기 때문에 보안을 위한 추가 패턴 찾아보는것 권장
        //아마도 storefilename이 URL로 노출 되기 때문에 보안이 취약하다고 하는것 같다
    }

}
