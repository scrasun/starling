package com.scheshire.starlingtest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheshire.starlingtest.models.Gallery;
import com.scheshire.starlingtest.models.Image;
import com.scheshire.starlingtest.models.User;

public interface GalleryRepo extends JpaRepository<Gallery, Long>
{
}
