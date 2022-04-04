package com.example.springcloudinary.controller;

import com.example.springcloudinary.dto.Message;
import com.example.springcloudinary.entity.Image;
import com.example.springcloudinary.service.CloudinaryService;
import com.example.springcloudinary.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cloud")
@CrossOrigin
public class MainController {

    private final CloudinaryService cloudinaryService;

    private final ImageService imageService;

    @Autowired
    public MainController(CloudinaryService cloudinaryService, ImageService imageService) {
        this.cloudinaryService = cloudinaryService;
        this.imageService = imageService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Image>> list(){
        List<Image> imageList = imageService.list();
        return ResponseEntity.ok(imageList);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam MultipartFile multipartFile) throws IOException {
        BufferedImage bi = ImageIO.read(multipartFile.getInputStream());
        if(bi == null){
            return new ResponseEntity<>(new Message("Invalid Request"), HttpStatus.BAD_REQUEST);
        }
        Map result = cloudinaryService.upload(multipartFile);
        Image image = new Image((String) result.get("original_filename"), (String) result.get("url"),
                (String) result.get("public_id"));
        imageService.save(image);
        return new ResponseEntity<>(new Message("Image submitted"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) throws IOException{
        if(!imageService.exists(id))
            return new ResponseEntity<>(new Message("Not found"), HttpStatus.NOT_FOUND);
        Image image = imageService.getOne(id).get();
        Map result = cloudinaryService.delete(image.getImageId());
        imageService.delete(id);
        return new ResponseEntity<>(new Message("Image deleted"), HttpStatus.NO_CONTENT);
    }


}
