package com.example.SOARSpringBoot.entity;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
    @Table(name="users")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotEmpty(message= "UserName cannot be empty")
        @Column(nullable = false, length = 45)
        @Pattern(regexp = "^[A-Za-z0-9]+$")
        private String username;

        @NotEmpty(message= "emaiId cannot be empty")
        @Email(message ="please provide valid email Id")
        @Column(unique = true,nullable = false)
        private String emailId;

        @Size(min = 8,message = "Password must be min of 8 characters")
        @NotEmpty(message="Password cannot be empty")
        @Column(nullable = false)
        private String password;

    public User(Long id, String username, String emailId,   String password,  String role) {
        this.id = id;
        this.username = username;
        this.emailId = emailId;
        this.password = password;
        this.role = role;
    }

    @NotEmpty(message="Role cannot be empty")
        @Column(nullable = false)
        private String role;
        public User() {

        }
        public User(String username, String emailId, String password, String role) {
            this.username = username;
            this.emailId = emailId;
            this.password = password;
            this.role = role;
        }

        public Long getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmailId() {
            return emailId;
        }

        public void setEmailId(String emailId) {
            this.emailId = emailId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }



    }


