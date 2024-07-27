package com.travelbnb.controller;


import com.travelbnb.entity.AppUser;
import com.travelbnb.entity.Image;
import com.travelbnb.entity.Property;
import com.travelbnb.repository.ImageRepository;
import com.travelbnb.repository.PropertyRepository;
import com.travelbnb.service.BucketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private PropertyRepository propertyRepository;
    private ImageRepository imageRepository;
    private BucketService bucketService;

    public ImageController(PropertyRepository propertyRepository, ImageRepository imageRepository, BucketService bucketService) {
        this.propertyRepository = propertyRepository;
        this.imageRepository = imageRepository;
        this.bucketService = bucketService;
    }


    @PostMapping(path = "/upload/file/{bucketName}/property/{propertyId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Image> uploadPropertyPhotos(
            @RequestParam MultipartFile file,
            @PathVariable String bucketName,
            @PathVariable long propertyId
            //  @AuthenticationPrincipal AppUser user
    ) {

        Property property = propertyRepository.findById(propertyId).get();
        String imageUrl = bucketService.uploadFile(file, bucketName);

        Image image = new Image();
        image.setProperty(property);
        image.setImageUrl(imageUrl);
        Image savedImageEntity = imageRepository.save(image);
        return new ResponseEntity<>(savedImageEntity, HttpStatus.CREATED);
    }
}
