package com.oliver.fuzzy.energic.joy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "fuzz_sensor")
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
public class Sensor extends CustomEntity{

    @Id
    private Long id;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "city_name")
    private String cityName;

}
