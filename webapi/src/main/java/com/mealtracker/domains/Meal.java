package com.mealtracker.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "meals")
@Setter @Getter @NoArgsConstructor
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "consumed_on", nullable = false)
    private ZonedDateTime consumedOn;

    @Column(name = "calories", nullable = false)
    private Integer calories;

    @Column(name = "consumed_by")
    private Long consumedBy;
}
