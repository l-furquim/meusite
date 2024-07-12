package meusite.domain.User;

import meusite.domain.enums.AccountStatus;
import meusite.domain.exception.DomainException;

import java.time.Instant;

public class User {
    String email;
    String password;
    String id;
    AccountStatus status;
    Instant createdAt;

    public User(){

    }

    public User(String email, String password, String id, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.Validate();
    }

    public User(String email, String password, String id, AccountStatus status, Instant createdAt) {
        this.email = email;
        this.password = password;
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public AccountStatus getStatus(){
        return status;
    }


    public Instant getCreatedAt() {
        return createdAt;
    }

    public void Validate(){
        if(this.id.isBlank()){
            throw new DomainException("User id cannot be blank");
        }
        if(this.email.isBlank()){
            throw new DomainException("User email cannot be blank");
        }
        if(this.password.isBlank()) {
            throw new DomainException("User password cannot be blank! ");
        }
        if(this.id.length() != 36){
            throw new DomainException("User id must have 36 characthers");
        }
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id='" + id + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
