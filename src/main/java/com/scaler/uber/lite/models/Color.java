package com.scaler.uber.lite.models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author mohit@interviewbit.com on 05/01/21
 **/
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Color extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
}

