package com.scheshire.starlingtest.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "galleries")
public class Gallery {
	   @Setter(AccessLevel.NONE)
	   @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	   private Long id;
	   
	   @ManyToOne(fetch=FetchType.LAZY)
	   @JoinColumn(name="user_id", nullable=false)
	   private User user;
	   
	   @Column(name = "name")
	   private String name;
	   
	   @OneToMany(fetch=FetchType.LAZY, mappedBy="gallery")
	   private Set<Image> images;
}
