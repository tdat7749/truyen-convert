package truyenconvert.server.modules.auth.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.auth.dtos.SignInDTO;
import truyenconvert.server.modules.auth.dtos.SignUpDTO;
import truyenconvert.server.modules.auth.vm.TokenVm;

@Service
public interface AuthService {
    public ResponseSuccess<TokenVm> signIn(SignInDTO dto);
    public ResponseSuccess<Boolean> signUp(SignUpDTO dto);
    public ResponseSuccess<Boolean> lockAccount(int id, User user);

    public ResponseSuccess<Boolean> unLockAccount(int id);
    public ResponseSuccess<Boolean> verifyAccount(String email,User user);
    public ResponseSuccess<Boolean> resendEmail(String email);
}