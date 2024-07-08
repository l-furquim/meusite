package meusite.service.auth;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import com.auth0.jwt.algorithms.Algorithm;
import meusite.controller.user.dto.LoginRequestDto;
import meusite.controller.user.dto.LoginResponseDto;
import meusite.controller.user.dto.RegisterRequestDto;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.repository.user.jpa.UserJpaRepository;
import meusite.service.auth.exception.AuthException;
import meusite.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;

import javax.security.auth.login.LoginException;
import java.time.Instant;
import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    UserJpaGateWay userJpaGateWay = UserJpaGateWay.build(userJpaRepository);

    @Autowired
    UserService userService = new UserService(userJpaGateWay);

    private final String TOKEN_SECRET = "gorila";
    private final String TOKEN_ISSUER = "meusite";

    public AuthService(){

    }

    public  String createToken(final UserJpaEntity userJpaEntity){
        try{
            final var anAlgorithm = Algorithm.HMAC256(TOKEN_SECRET);

            final var aToken = JWT.create()
                    .withIssuer(TOKEN_ISSUER)
                    .withSubject(userJpaEntity.getEmail())
                    .withExpiresAt(Instant.now().plusSeconds(60 * 60 * 4))
                    .sign(anAlgorithm);
            return aToken;
        }catch (IllegalArgumentException e){
            throw new AuthException(e.getMessage());
        }catch (JWTCreationException e){
            throw new AuthException(e.getMessage());
        }
    }

    public String validateToken( String aToken){
        try{
            final var anAlgorithm = Algorithm.HMAC256(TOKEN_SECRET);

            final var aVerifier = JWT.require(anAlgorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build();

            final var aDecodedToken = aVerifier.verify(aToken);

            final var aSubject = aDecodedToken.getSubject();

            return aSubject;
        } catch (Exception e) {
            throw new AuthException("Token invalido!" + aToken);
    }
    }
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException{
        var auser = userService.findUserByEmail(username);

        if(auser.isPresent()){
            return auser.get();
        }else{
            throw new UsernameNotFoundException("Usuario nao encontrado !");
        }
    }

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        var aGateWay = UserJpaGateWay.build(userJpaRepository);

        var aService = new UserService(aGateWay);

        if (!aService.loginCredentialsValidate(loginRequest.email(), loginRequest.password())) {
            throw new AuthException("Login invalido, senha ou email incorretos !");
        }
        var aUser = aService.findUserByEmail(loginRequest.email());

        var token =  this.createToken(aUser.get());

        return new LoginResponseDto(token);
    }
    public Optional<UserJpaEntity> extractUserFromToken(String jwtToken) {
        var useremail = this.validateToken(jwtToken);

        var aGateWay = UserJpaGateWay.build(userJpaRepository);

        var aService = new UserService(aGateWay);

        var user = aService.findUserByEmail(useremail);
        return user;
    }

    public String extractSubject(String token){
        return this.validateToken(token);
    }
}
