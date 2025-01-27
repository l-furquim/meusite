package meusite.service.auth;
import com.auth0.jwt.algorithms.Algorithm;
import meusite.controller.user.dto.LoginRequestDto;
import meusite.controller.user.dto.LoginResponseDto;
import meusite.repository.coments.ComentsJpaGateway;
import meusite.repository.coments.jpa.ComentsJpaRepository;
import meusite.repository.likes.LikesJpaGateway;
import meusite.repository.likes.jpa.LikesRepository;
import meusite.repository.post.PostJpaGateWay;
import meusite.repository.post.jpa.PostJpaRepository;
import meusite.repository.user.UserJpaGateWay;
import meusite.repository.user.jpa.UserJpaEntity;
import meusite.repository.user.jpa.UserJpaRepository;
import meusite.service.auth.exception.AuthException;
import meusite.service.coments.implementation.ComentsServiceImplementation;
import meusite.service.likes.implementation.LikesServiceImplementation;
import meusite.service.post.PostService;
import meusite.service.user.implementation.UserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService implements UserDetailsService {


    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Autowired
    private ComentsJpaRepository comentsJpaRepository;

    @Autowired
    private LikesRepository likesRepository;


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
            throw new AuthException("Token invalido! " + aToken + e.getMessage());
    }
    }
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException{

        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var userService = UserServiceImplementation.build(aGateway);

        var auser = userService.findUserByEmail(username);

        if(auser.isPresent()){
            return auser.get();
        }else{
            throw new UsernameNotFoundException("Usuario nao encontrado !");
        }
    }

    public LoginResponseDto login(LoginRequestDto loginRequest) {
        var aGateWay = UserJpaGateWay.build(userJpaRepository);

        var aService = UserServiceImplementation.build(aGateWay);

        if (!aService.loginCredentialsValidate(loginRequest.email(), loginRequest.password())) {
            throw new AuthException("Login invalido, senha ou email incorretos !");
        }
        var aUser = aService.findUserByEmail(loginRequest.email());

        if(aUser.get().getStatus().getDescription().equals("pending")){
            throw new AuthException("Voce nao pode se logar, verificação de email esta pendente!");
        }

        var token =  this.createToken(aUser.get());

        return new LoginResponseDto(token);
    }
    public Optional<UserJpaEntity> extractUserFromToken(String jwtToken) {
        var useremail = this.validateToken(jwtToken);

        var aGateway = UserJpaGateWay.build(userJpaRepository);

        var userServiceImplementation = UserServiceImplementation.build(aGateway);

        var user = userServiceImplementation.findUserByEmail(useremail);
        return user;
    }

    public String extractSubject(String token){
        return this.validateToken(token);
    }

    public String deleteUser(String token){
        try{
            var aGateway = UserJpaGateWay.build(userJpaRepository);

            var userServiceImplementation = UserServiceImplementation.build(aGateway);

            var postGateway = PostJpaGateWay.build( postJpaRepository);

            var commentGateway = ComentsJpaGateway.build(comentsJpaRepository);

            var likeGateway = LikesJpaGateway.build(likesRepository);

            var postService = new PostService(postGateway);

            final var anAlgorithm = Algorithm.HMAC256(TOKEN_SECRET);

            final var aVerifier = JWT.require(anAlgorithm)
                    .withIssuer(TOKEN_ISSUER)
                    .build();

            final var aDecodedToken = aVerifier.verify(token);

            var userEmail = aDecodedToken.getSubject();

            if(userEmail.isBlank()){
                throw new AuthException("este codigo não esta connectado a nenhum email !");
            }

            var user = userServiceImplementation.findUserByEmail(userEmail);

            var posts= postService.findAllByUserId(user.get());

            var comments = commentGateway.findAllByUserId(user.get());

            var likes = likeGateway.findLikesByUserId(user.get());

            if(user.isPresent()){
                for(int i= 0; i==posts.get().size(); i++){
                    postGateway.delete(posts.get().get(i));

                }
                for(int i= 0; i==comments.get().size(); i++){
                    commentGateway.delete(comments.get().get(i));
                }
                for(int i= 0; i==likes.get().size(); i++){
                    likeGateway.delete(likes.get().get(i));
                }
                userServiceImplementation.delete(user.get());
                return "Usuario excluido com sucesso";
            }else {
                throw new AuthException("Token invalido para deleção!");
            }
         } catch (Exception e) {
            throw new AuthException("Esta conta ja não existe mais");
        }
    }
}
