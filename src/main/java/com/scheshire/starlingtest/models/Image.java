package com.scheshire.starlingtest.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Image db object
 */
@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {
   @Setter(AccessLevel.NONE)
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
   
   @Column(name = "file")
   private String file;
   
   @OneToOne(fetch=FetchType.LAZY)
   @JoinColumn(name="thumbnail_id")
   private Image thumbnail;
   
   @ManyToOne(fetch=FetchType.LAZY)
   @JoinColumn(name="gallery_id")
   private Gallery gallery;
}
