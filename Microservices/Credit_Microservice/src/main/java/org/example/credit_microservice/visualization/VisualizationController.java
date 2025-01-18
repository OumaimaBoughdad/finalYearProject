package org.example.credit_microservice.visualization;


import org.example.credit_microservice.DTO.LoanStatusByCreditHistoryDTO;
import org.example.credit_microservice.DTO.LoanStatusByDefaultDTO;
import org.example.credit_microservice.visualization.VisualizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/visualization")
public class VisualizationController {

    @Autowired
    private VisualizationService visualizationService;

    @GetMapping("/loan-status-by-default")
    public List<LoanStatusByDefaultDTO> getLoanStatusByDefaultOnFile() {
        return visualizationService.getLoanStatusByDefaultOnFile();
    }

    @GetMapping("/loan-status-by-credit-history")
    public List<LoanStatusByCreditHistoryDTO> getLoanStatusByCreditHistoryLength() {
        return visualizationService.getLoanStatusByCreditHistoryLength();
    }
}