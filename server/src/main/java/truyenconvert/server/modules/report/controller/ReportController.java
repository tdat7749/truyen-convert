package truyenconvert.server.modules.report.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.report.dtos.CreateReportDTO;
import truyenconvert.server.modules.report.service.ReportService;
import truyenconvert.server.modules.report.vm.ReportVm;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    public ReportController(
            ReportService reportService
    ){
        this.reportService = reportService;
    }

    @GetMapping("/admin")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<ReportVm>>>> getAllReportForAdmin(
            @RequestParam(value = "page_index",defaultValue = "0") int pageIndex,
            @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
            @RequestParam(value = "status",defaultValue = "0") int status
    ){
        var result = reportService.getAllReportForAdmin(pageIndex,status,sort);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/user")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<ReportVm>>>> getAllReportForUser(
            @RequestParam(value = "page_index",defaultValue = "0") int pageIndex,
            @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
            @RequestParam(value = "status",defaultValue = "0") int status,
            @AuthenticationPrincipal User user
            ){
        var result = reportService.getAllReportForUser(pageIndex,status,sort,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ReportVm>> getReportById(
            @PathVariable("id") int id
    ){
        var result = reportService.getReportById(id);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ReportVm>> handleReport(
            @PathVariable("id") int id,
            @AuthenticationPrincipal User user
    ){
        var result = reportService.handleReport(id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ReportVm>> createReport(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateReportDTO dto
    ){
        var result = reportService.createReport(dto,user);

        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }
}
