package com.ragbecca.rgbimageservice.payload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageRequest {

    String title;
    MultipartFile image;
}
