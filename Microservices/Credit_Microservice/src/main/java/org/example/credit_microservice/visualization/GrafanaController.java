package org.example.credit_microservice.visualization;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GrafanaController {

    @GetMapping("/grafana-panel")
    public String showGrafanaPanel(Model model) {
        // Replace with your actual panel snapshot URL
        String panelSnapshotUrl = "https://snapshots.raintank.io/dashboard/snapshot/Lxu81YWfTcx5JtQ8G01q7QER91ZMIPXH";
        model.addAttribute("panelSnapshotUrl", panelSnapshotUrl);
        return "grafana-dashboard";
    }
}