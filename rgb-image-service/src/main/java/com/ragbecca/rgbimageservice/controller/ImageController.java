package com.ragbecca.rgbimageservice.controller;

import com.ragbecca.rgbimageservice.model.Image;
import com.ragbecca.rgbimageservice.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.zip.GZIPInputStream;

@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Autowired
    private WebClient.Builder webClient;
    @Autowired
    private GridFsOperations operations;

    private static String uncompress(byte[] compressedContent) throws IOException {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressedContent));
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
        String line;
        StringBuilder out = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<?> addPhoto(@RequestParam("title") String title,
                                      @RequestParam("image") MultipartFile image) throws IOException {
        imageService.removeOldImage(title);
        String id = imageService.addImage(title, image);

        // Send mail request to mail server
        webClient.build().get()
                .uri("http://localhost:8090/api/userInfo/user/image/upload?username=" + title +
                        "&url=http://localhost:8080/api/image/get/" + id)
                .exchange().block();

        return ResponseEntity.ok("http://localhost:8080/api/image/get/" + id);
    }

    @ResponseBody
    @GetMapping(value = "/get", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<String> getPhoto(@RequestParam String id) {
        Image image = imageService.getImage(id);

        Base64.Encoder encoder = Base64.getEncoder();

        return ResponseEntity.ok()
                .body(encoder.encodeToString(image.getImage().getData()));
    }

    @ResponseBody
    @GetMapping(value = "/get-user", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<String> getPhotoUser(@RequestParam String username) {
        Optional<Image> image = imageService.getImageFromUsername(username);

        if (image.isPresent()) {
            Base64.Encoder encoder = Base64.getEncoder();
            return ResponseEntity.ok()
                    .body(encoder.encodeToString(image.get().getImage().getData()));
        } else {
            return ResponseEntity.ok().body("Not found");
        }
    }
}
