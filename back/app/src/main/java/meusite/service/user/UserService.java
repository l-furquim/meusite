package meusite.service.user;
import meusite.controller.user.dto.GetUserModelDto;
import meusite.controller.user.dto.RegisterRequestDto;
import meusite.domain.User.User;
import meusite.repository.post.jpa.PostJpaEntity;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.exception.UserException;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.service.auth.EmailService;
import meusite.service.auth.exception.AuthException;
import meusite.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Autowired
    EmailService emailService;


    public UserService(){

    }


    public UserService(UserJpaGateWay userJpaGateWay){
        this.userJpaGateWay = userJpaGateWay;
    }

    public void save(UserJpaEntity userJpaEntity){
        userJpaGateWay.save(userJpaEntity);
    }

    public void createUser(RegisterRequestDto registerRequestDto){
        passwordEncoder = new BCryptPasswordEncoder();


        ZoneId zoneId = ZoneId.systemDefault();

        Instant instant = Instant.now();

        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        if(this.findUserByEmail(registerRequestDto.email()).isPresent()){
            throw new AuthException("Ja existe um usuario com este email !");
        }
        if(registerRequestDto.email().isEmpty() || registerRequestDto.password().isEmpty()){
            throw new AuthException("Voce nao pode criar um usuario com senha ou email vazios !");
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

    }

    public Optional<UserJpaEntity> findUserByPassword( String password){
        return userJpaGateWay.findByPassword(password);
    }

    public Optional<UserJpaEntity> findUserByEmail(final String email){
        var user = userJpaGateWay.findByEmail(email);
        if(user.isPresent()) {
            return user;
        }
        return Optional.empty();
    }

    public void updateUserEmailById(final String id, final String email){
        var anUser = this.findById(id);
        anUser.get().setEmail(email);
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

    public String ChangePassword(String email, String password,String newPassword) {
        var anUser = this.findUserByEmail(email).get();

        Logger logger = LoggerFactory.getLogger(UserService.class);



        if(passwordEncoder.matches(password, anUser.getPassword())){
            var passwordEncoded = passwordEncoder.encode(newPassword);

            this.userJpaGateWay.changePassword(email, passwordEncoded);

            logger.info(anUser.getPassword());
            return "Senha alterada com sucesso !";
        }
        throw new AuthException("Senha incorreta!");
        
    }
    public String ChangePasswordNotLoggedEmail(String email) {
        var anUser = this.findUserByEmail(email);

        String code = UUID.randomUUID().toString();

        if(anUser.isEmpty()){
            throw new UserException("Não é possivel alterar sua senha: Seu cadastro não existe !");
        }

        this.userJpaGateWay.changeId(email,code);

        emailService.sendEmail(email, "Mudança de senha",code + " Aqui esta seu codigo !");
        return "Senha alterada com sucesso !";
    }

    public UserJpaEntity verifierCode(String code){
        var anUser = this.userJpaGateWay.findById(code);

        if(anUser.isEmpty()){
            throw new UserException("Codigo invalido ou incorreto !");
        }
        return anUser.get();

    }

    public String ChangePasswordNotLogged(UserJpaEntity userJpaEntity, String password){

        if(this.loginCredentialsValidate(userJpaEntity.getEmail(), password)){
            throw new UserException("Não e possivel alterar sua senha para uma ja existente !");
        }

        var encodedPassword = this.passwordEncoder.encode(password);

        this.userJpaGateWay.changePassword(userJpaEntity.getEmail(),encodedPassword );

        if(password.isEmpty()){
            throw new UserException("Voce nao pode alterar a senha para vazio !");
        }
        return "Senha alterada com sucesso !";
    }

}
