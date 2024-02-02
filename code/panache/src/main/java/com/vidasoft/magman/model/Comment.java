package com.vidasoft.magman.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Comment extends AbstractEntity {

    private String content;

    @ManyToOne
    private User author;

    private LocalDateTime created;

}
