package com.example.HeadsOrTails.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "chatId")
    private long chatId;
    @Column(name = "date")
    private String date;
    @Column(name = "sum")
    private String sum;
    @Column(name = "result")
    private String result;

}
