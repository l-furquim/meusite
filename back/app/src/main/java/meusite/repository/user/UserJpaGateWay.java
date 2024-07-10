package meusite.repository.user;

import meusite.repository.user.jpa.UserJpaEntity;
import meusite.repository.user.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserJpaGateWay {
    @Autowired
    private UserJpaRepository userJpaRepository;

    public UserJpaGateWay(){

    }

    private UserJpaGateWay(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }
    public static UserJpaGateWay build(final UserJpaRepository aRepository){
        return new UserJpaGateWay(aRepository);
    }

    public void save(UserJpaEntity userJpaEntity){
        userJpaRepository.save(userJpaEntity);
    }
    public Optional<UserJpaEntity> findByEmail(String email){
        return userJpaRepository.findByEmail(email);

    }
    public Optional<UserJpaEntity> findByPassword(String password){
        return userJpaRepository.findByPassword(password);
    }

    public Optional<UserJpaEntity> findById(String id){
        return userJpaRepository.findById(id);
    }

    public void changePassword(String email, String newPassword){
        this.userJpaRepository.changeUserPassword(email, newPassword);
    }

    public void changeId(String email, String newId){
        this.userJpaRepository.changeUserId(email,newId);
    }




}
