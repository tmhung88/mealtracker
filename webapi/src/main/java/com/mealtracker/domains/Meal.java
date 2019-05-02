package com.mealtracker.domains;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "meals")
@NamedEntityGraph(name = "Meal.consumer",
        attributeNodes = @NamedAttributeNode("consumer"))
@Data
public class Meal implements Ownable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "consumed_date", nullable = false)
    private LocalDate consumedDate;

    @Column(name = "consumed_time", nullable = false)
    private LocalTime consumedTime;

    @Column(name = "calories", nullable = false)
    private Integer calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consumer_id")
    private User consumer;

    @Column(name = "deleted")
    private boolean deleted = false;

    @Override
    public User getOwner() {
        return consumer;
    }
}
