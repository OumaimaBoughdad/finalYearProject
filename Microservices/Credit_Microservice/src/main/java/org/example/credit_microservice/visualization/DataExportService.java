package org.example.credit_microservice.visualization;

import org.example.credit_microservice.entities.LoanPredictionRequest;
import org.example.credit_microservice.entities.LoanPredictionResponse;
import org.example.credit_microservice.repositories.LoanPredictionRepository;
import org.example.credit_microservice.repositories.LoanPredictionResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DataExportService {

    private static final Logger logger = LoggerFactory.getLogger(DataExportService.class);

    @Autowired
    private LoanPredictionRepository requestRepository;

    @Autowired
    private LoanPredictionResponseRepository responseRepository;

    @Value("${python.script.directory}")
    private String pythonScriptDirectory;

    @Scheduled(fixedRate = 60000) // Export every minute
    public void exportRequestsToCsv() {
        logger.info("Starting CSV export process...");

        // Fetch all loan requests
        List<LoanPredictionRequest> requests = requestRepository.findAll();
        logger.info("Found {} loan requests in the database.", requests.size());

        // Ensure the directory exists
        Path directoryPath = Paths.get(pythonScriptDirectory);
        if (!Files.exists(directoryPath)) {
            try {
                logger.info("Directory does not exist. Creating directory: {}", directoryPath);
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                logger.error("Failed to create directory: {}", directoryPath, e);
                return; // Exit if directory creation fails
            }
        }

        // Construct the full path to the CSV file
        String csvFilePath = Paths.get(pythonScriptDirectory, "loan_requests.csv").toString();
        logger.info("Writing CSV file to: {}", csvFilePath);

        try (FileWriter writer = new FileWriter(csvFilePath)) {
            // Write CSV header
            writer.write("person_age,person_income,person_home_ownership,person_emp_length,loan_intent,loan_grade,loan_amnt,loan_int_rate,loan_percent_income,cb_person_default_on_file,cb_person_cred_hist_length,loan_status\n");

            // Write data rows
            // Write data rows
            for (LoanPredictionRequest request : requests) {
                // Fetch the corresponding LoanPredictionResponse
                Optional<LoanPredictionResponse> responseOptional = responseRepository.findByLoanPredictionRequest(request);
                int loanStatus = responseOptional.map(LoanPredictionResponse::getLoan_status).orElse(0); // Use 0 for missing responses

                writer.write( request.getPerson_age() + "," + request.getPerson_income() + "," +
                        request.getPerson_home_ownership() + "," + request.getPerson_emp_length() + "," +
                        request.getLoan_intent() + "," + request.getLoan_grade() + "," + request.getLoan_amnt() + "," +
                        request.getLoan_int_rate() + "," + request.getLoan_percent_income() + "," +
                        request.getCb_person_default_on_file() + "," + request.getCb_person_cred_hist_length() + "," +
                        loanStatus + "\n");
            }
            logger.info("CSV file successfully written.");
        } catch (IOException e) {
            logger.error("Failed to write CSV file: {}", csvFilePath, e);
        }
    }
}