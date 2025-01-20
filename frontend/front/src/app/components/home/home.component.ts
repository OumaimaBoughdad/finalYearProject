import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../services/client.service';
import { EmployeeService } from '../../services/employe.service';
import { TransactionService } from '../../services/transaction.service';
import { CompteService } from '../../services/compte.service';
import { DashboardComponent } from '../../Dashboards/dashboard/dashboard.component'; // Importez le DashboardComponent

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports:[DashboardComponent],
  standalone: true
})
export class HomeComponent implements OnInit {
  totalClients: number = 0;
  totalEmployees: number = 0;
  totalTransactions: number = 0;
  totalAccounts: number = 0;

  constructor(
    private clientService: ClientService,
    private employeeService: EmployeeService,
    private transactionService: TransactionService,
    private accountService: CompteService,
  ) {}

  ngOnInit(): void {
    this.loadMetrics();
  }

  loadMetrics(): void {
    // Exemple de récupération des données
    this.clientService.getTotalClients().subscribe((total) => {
      this.totalClients = total;
    });

    this.employeeService.getTotalEmployees().subscribe((total) => {
      this.totalEmployees = total;
    });

    this.transactionService.getTotalTransactions().subscribe((total) => {
      this.totalTransactions = total;
    });

    this.accountService.getTotalAccounts().subscribe((total) => {
      this.totalAccounts = total;
    });
  }
}
