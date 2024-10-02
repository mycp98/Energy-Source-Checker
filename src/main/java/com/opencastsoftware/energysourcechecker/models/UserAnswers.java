package com.opencastsoftware.energysourcechecker.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="userAnswers")
public class UserAnswers {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String postcode;

}
