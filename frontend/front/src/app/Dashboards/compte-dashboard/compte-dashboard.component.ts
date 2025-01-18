import { Component, OnInit } from '@angular/core';
import { CompteService } from '../../services/compte.service';
import { Compte } from '../../models/compte.model';
import Chart from 'chart.js/auto';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-compte-dashboard', // Nouveau sélecteur
  templateUrl: './compte-dashboard.component.html', // Nouveau template
  standalone: true,
  imports: [CommonModule, FormsModule],
  styleUrls: ['./compte-dashboard.component.css'], // Nouveau style
})
export class CompteDashboardComponent implements OnInit { // Nouveau nom du composant
  public lineChartCompteDate: any;
  public pieChartCompteType: any;
  public barChartCompteSolde: any;

  constructor(private compteService: CompteService) {}

  ngOnInit(): void {
    this.loadComptes();
  }

  loadComptes(): void {
    this.compteService.getComptes().subscribe((comptes) => {
      this.createLineChartCompteDate(comptes);
      this.createPieChartCompteType(comptes);
      this.createBarChartCompteSolde(comptes);
    });
  }

  createLineChartCompteDate(comptes: Compte[]): void {
    const dateData = comptes.reduce((acc, compte) => {
      const date = new Date(compte.dateOuverture).toLocaleDateString();
      acc[date] = (acc[date] || 0) + 1;
      return acc;
    }, {} as { [key: string]: number });

    const labels = Object.keys(dateData);
    const data = Object.values(dateData);

    const ctx = document.getElementById('lineChartCompteDate') as HTMLCanvasElement;
    if (ctx) {
      this.lineChartCompteDate = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            {
              label: 'Nombre de comptes ouverts',
              data: data,
              borderColor: '#FF6384',
              fill: false,
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'top' },
            title: {
              display: true,
              text: 'Évolution des comptes ouverts par date',
            },
          },
          scales: {
            x: { title: { display: true, text: 'Date' } },
            y: { title: { display: true, text: 'Nombre de comptes' } },
          },
        },
      });
    } else {
      console.error("Element with ID 'lineChartCompteDate' not found.");
    }
  }

  createPieChartCompteType(comptes: Compte[]): void {
    const typeCounts = comptes.reduce((acc, compte) => {
      acc[compte.typeCompte] = (acc[compte.typeCompte] || 0) + 1;
      return acc;
    }, {} as { [key: string]: number });

    const labels = Object.keys(typeCounts);
    const data = Object.values(typeCounts);

    const ctx = document.getElementById('pieChartCompteType') as HTMLCanvasElement;
    if (ctx) {
      this.pieChartCompteType = new Chart(ctx, {
        type: 'pie',
        data: {
          labels: labels,
          datasets: [
            {
              label: 'Nombre de comptes',
              data: data,
              backgroundColor: ['#36A2EB', '#FF6384'],
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'top' },
            title: {
              display: true,
              text: 'Répartition des comptes par type',
            },
          },
        },
      });
    } else {
      console.error("Element with ID 'pieChartCompteType' not found.");
    }
  }

  createBarChartCompteSolde(comptes: Compte[]): void {
    const sortedComptes = comptes.sort((a, b) => b.solde - a.solde).slice(0, 10);

    const labels = sortedComptes.map((compte) => compte.numeroCompte);
    const data = sortedComptes.map((compte) => compte.solde);

    const ctx = document.getElementById('barChartCompteSolde') as HTMLCanvasElement;
    if (ctx) {
      this.barChartCompteSolde = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [
            {
              label: 'Solde',
              data: data,
              backgroundColor: '#4BC0C0',
            },
          ],
        },
        options: {
          responsive: true,
          plugins: {
            legend: { display: false },
            title: {
              display: true,
              text: 'Top 10 des comptes avec le solde le plus élevé',
            },
          },
          scales: {
            x: { title: { display: true, text: 'Numéro de compte' } },
            y: { title: { display: true, text: 'Solde (€)' } },
          },
        },
      });
    } else {
      console.error("Element with ID 'barChartCompteSolde' not found.");
    }
  }
}
