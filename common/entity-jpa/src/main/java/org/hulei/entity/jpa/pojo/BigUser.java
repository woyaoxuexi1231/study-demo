package org.hulei.entity.jpa.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "big_user", schema = "test")
public class BigUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Size(max = 255)
    @NotNull
    @Column(name = "ssn", nullable = false)
    private String ssn;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 32)
    @NotNull
    @Column(name = "phone_number", nullable = false, length = 32)
    private String phoneNumber;

    @Size(max = 255)
    @NotNull
    @Column(name = "plate", nullable = false)
    private String plate;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Size(max = 255)
    @NotNull
    @Column(name = "building_number", nullable = false)
    private String buildingNumber;

    @Size(max = 255)
    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Size(max = 255)
    @NotNull
    @Column(name = "birth", nullable = false)
    private String birth;

    @Size(max = 255)
    @NotNull
    @Column(name = "company", nullable = false)
    private String company;

    @Size(max = 255)
    @NotNull
    @Column(name = "job", nullable = false)
    private String job;

    @Size(max = 255)
    @NotNull
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Size(max = 255)
    @NotNull
    @Column(name = "city", nullable = false)
    private String city;

    @Size(max = 255)
    @NotNull
    @Column(name = "week", nullable = false)
    private String week;

    @Size(max = 255)
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @Size(max = 255)
    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Lob
    @Column(name = "paragraphs", nullable = false)
    private String paragraphs;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

}