import { Component } from '@angular/core';
import {SafePipe} from '../../pipes/safe.pipe';

@Component({
  selector: 'app-grafana-panel',
  templateUrl: './grafana-panel.component.html',
  standalone: true,
  imports: [
    SafePipe
  ],
  styleUrls: ['./grafana-panel.component.css']
})
export class GrafanaPanelComponent {
  // Remplacez par l'URL de votre tableau de bord Grafana
  grafanaUrl = "https://snapshot.raintank.io/dashboard-solo/snapshot/your-snapshot-id";
}
