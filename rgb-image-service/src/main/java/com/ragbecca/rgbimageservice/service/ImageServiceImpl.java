package com.ragbecca.rgbimageservice.service;

import com.ragbecca.rgbimageservice.model.Image;
import com.ragbecca.rgbimageservice.repository.ImageRepository;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public String addImage(String title, MultipartFile file) throws IOException {
        Image image = new Image(title);
        image.setImage(new Binary(BsonBinarySubType.BINARY, file.getBytes()));
        image = imageRepository.insert(image);
        return image.getId();
    }

    @Override
    public void removeOldImage(String title) {
        if (imageRepository.findByTitle(title).isPresent()) {
            imageRepository.deleteByTitle(title);
        }
    }

    @Override
    public Image getImage(String id) {
        return imageRepository.findById(id).get();
    }
}
