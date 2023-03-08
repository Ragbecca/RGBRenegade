package com.ragbecca.rgbimageservice.repository;

import com.ragbecca.rgbimageservice.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ImageRepository extends MongoRepository<Image, String> {

    void deleteByTitle(String title);

    Optional<Image> findByTitle(String title);
}
