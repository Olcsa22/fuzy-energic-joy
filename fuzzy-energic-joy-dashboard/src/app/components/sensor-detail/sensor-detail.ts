import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { SensorService } from '../../services/sensor.service';
import { Sensor, MetricDetail } from '../../models/sensor.model';

@Component({
  selector: 'app-sensor-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './sensor-detail.html',
  styleUrl: './sensor-detail.css'
})
export class SensorDetailComponent implements OnInit {
  sensor = signal<Sensor | null>(null);
  metrics = signal<MetricDetail[]>([]);
  loading = signal(true);
  error = signal('');

  constructor(
    private route: ActivatedRoute,
    private sensorService: SensorService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadSensor(id);
    this.loadMetrics(id);
  }

  loadSensor(id: number): void {
    this.sensorService.getSensor(id).subscribe({
      next: (sensor) => this.sensor.set(sensor),
      error: () => this.error.set('Failed to load sensor')
    });
  }

  loadMetrics(id: number): void {
    this.sensorService.getSensorMetrics(id).subscribe({
      next: (metrics) => {
        this.metrics.set(metrics);
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Failed to load metrics');
        this.loading.set(false);
      }
    });
  }
}
