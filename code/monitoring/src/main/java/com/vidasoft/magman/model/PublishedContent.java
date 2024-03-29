package com.vidasoft.magman.model;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
public class PublishedContent extends AbstractEntity {

    public LocalDateTime publishDate;
    public LocalDateTime lastModified;

}
