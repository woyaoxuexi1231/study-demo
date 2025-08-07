package org.hulei.entity.jpa.pojo;

import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "big_data_users", schema = "test")
public class BigDataUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public static BigDataUser gen(){
        Faker faker = new Faker();
        BigDataUser bigDataUsers = new BigDataUser();
        bigDataUsers.setName(faker.name().fullName());
        bigDataUsers.setEmail(faker.internet().emailAddress());
        return bigDataUsers;
    }
}