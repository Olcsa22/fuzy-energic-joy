package com.oliver.fuzzy.energic.joy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Date;


@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public abstract class CustomEntity {

    protected Date createdOn;
    protected Date modifiedOn;

    @PrePersist
    protected void onCreate() {
        if(createdOn == null) //this if is only there because when generating metrics, I randomly generate creation date so we can get varying data. Otherwise, I would not add it.
            createdOn = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedOn = new Date();
    }

}
