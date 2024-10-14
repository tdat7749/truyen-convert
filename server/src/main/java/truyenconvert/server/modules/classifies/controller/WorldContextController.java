package truyenconvert.server.modules.classifies.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.classifies.dto.CreateWorldContextDTO;
import truyenconvert.server.modules.classifies.dto.EditWorldContextDTO;
import truyenconvert.server.modules.classifies.service.WorldContextService;
import truyenconvert.server.modules.classifies.vm.WorldContextVm;

import java.util.List;

@RestController
@RequestMapping("/api/worldcontexts")
public class WorldContextController {
    private final WorldContextService worldContextService;
    public WorldContextController(
            WorldContextService worldContextService
    ){
        this.worldContextService = worldContextService;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<WorldContextVm>>> getAllSect(){
        var result = worldContextService.getAllWorldContext();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<WorldContextVm>> getById(
            @PathVariable("id") int id
    ){
        var result = worldContextService.getById(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<WorldContextVm>> editWorldContext(
            @AuthenticationPrincipal User user,
            @PathVariable("id") int id,
            @RequestBody EditWorldContextDTO dto
    ){
        var result = worldContextService.editWorldContext(dto, id, user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<WorldContextVm>> createWorldContext(
            @AuthenticationPrincipal User user,
            @RequestBody CreateWorldContextDTO dto
    ){
        var result = worldContextService.createWorldContext(dto, user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
