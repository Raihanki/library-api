package com.raihanhori.library_app.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "books")
@EntityListeners(AuditingEntityListener.class)
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(length = 200, nullable = false)
	private String title;
	
	@Column(length = 2000, nullable = false)
	private String author;
	
	@Column(length = 200, nullable = false)
	private String publisher;
	
	@Column(length = 200, nullable = false)
	private Instant publishedAt;
	
	@Column(nullable = false)
	private Boolean isAvailable;
	
	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@LastModifiedDate
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	
	@ManyToOne
	@JoinColumn(columnDefinition = "category_id", referencedColumnName = "id", nullable = false)
	private Category category;
	
}
