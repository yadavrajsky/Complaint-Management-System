package com.example.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "complains")
public class Complain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String complainType;

    @Column(nullable = false)
    private String complainDescription;

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdByUser;

    @ManyToOne
    @JoinColumn(name = "created_for_user_id", nullable = false)
    private User createdForUser;

    @Column(nullable = false)
    private LocalDateTime createdOnDateTime;

    @ManyToOne
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedTo;

    @Column(nullable = false)
    private String status;

    // Default Constructor
    public Complain() {
        // Empty constructor required by JPA
    }

    // Constructor with all fields
    public Complain(String complainType, String complainDescription, User createdByUser,
            LocalDateTime createdOnDateTime, User assignedTo, String status, User createdForUser) {
        this.complainType = complainType;
        this.complainDescription = complainDescription;
        this.createdByUser = createdByUser;
        this.createdOnDateTime = createdOnDateTime;
        this.assignedTo = assignedTo;
        this.status = status;
        this.createdForUser = createdForUser;
    }
    // Getters and Setters

    public User getCreatedForUser() {
        return createdForUser;
    }

    public void setCreatedForUser(User createdForUser) {
        this.createdForUser = createdForUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComplainType() {
        return complainType;
    }

    public void setComplainType(String complainType) {
        this.complainType = complainType;
    }

    public String getComplainDescription() {
        return complainDescription;
    }

    public void setComplainDescription(String complainDescription) {
        this.complainDescription = complainDescription;
    }

    public User getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = createdByUser;
    }

    public LocalDateTime getCreatedOnDateTime() {
        return createdOnDateTime;
    }

    public void setCreatedOnDateTime(LocalDateTime createdOnDateTime) {
        this.createdOnDateTime = createdOnDateTime;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Complain{" +
                "id=" + id +
                ", complainType='" + complainType + '\'' +
                ", complainDescription='" + complainDescription + '\'' +
                ", createdByUser=" + createdByUser +
                ", createdOnDateTime=" + createdOnDateTime +
                ", assignedTo=" + assignedTo +
                ", createdForUser=" + createdForUser +
                ", status='" + status + '\'' +
                '}';
    }
}
