package meusite.repository.verifier.jpa;


import jakarta.persistence.*;
import meusite.domain.enums.AccountStatus;
import meusite.repository.user.jpa.UserJpaEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="user_verifier")
public class UserVerifierJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="verifier_id")
    private Long id;


    @Column(name="user_email")
    private String userEmail;

    @Column(name="user_password")
    private String userPassword;

    @Column(name="expires_at")
    private Instant expires_at;

    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private AccountStatus userJpaEntityStatus;

    @Column(name="code")
    private String code;

    public UserVerifierJpaEntity() {
    }

    public UserVerifierJpaEntity(String userEmail,String userPassword, Instant expires_at, AccountStatus userJpaEntityStatus, String code) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.expires_at = expires_at;
        this.userJpaEntityStatus = userJpaEntityStatus;
        this.code = code;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserJpaEntity(String userEmail) {
        this.userEmail = userEmail;
    }

    public Instant getExpires_at() {
        return this.expires_at;
    }

    public void setExpires_at(Instant expires_at) {
        this.expires_at = expires_at;
    }

    public AccountStatus getUserJpaEntityStatus() {
        return userJpaEntityStatus;
    }

    public void setUserJpaEntityStatus(AccountStatus userJpaEntityStatus) {
        this.userJpaEntityStatus = userJpaEntityStatus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public static UserVerifierJpaEntity build(UserJpaEntity user){
        return new UserVerifierJpaEntity(user.getEmail(),user.getPassword(),
                Instant.now().plusMillis(900000),
                user.getStatus(),
                UUID.randomUUID().toString());
    }
}
