package HyeonRi.TripDrawApp.controller;

import HyeonRi.TripDrawApp.service.AiService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
public class PlaceController {
    private final AiService aiService;

    public PlaceController(AiService aiService) {
        this.aiService = aiService;
    }

    /**
    * photoReference를 받아서 Google에서 이미지를 가져와 그대로 리턴
    */
    @GetMapping("/photo")
    public ResponseEntity<ByteArrayResource> proxyPhoto(@RequestParam("reference") String photoReference) {
        System.out.println("▶ [PlaceController.proxyPhoto] reference = " + photoReference);

        ByteArrayResource img = aiService.downloadPhotoBytes(photoReference);


        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(img);
    }
}