package meusite.service.user;

import meusite.controller.user.dto.GetUserModelDto;
import meusite.controller.user.dto.RegisterRequestDto;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.jpa.UserJpaEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public interface UserService {

    void save(UserJpaEntity userJpaEntity);

    void createUser(RegisterRequestDto registerRequestDto);

    Optional<UserJpaEntity> findUserByPassword(String password);

    Optional<UserJpaEntity> findUserByEmail(final String email);

    void updateUserEmailById(final String id, final String email);

    boolean loginCredentialsValidate(String email, String password);

    Optional<UserJpaEntity> findById(String id);

    GetUserModelDto formatUserToModel(UserJpaEntity userJpaEntity);

    String ChangePassword(String email, String password,String newPassword);

    String ChangePasswordNotLoggedEmail(String email);

    UserJpaEntity verifierCode(String code);

    String ChangePasswordNotLogged(UserJpaEntity userJpaEntity, String password);

    String validateRegister(String code);

    void delete(UserJpaEntity userJpaEntity);

    void updateFollowers(Integer followers, String id);

    void updateFollowing(Integer following , String id);
}
