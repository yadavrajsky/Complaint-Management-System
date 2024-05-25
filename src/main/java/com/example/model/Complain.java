package com.example.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "complains")
public class Complain {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
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

    public User getCreatedForUser() {
        return createdForUser;
    }

    public void setCreatedForUser(User createdForUser) {
        this.createdForUser = createdForUser;
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
