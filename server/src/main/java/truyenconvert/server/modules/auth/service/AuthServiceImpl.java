package truyenconvert.server.modules.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.auth.dtos.SignInDTO;
import truyenconvert.server.modules.auth.dtos.SignUpDTO;
import truyenconvert.server.modules.auth.exceptions.EmailUsedException;
import truyenconvert.server.modules.auth.vm.TokenVm;
import truyenconvert.server.modules.common.service.MessageService;
import truyenconvert.server.modules.jwt.JwtService;
import truyenconvert.server.modules.users.exceptions.UserNotFoundException;
import truyenconvert.server.modules.users.model.Role;
import truyenconvert.server.modules.users.model.User;
import truyenconvert.server.modules.users.service.UserService;
import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService{

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    private final MessageService messageService;

    public AuthServiceImpl(
            UserService userService,
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            MessageService messageService
    ){
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
    }

    @Override
    public ResponseSuccess<TokenVm> signIn(SignInDTO dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dto.getEmail(),
                dto.getPassword()
        ));

        var user = userService.findByEmail(dto.getEmail()).orElse(null);
        if(user == null){
            throw new UserNotFoundException(messageService.getMessage("user.not.found"));
        }

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var result = TokenVm.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return new ResponseSuccess<>(messageService.getMessage("sign-in.success"), result);
    }

    @Override
    public ResponseSuccess<Boolean> signUp(SignUpDTO dto) {
        var emailLowerCase = dto.getEmail().toLowerCase().trim();
        var user = userService.findByEmail(emailLowerCase).orElse(null);

        if(user != null){
            throw new EmailUsedException(messageService.getMessage("email.used"));
        }
        var newUser = User.builder()
                .email(emailLowerCase)
                .password(passwordEncoder.encode(dto.getPassword()))
                .avatar("default avatar")
                .displayName(dto.getDisplayName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .role(Role.USER)
                .build();

        userService.save(newUser);

        return new ResponseSuccess<>(messageService.getMessage("sign-up.success"), true);
    }
}
