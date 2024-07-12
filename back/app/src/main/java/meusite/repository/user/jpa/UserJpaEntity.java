package meusite.repository.user.jpa;

import jakarta.persistence.*;
import meusite.controller.user.dto.GetUserModelDto;
import meusite.domain.User.User;
import meusite.domain.enums.AccountStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "users")
public class UserJpaEntity implements UserDetails {

    @Id
    @Column(name="id", nullable = false)
    private String id;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="created_at", nullable = false)
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name="status" ,nullable=false)
    private AccountStatus status;

    public UserJpaEntity(){

    }

    private UserJpaEntity(String id, String email, String password, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public UserJpaEntity(String id, String email, String password, Instant createdAt, AccountStatus status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.status = status;
    }

    public static UserJpaEntity from(final User user){
        return new UserJpaEntity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getStatus()
                );
    }

    public static UserJpaEntity fromWithRole(final User user){
        return new UserJpaEntity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getStatus()
        );
    }
    public static GetUserModelDto toModel(UserJpaEntity userJpaEntity){

        // Converter a string original para Instant


        // Converter Instant para ZonedDateTime (opcional: ajustar o fuso horário)
        ZonedDateTime zonedDateTime = userJpaEntity.getCreatedAt().atZone(ZoneId.of("UTC"));

        // Formatar ZonedDateTime para a string desejada
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = formatter.format(zonedDateTime);



        return new GetUserModelDto(
                userJpaEntity.getEmail(),
                userJpaEntity.getPassword(),
                formattedDateTime

        );
    }
    public void setStatus(AccountStatus status){
        this.status = status;
    }
    public AccountStatus getStatus(){
        return this.status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }


}
