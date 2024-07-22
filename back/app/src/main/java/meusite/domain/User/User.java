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
    private AccountStatus accountStatus;
    private Integer followes;
    private Integer following;

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

    public User(String email, String password, String id, AccountStatus status, Instant createdAt, AccountStatus accountStatus, Integer followes, Integer following) {
        this.email = email;
        this.password = password;
        this.id = id;
        this.status = status;
        this.createdAt = createdAt;
        this.accountStatus = accountStatus;
        this.followes = followes;
        this.following = following;
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

    public Integer getFollowes() {
        return followes;
    }

    public void setFollowes(Integer followes) {
        this.followes = followes;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
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
