package meusite.service.user;
import meusite.controller.user.dto.GetUserModelDto;
import meusite.controller.user.dto.RegisterRequestDto;
import meusite.domain.User.User;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.exception.UserException;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.service.auth.exception.AuthException;
import meusite.service.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserJpaGateWay userJpaGateWay;


    public UserService(){

    }


    public UserService(UserJpaGateWay userJpaGateWay){
        this.userJpaGateWay = userJpaGateWay;
    }

    public void save(UserJpaEntity userJpaEntity){
        userJpaGateWay.save(userJpaEntity);
    }

    public String createUser(RegisterRequestDto registerRequestDto){
        passwordEncoder = new BCryptPasswordEncoder();


        ZoneId zoneId = ZoneId.systemDefault();

        Instant instant = Instant.now();

        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        if(this.isUserAlredyRegister(registerRequestDto.email(),registerRequestDto.password()).isPresent()){
            throw new ServiceException("Ja existe um usuario com este email !");
        }
        if(registerRequestDto.email().isEmpty() || registerRequestDto.password().isEmpty()){
            throw new ServiceException("Voce nao pode criar um usuario com senha ou email vazios !");
        }

        var anUser = new User(
                registerRequestDto.email(),passwordEncoder.encode(registerRequestDto.password()), UUID.randomUUID().toString(),Instant.from(zonedDateTime)
        );

        var userEntity = UserJpaEntity.from(anUser);


        try{
            userJpaGateWay.save(userEntity);
        }catch (IllegalArgumentException e){
            throw new UserException(e.getMessage());
        }catch(OptimisticLockingFailureException e) {
            throw new UserException((e.getMessage()));
        }
        return userEntity.getId();
    }

    public Optional<UserJpaEntity> findUserByPassword( String password){
        return userJpaGateWay.findByPassword(password);
    }

    public Optional<UserJpaEntity> findUserByEmail(final String email){
        var user = userJpaGateWay.findByEmail(email);
        if(user.isEmpty()) {
            throw new ServiceException("nao foi possivel encontrar o usuario ! " + email);
        }
        return user;
    }

    public void updateUserEmailById(final String id, final String email){
        var anUser = this.findById(id);
        anUser.get().setEmail(email);
    }

    public Optional<UserJpaEntity> isUserAlredyRegister(String email, String password){
        var anUser = this.findUserByEmail(email);
        if(anUser.isPresent()){
            return anUser;
        }
        return Optional.empty();
    }
    public boolean loginCredentialsValidate(String email, String password){
        var aUser = this.findUserByEmail(email);

        if(aUser.isPresent()) {

            return passwordEncoder.matches(password, aUser.get().getPassword());
        }

        return false;
        }

    public Optional<UserJpaEntity> findById(String id){
        return userJpaGateWay.findById(id);
    }

    public GetUserModelDto formatUserToModel(UserJpaEntity userJpaEntity){
        return UserJpaEntity.toModel(userJpaEntity);
    }



}
