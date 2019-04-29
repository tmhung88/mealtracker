package com.mealtracker.domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "meals")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getConsumedOn() {
        return consumedOn;
    }

    public void setConsumedOn(ZonedDateTime consumedOn) {
        this.consumedOn = consumedOn;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Long getConsumedBy() {
        return consumedBy;
    }

    public void setConsumedBy(Long consumedBy) {
        this.consumedBy = consumedBy;
    }
}
