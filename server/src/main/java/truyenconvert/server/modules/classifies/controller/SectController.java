package truyenconvert.server.modules.classifies.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.classifies.dto.CreateSectDTO;
import truyenconvert.server.modules.classifies.dto.EditSectDTO;
import truyenconvert.server.modules.classifies.service.SectService;
import truyenconvert.server.modules.classifies.vm.SectVm;

import java.util.List;

@RestController
@RequestMapping("/api/sects")
public class SectController {
    private final SectService sectService;
    public SectController(
            SectService sectService
    ){
        this.sectService = sectService;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<SectVm>>> getAllSect(){
        var result = sectService.getAllSect();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<SectVm>> getById(
            @PathVariable("id") int id
    ){
        var result = sectService.getById(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<SectVm>> editSect(
            @AuthenticationPrincipal User user,
            @PathVariable("id") int id,
            @RequestBody EditSectDTO dto
    ){
        var result = sectService.editSect(dto, id, user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<SectVm>> createSect(
            @AuthenticationPrincipal User user,
            @RequestBody CreateSectDTO dto
    ){
        var result = sectService.createSect(dto, user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
