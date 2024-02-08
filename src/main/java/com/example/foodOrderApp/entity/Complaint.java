package com.example.foodOrderApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String subject;
    private String description;
    private String complaintDate;
    private String replyDate;
    private String reply;
    private Status status;
    private String attachment;

    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", subject='" + subject + '\'' +
                ", description='" + description + '\'' +
                ", complaintDate='" + complaintDate + '\'' +
                ", replyDate='" + replyDate + '\'' +
                ", reply='" + reply + '\'' +
                ", status=" + status +
                '}';
    }
}
