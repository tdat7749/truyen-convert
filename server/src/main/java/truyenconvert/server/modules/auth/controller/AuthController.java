package truyenconvert.server.modules.auth.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.auth.dtos.SignInDTO;
import truyenconvert.server.modules.auth.dtos.SignUpDTO;
import truyenconvert.server.modules.auth.service.AuthService;
import truyenconvert.server.modules.auth.vm.TokenVm;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping(value = "/sign-up")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> signUp(@Valid @RequestBody SignUpDTO dto){
        var result = authService.signUp(dto);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping(value = "/sign-in")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<TokenVm>> signIn(@RequestBody SignInDTO dto){
        var result = authService.signIn(dto);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
