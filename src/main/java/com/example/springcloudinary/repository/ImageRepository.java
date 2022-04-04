package com.example.springcloudinary.repository;

import com.example.springcloudinary.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.image.ImageFilter;
import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByOrderById();
}
