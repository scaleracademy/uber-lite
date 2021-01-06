package com.scaler.uber.lite.models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author mohit@interviewbit.com on 05/01/21
 **/
@MappedSuperclass // don't create table
@EntityListeners(AuditingEntityListener.class)
@Data
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)  // Jpa
    @CreatedDate // hibernate
    private Date createdAt; // whenever the row is created - automatically set this value

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate // hibernate
    private Date updatedAt; // whenever the row is modified - automatically update this
}
