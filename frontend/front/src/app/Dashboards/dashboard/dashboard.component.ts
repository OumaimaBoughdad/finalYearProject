import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../services/transaction.service';
import { Transaction } from '../../models/transaction.model';
import Chart from 'chart.js/auto';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  standalone: true,
  imports:[CommonModule,FormsModule],
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  public pieChart: any; // Graphique en camembert (type)
  public lineChartDate: any; // Graphique en lignes (date)

  public stackedBarChartCompte: any; // Graphique en barres empilées (compte)

  totalTransactions: number = 0; // Nombre total de transactions
  showCount: boolean = true; // Afficher le nombre de transactions
  showAmount: boolean = true; // Afficher le montant des transactions
  selectedDate: string = new Date().toISOString().split('T')[0]; // Date sélectionnée (aujourd'hui par défaut)

  constructor(private transactionService: TransactionService) {}

  ngOnInit(): void {
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.transactionService.getAllTransactions().subscribe(data => {
      this.totalTransactions = data.length; // Nombre total de transactions
      this.createPieChart(data); // Camembert (type)
      this.createLineChartDate(data); // Lignes (date)

      this.updateStackedBarChartCompte(data); // Barres empilées (compte)
    });
  }

  // 1. Camembert : Répartition des transactions par type
  createPieChart(transactions: Transaction[]): void {
    const typeCounts = transactions.reduce((acc, transaction) => {
      acc[transaction.typeTransaction] = (acc[transaction.typeTransaction] || 0) + 1;
      return acc;
    }, {} as { [key: string]: number });

    const labels = Object.keys(typeCounts);
    const data = Object.values(typeCounts);

    const ctx = document.getElementById("pieChart") as HTMLCanvasElement;
    if (ctx) {
      this.pieChart = new Chart(ctx, {
        type: 'pie',
        data: {
          labels: labels,
          datasets: [{
            label: "Nombre de transactions",
            data: data,
            backgroundColor: ['#2ec130', '#e6be59', '#6774ac'],
          }]
        },
        options: {
          responsive: true,
          plugins: {
            legend: { position: 'top' },
            title: {
              display: true,
              text: 'Répartition des transactions par type'
            }
          }
        }
      });
    } else {
      console.error("Element with ID 'pieChart' not found.");
    }
  }

  // 2. Lignes : Évolution des transactions par date (nombre et montant)
  createLineChartDate(transactions: Transaction[]): void {
    const dateData = transactions.reduce((acc, transaction) => {
      const date = new Date(transaction.dateTransaction).toLocaleDateString();
      if (!acc[date]) {
        acc[date] = { count: 0, amount: 0 };
      }
      acc[date].count += 1; // Nombre de transactions
      acc[date].amount += transaction.montant; // Montant total des transactions
      return acc;
    }, {} as { [key: string]: { count: number; amount: number } });

    const labels = Object.keys(dateData);
    const countData = labels.map(date => dateData[date].count);
    const amountData = labels.map(date => dateData[date].amount);

    const ctx = document.getElementById("lineChartDate") as HTMLCanvasElement;
    if (ctx) {
      this.lineChartDate = new Chart(ctx, {
        type: 'line',
        data: {
          labels: labels,
          datasets: [
            {
              label: "Nombre de transactions",
              data: countData,
              borderColor: '#2ec130',
              fill: false,
              hidden: !this.showCount // Masquer ou afficher en fonction de showCount
            },
            {
              label: "Montant total des transactions",
              data: amountData,
              borderColor: '#6774ac',
              fill: false,
              hidden: !this.showAmount // Masquer ou afficher en fonction de showAmount
            }
          ]
        },
        options: {
          responsive: true,
          plugins: {
            legend: { display: true },
            title: {
              display: true,
              text: 'Évolution des transactions par date'
            }
          },
          scales: {
            x: { title: { display: true, text: 'Date' } },
            y: { title: { display: true, text: 'Valeur' } }
          }
        }
      });
    } else {
      console.error("Element with ID 'lineChartDate' not found.");
    }
  }



  // 5. Barres empilées : Transactions par compte (nombre et montant) pour une date spécifique
  updateStackedBarChartCompte(transactions: Transaction[]): void {
    const filteredTransactions = transactions.filter(transaction => {
      const transactionDate = new Date(transaction.dateTransaction).toISOString().split('T')[0];
      return transactionDate === this.selectedDate;
    });

    const compteData = filteredTransactions.reduce((acc, transaction) => {
      const compteId = transaction.comptes[0]?.idCompte || 'Inconnu';
      if (!acc[compteId]) {
        acc[compteId] = { count: 0, amount: 0 };
      }
      acc[compteId].count += 1; // Nombre de transactions
      acc[compteId].amount += transaction.montant; // Montant total des transactions
      return acc;
    }, {} as { [key: string]: { count: number; amount: number } });

    const labels = Object.keys(compteData);
    const countData = labels.map(compteId => compteData[compteId].count);
    const amountData = labels.map(compteId => compteData[compteId].amount);

    const ctx = document.getElementById("stackedBarChartCompte") as HTMLCanvasElement;
    if (ctx) {
      if (this.stackedBarChartCompte) {
        this.stackedBarChartCompte.destroy(); // Détruire le graphique existant
      }
      this.stackedBarChartCompte = new Chart(ctx, {
        type: 'bar',
        data: {
          labels: labels,
          datasets: [
            {
              label: "Nombre de transactions",
              data: countData,
              backgroundColor: '#2ec130'
            },
            {
              label: "Montant total des transactions",
              data: amountData,
              backgroundColor: '#6774ac'
            }
          ]
        },
        options: {
          responsive: true,
          plugins: {
            legend: { display: true },
            title: {
              display: true,
              text: `Transactions par compte le ${this.selectedDate}`
            }
          },
          scales: {
            x: { title: { display: true, text: 'Compte' } },
            y: { title: { display: true, text: 'Valeur' } }
          }
        }
      });
    } else {
      console.error("Element with ID 'stackedBarChartCompte' not found.");
    }
  }

  // Méthode pour mettre à jour l'affichage des lignes
  toggleLineChartVisibility(): void {
    if (this.lineChartDate) {
      this.lineChartDate.data.datasets[0].hidden = !this.showCount; // Nombre de transactions
      this.lineChartDate.data.datasets[1].hidden = !this.showAmount; // Montant des transactions
      this.lineChartDate.update(); // Mettre à jour le graphique
    }
  }

  // Méthode pour mettre à jour le graphique des transactions par compte
  onDateChange(): void {
    this.transactionService.getAllTransactions().subscribe(data => {
      this.updateStackedBarChartCompte(data); // Mettre à jour le graphique avec la nouvelle date
    });
  }
}
