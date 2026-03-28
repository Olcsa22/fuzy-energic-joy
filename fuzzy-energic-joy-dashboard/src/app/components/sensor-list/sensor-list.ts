import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SensorService } from '../../services/sensor.service';
import { Sensor } from '../../models/sensor.model';

@Component({
  selector: 'app-sensor-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './sensor-list.html',
  styleUrl: './sensor-list.css'
})
export class SensorListComponent implements OnInit {
  sensors = signal<Sensor[]>([]);
  loading = signal(true);
  error = signal('');

  constructor(private sensorService: SensorService) {}

  ngOnInit(): void {
    this.loadSensors();
  }

  loadSensors(): void {
    this.loading.set(true);
    this.sensorService.getAllSensors().subscribe({
      next: (sensors) => {
        this.sensors.set(sensors);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load sensors');
        this.loading.set(false);
      }
    });
  }
}
