package truyenconvert.server.modules.report.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.report.dtos.CreateReportTypeDTO;
import truyenconvert.server.modules.report.dtos.EditReportTypeDTO;
import truyenconvert.server.modules.report.service.ReportTypeService;
import truyenconvert.server.modules.report.vm.ReportTypeVm;
import truyenconvert.server.modules.report.vm.ReportVm;

import java.util.List;

@RestController
@RequestMapping("/api/rptypes")
public class ReportTypeController {

    private final ReportTypeService reportTypeService;

    public ReportTypeController(
            ReportTypeService reportTypeService
    ){
        this.reportTypeService = reportTypeService;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<ReportTypeVm>>> getAllReportType(){
        var result = reportTypeService.getAllReportType();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ReportTypeVm>> getReportTypeById(
            @PathVariable("id") int id
    ){
        var result = reportTypeService.getReportTypeById(id);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ReportTypeVm>> createReportType(
            @RequestBody @Valid CreateReportTypeDTO dto,
            @AuthenticationPrincipal User user
            ){
        var result = reportTypeService.createReportType(dto,user);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ReportTypeVm>> editReportType(
            @RequestBody @Valid EditReportTypeDTO dto,
            @PathVariable int id,
            @AuthenticationPrincipal User user
    ){
        var result = reportTypeService.editReportType(dto,id,user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
