package com.scheshire.starlingtest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.scheshire.starlingtest.models.Image;
import com.scheshire.starlingtest.models.User;

public interface ImageRepo extends JpaRepository<Image, Long>
{
}
