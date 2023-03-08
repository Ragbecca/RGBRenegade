package com.ragbecca.rgbimageservice.service;

import com.ragbecca.rgbimageservice.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {

    String addImage(String title, MultipartFile file) throws IOException;

    void removeOldImage(String title);

    Image getImage(String id);
}
