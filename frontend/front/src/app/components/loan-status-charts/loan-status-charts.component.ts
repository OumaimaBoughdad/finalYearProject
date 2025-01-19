import { Component, OnInit } from '@angular/core';
import { VisualizationService, LoanStatusByDefaultDTO, LoanStatusByCreditHistoryDTO } from '../../services/visualization.service';
import { Chart } from 'chart.js';

@Component({
  selector: 'app-loan-status-charts',
  templateUrl: './loan-status-charts.component.html',
  standalone: true,
  styleUrls: ['./loan-status-charts.component.css']
})
export class LoanStatusChartsComponent implements OnInit {
  loanStatusByDefault: LoanStatusByDefaultDTO[] = [];
  loanStatusByCreditHistory: LoanStatusByCreditHistoryDTO[] = [];

  constructor(private visualizationService: VisualizationService) {}

  ngOnInit(): void {
    this.visualizationService.getLoanStatusByDefaultOnFile().subscribe({
      next: (data) => {
        console.log('Loan Status by Default on File:', data);
        this.loanStatusByDefault = data;
        this.renderDefaultOnFileChart();
      },
      error: (err) => {
        console.error('Failed to load loan status by default:', err);
      }
    });

    this.visualizationService.getLoanStatusByCreditHistoryLength().subscribe({
      next: (data) => {
        console.log('Loan Status by Credit History Length:', data);
        this.loanStatusByCreditHistory = data;
        this.renderCreditHistoryLengthChart();
      },
      error: (err) => {
        console.error('Failed to load loan status by credit history:', err);
      }
    });
  }

  renderDefaultOnFileChart(): void {
    if (this.loanStatusByDefault.length === 0) {
      console.warn('No data available for default on file chart.');
      return;
    }

    const ctx = document.getElementById('defaultOnFileChart') as HTMLCanvasElement;
    const chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: this.loanStatusByDefault.map(d => d.defaultOnFile),
        datasets: [
          {
            label: 'No Default',
            data: this.loanStatusByDefault.map(d => d.noDefaultCount),
            backgroundColor: 'rgba(255, 99, 132, 0.2)',
            borderColor: 'rgba(255, 99, 132, 1)',
            borderWidth: 1
          },
          {
            label: 'Default',
            data: this.loanStatusByDefault.map(d => d.defaultCount),
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgba(54, 162, 235, 1)',
            borderWidth: 1
          }
        ]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }

  renderCreditHistoryLengthChart(): void {
    if (this.loanStatusByCreditHistory.length === 0) {
      console.warn('No data available for credit history length chart.');
      return;
    }

    const ctx = document.getElementById('creditHistoryLengthChart') as HTMLCanvasElement;
    const chart = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: this.loanStatusByCreditHistory.map(d => d.creditHistoryLength),
        datasets: [
          {
            label: 'No Default',
            data: this.loanStatusByCreditHistory.map(d => d.noDefaultCount),
            backgroundColor: 'rgba(255, 159, 64, 0.2)',
            borderColor: 'rgba(255, 159, 64, 1)',
            borderWidth: 1
          },
          {
            label: 'Default',
            data: this.loanStatusByCreditHistory.map(d => d.defaultCount),
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
          }
        ]
      },
      options: {
        scales: {
          y: {
            beginAtZero: true
          }
        }
      }
    });
  }
}
