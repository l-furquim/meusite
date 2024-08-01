package meusite.service.user.implementation;
import meusite.controller.user.dto.GetUserModelDto;
import meusite.controller.user.dto.RegisterRequestDto;
import meusite.domain.User.User;
import meusite.domain.enums.AccountStatus;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.exception.UserException;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.repository.verifier.UserVerifierJpaGateway;
import meusite.repository.verifier.jpa.UserVerifierJpaEntity;
import meusite.repository.verifier.jpa.UserVerifierRepository;
import meusite.service.auth.EmailService;
import meusite.service.auth.exception.AuthException;
import meusite.service.user.UserService;
import meusite.service.verifier.implementation.UserVerifierServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImplementation implements UserService {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    UserJpaGateWay userJpaGateWay;

    public static UserServiceImplementation build(UserJpaGateWay userJpaGateWay){
        return new UserServiceImplementation(
                userJpaGateWay
        );
    }


    private UserServiceImplementation(UserJpaGateWay userJpaGateWay){
        this.userJpaGateWay = userJpaGateWay;
    }
    @Override
    public void save(UserJpaEntity userJpaEntity){
        userJpaGateWay.save(userJpaEntity);
    }
    @Override
    public void createUser(RegisterRequestDto registerRequestDto, UserVerifierServiceImplementation userVerifierService, EmailService emailService){
        passwordEncoder = new BCryptPasswordEncoder();

        var aUser = this.findUserByEmail(registerRequestDto.email());

        if(aUser.isPresent()){
            throw new AuthException("Ja existe um usuario com este email !");
        }
        if(registerRequestDto.email().isEmpty() || registerRequestDto.password().isEmpty()){
            throw new AuthException("Voce nao pode criar um usuario com senha ou email vazios !");
        }
        if(
                userVerifierService.getUserVerifierByUserEmail(registerRequestDto.email()).isPresent() &&
                        userVerifierService.getUserVerifierByUserEmail(registerRequestDto.email()).get().getExpires_at().compareTo(Instant.now()) >= 0 ){
            throw  new AuthException("Seu codigo ainda nao expirou !");
        }


        UserVerifierJpaEntity userVerifierJpaEntity = new UserVerifierJpaEntity(
                registerRequestDto.email(), passwordEncoder.encode(registerRequestDto.password()), Instant.now().plusMillis(6000000),AccountStatus.PENDING,UUID.randomUUID().toString());

        try{
            userVerifierService.save(userVerifierJpaEntity);
            emailService.sendEmail(
                    registerRequestDto.email(),"Codigo para criação da conta!",
                    "aqui esta seu codigo: " + userVerifierJpaEntity.getCode());
        }catch (IllegalArgumentException e){
            throw new UserException(e.getMessage());
        }catch(OptimisticLockingFailureException e) {
            throw new UserException((e.getMessage()));
        }

    }
    @Override
    public Optional<UserJpaEntity> findUserByPassword( String password){
        return userJpaGateWay.findByPassword(password);
    }
    @Override
    public Optional<UserJpaEntity> findUserByEmail(final String email){
        var user = userJpaGateWay.findByEmail(email);
        if(user.isPresent()) {
            return user;
        }
        return Optional.empty();
    }
    @Override
    public void updateUserEmailById(final String id, final String email){
        var anUser = this.findById(id);
        anUser.get().setEmail(email);
    }
    @Override
    public boolean loginCredentialsValidate(String email, String password){
        var aUser = this.findUserByEmail(email);

        if(aUser.isPresent()) {

            return passwordEncoder.matches(password, aUser.get().getPassword());
        }

        return false;
        }
    @Override
    public Optional<UserJpaEntity> findById(String id){
        return userJpaGateWay.findById(id);
    }
    @Override
    public GetUserModelDto formatUserToModel(UserJpaEntity userJpaEntity){
        return UserJpaEntity.toModel(userJpaEntity);
    }
    @Override
    public String ChangePassword(String email, String password,String newPassword) {
        var anUser = this.findUserByEmail(email).get();





        if(passwordEncoder.matches(password, anUser.getPassword())){
            var passwordEncoded = passwordEncoder.encode(newPassword);

            this.userJpaGateWay.changePassword(email, passwordEncoded);


            return "Senha alterada com sucesso !";
        }
        throw new AuthException("Senha incorreta!");
        
    }
    @Override
    public String ChangePasswordNotLoggedEmail(String email, EmailService emailService, UserVerifierServiceImplementation userVerifierService) {
        var anUser = this.findUserByEmail(email);

        String code = UUID.randomUUID().toString();

        if(anUser.isEmpty()){
            throw new UserException("Não é possivel alterar sua senha: Seu cadastro não existe !");
        }

        var aVerifier =  userVerifierService.getUserVerifierByUserEmail(email);

        if(
                aVerifier.isPresent() &&
                       aVerifier.get().getExpires_at().compareTo(Instant.now()) >= 0 ){
            throw  new AuthException("Seu codigo ainda nao expirou !");
        }


        UserVerifierJpaEntity userVerifierJpaEntity = new UserVerifierJpaEntity(
                email, anUser.get().getPassword(), Instant.now().plusMillis(6000000),AccountStatus.PENDING,code);

        try{
            userVerifierService.save(userVerifierJpaEntity);
        }catch (IllegalArgumentException e){
            throw new UserException(e.getMessage());
        }catch(OptimisticLockingFailureException e) {
            throw new UserException((e.getMessage()));
        }

        emailService.sendEmail(email, "Mudança de senha",code + " Aqui esta seu codigo !");
        return "Senha alterada com sucesso !";
    }
    @Override
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
    @Override
    public UserJpaEntity verifierCode(String code,UserVerifierServiceImplementation userVerifierServiceImplementation){


        var aUserVerifier = userVerifierServiceImplementation.getUserVerifierByCode(code);

        var anUser = this.userJpaGateWay.findByEmail(aUserVerifier.get().getUserEmail());

        if(aUserVerifier.isEmpty()){
            throw new UserException("Codigo invalido ou incorreto !");
        }
        userVerifierServiceImplementation.delete(aUserVerifier.get());

        return anUser.get();

    }
    @Override
    public String validateRegister(String code, UserVerifierServiceImplementation userVerifierService){
        ZoneId zoneId = ZoneId.systemDefault();

        Instant instant = Instant.now();

        ZonedDateTime zonedDateTime = instant.atZone(zoneId);

        var userVerifier = userVerifierService.getUserVerifierByCode(code);

        if(userVerifier.isEmpty()){
            throw new UserException("Codigo invalido , nao existe nenhum login com este codigo!");
        }
        if(userVerifier.get().getExpires_at().compareTo(Instant.now()) <=0){
            userVerifierService.delete(userVerifier.get());
            throw new UserException("Seu codigo de validação expirou!");
        }

        userVerifier.get().setUserJpaEntityStatus(AccountStatus.ACTIVE);

        var anUser = new User(
                userVerifier.get().getUserEmail(),userVerifier.get().getUserPassword(),
                UUID.randomUUID().toString(),AccountStatus.ACTIVE,Instant.from(zonedDateTime));

        anUser.setFollowing(0);
        anUser.setFollowes(0);



        var userEntity = UserJpaEntity.from(anUser);

        try{
            this.userJpaGateWay.save(userEntity);
            userVerifierService.delete(userVerifier.get());
        }catch (IllegalArgumentException e){
            throw new UserException(e.getMessage());
        }catch(OptimisticLockingFailureException e) {
            throw new UserException((e.getMessage()));
        }

        return "Validação realizada com sucesso !";
    }
    @Override
    public void delete(UserJpaEntity userJpaEntity){
        this.userJpaGateWay.delete(userJpaEntity);
    }
    @Override
    public void updateFollowers(Integer followers, String id){
        userJpaGateWay.updateFollowers(followers, id);
    }
    @Override
    public void updateFollowing(Integer following , String id){
        userJpaGateWay.updateFollowing(following, id);
    }


}
