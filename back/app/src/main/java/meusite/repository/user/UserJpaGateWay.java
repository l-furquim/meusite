package meusite.repository.user;

import meusite.domain.gateway.UserGateway;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.repository.user.jpa.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

public class UserJpaGateWay implements UserGateway {

    private UserJpaRepository userJpaRepository;

    private UserJpaGateWay(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }
    public static UserJpaGateWay build(final UserJpaRepository aRepository){
        return new UserJpaGateWay(aRepository);
    }


    @Override
    public void save(UserJpaEntity userJpaEntity){
        userJpaRepository.save(userJpaEntity);
    }

    @Override
    public Optional<UserJpaEntity> findByEmail(String email){
        return userJpaRepository.findByEmail(email);

    }

    @Override
    public Optional<UserJpaEntity> findByPassword(String password){
        return userJpaRepository.findByPassword(password);
    }

    @Override
    public Optional<UserJpaEntity> findById(String id){
        return userJpaRepository.findById(id);
    }

    @Override
    public void changePassword(String email, String newPassword){
        this.userJpaRepository.changeUserPassword(email, newPassword);
    }

    @Override
    public void changeId(String email, String newId){
        this.userJpaRepository.changeUserId(email,newId);
    }

    @Override
    public void delete(UserJpaEntity userJpaEntity){
        this.userJpaRepository.delete(userJpaEntity);
    }

    @Override
    public void updateFollowers(Integer followers, String id){
        userJpaRepository.updateFollowers(followers, id);
    }
    @Override
    public void updateFollowing(Integer following, String id){
        userJpaRepository.updateFollowing(following, id);
    }

}
