package org.example.credit_microservice.visualization;


import org.example.credit_microservice.DTO.LoanStatusByCreditHistoryDTO;
import org.example.credit_microservice.DTO.LoanStatusByDefaultDTO;
import org.example.credit_microservice.entities.LoanPredictionRequest;
import org.example.credit_microservice.entities.LoanPredictionResponse;
import org.example.credit_microservice.repositories.LoanPredictionRepository;
import org.example.credit_microservice.repositories.LoanPredictionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VisualizationService {

    @Autowired
    private LoanPredictionRepository requestRepository;

    @Autowired
    private LoanPredictionResponseRepository responseRepository;

    // Get data for Loan Status by Default on File
    public List<LoanStatusByDefaultDTO> getLoanStatusByDefaultOnFile() {
        List<LoanPredictionRequest> requests = requestRepository.findAll();
        Map<String, long[]> counts = new HashMap<>();
        counts.put("Y", new long[]{0, 0}); // Y: [noDefaultCount, defaultCount]
        counts.put("N", new long[]{0, 0}); // N: [noDefaultCount, defaultCount]

        for (LoanPredictionRequest request : requests) {
            Optional<LoanPredictionResponse> response = responseRepository.findByLoanPredictionRequest(request);
            String defaultOnFile = request.getCb_person_default_on_file();
            int loanStatus = response.map(LoanPredictionResponse::getLoan_status).orElse(0);

            if (loanStatus == 0) {
                counts.get(defaultOnFile)[0]++;
            } else {
                counts.get(defaultOnFile)[1]++;
            }
        }

        List<LoanStatusByDefaultDTO> result = new ArrayList<>();
        result.add(new LoanStatusByDefaultDTO("Y", counts.get("Y")[0], counts.get("Y")[1]));
        result.add(new LoanStatusByDefaultDTO("N", counts.get("N")[0], counts.get("N")[1]));
        return result;
    }

    // Get data for Loan Status by Credit History Length (Binned)
    public List<LoanStatusByCreditHistoryDTO> getLoanStatusByCreditHistoryLength() {
        List<LoanPredictionRequest> requests = requestRepository.findAll();
        Map<String, long[]> counts = new HashMap<>();
        counts.put("0-2", new long[]{0, 0});
        counts.put("3-5", new long[]{0, 0});
        counts.put("6-10", new long[]{0, 0});
        counts.put("10+", new long[]{0, 0});

        for (LoanPredictionRequest request : requests) {
            Optional<LoanPredictionResponse> response = responseRepository.findByLoanPredictionRequest(request);
            int creditHistoryLength = request.getCb_person_cred_hist_length();
            int loanStatus = response.map(LoanPredictionResponse::getLoan_status).orElse(0);

            String bin;
            if (creditHistoryLength <= 2) {
                bin = "0-2";
            } else if (creditHistoryLength <= 5) {
                bin = "3-5";
            } else if (creditHistoryLength <= 10) {
                bin = "6-10";
            } else {
                bin = "10+";
            }

            if (loanStatus == 0) {
                counts.get(bin)[0]++;
            } else {
                counts.get(bin)[1]++;
            }
        }

        List<LoanStatusByCreditHistoryDTO> result = new ArrayList<>();
        counts.forEach((bin, count) -> result.add(new LoanStatusByCreditHistoryDTO(bin, count[0], count[1])));
        return result;
    }
}