package truyenconvert.server.modules.auth.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.auth.dtos.SignInDTO;
import truyenconvert.server.modules.auth.dtos.SignUpDTO;
import truyenconvert.server.modules.auth.vm.TokenVm;

@Service
public interface AuthService {
    public ResponseSuccess<TokenVm> signIn(SignInDTO dto);
    public ResponseSuccess<Boolean> signUp(SignUpDTO dto);
}