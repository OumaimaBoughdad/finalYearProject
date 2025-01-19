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
  grafanaUrl = "https://snapshots.raintank.io/dashboard/snapshot/Lxu81YWfTcx5JtQ8G01q7QER91ZMIPXH";
}
