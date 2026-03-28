import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { SensorService } from '../../services/sensor.service';
import { Sensor, MetricsQuery } from '../../models/sensor.model';

@Component({
  selector: 'app-query-metrics',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './query-metrics.html',
  styleUrl: './query-metrics.css'
})
export class QueryMetricsComponent implements OnInit {
  sensors = signal<Sensor[]>([]);
  selectedSensorIds: number[] = [];
  includeTemperature = true;
  includeHumidity = true;
  days = 7;
  allSensors = true;

  result = signal<MetricsQuery | null>(null);
  loading = signal(false);
  error = signal('');

  constructor(private sensorService: SensorService) {}

  ngOnInit(): void {
    this.sensorService.getAllSensors().subscribe({
      next: (sensors) => this.sensors.set(sensors),
      error: () => this.error.set('Failed to load sensors')
    });
  }

  toggleSensor(id: number): void {
    const idx = this.selectedSensorIds.indexOf(id);
    if (idx >= 0) {
      this.selectedSensorIds.splice(idx, 1);
    } else {
      this.selectedSensorIds.push(id);
    }
  }

  isSensorSelected(id: number): boolean {
    return this.selectedSensorIds.includes(id);
  }

  query(): void {
    const metrics: string[] = [];
    if (this.includeTemperature) metrics.push('TEMPERATURE');
    if (this.includeHumidity) metrics.push('HUMIDITY');

    if (metrics.length === 0) {
      this.error.set('Select at least one metric');
      return;
    }

    this.loading.set(true);
    this.error.set('');
    this.result.set(null);

    const sensorIds = this.allSensors ? [] : this.selectedSensorIds;

    this.sensorService.queryMetrics(sensorIds, metrics, this.days).subscribe({
      next: (result) => {
        this.result.set(result);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Query failed');
        this.loading.set(false);
      }
    });
  }
}
