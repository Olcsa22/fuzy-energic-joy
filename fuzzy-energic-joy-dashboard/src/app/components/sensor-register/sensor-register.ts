import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SensorService } from '../../services/sensor.service';

@Component({
  selector: 'app-sensor-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './sensor-register.html',
  styleUrl: './sensor-register.css'
})
export class SensorRegisterComponent {
  sensorId: number | null = null;
  cityName = '';
  countryName = '';
  submitting = signal(false);
  error = signal('');

  constructor(
    private sensorService: SensorService,
    private router: Router
  ) {}

  register(): void {
    if (this.sensorId === null) {
      this.error.set('Sensor ID is required');
      return;
    }
    if (!this.cityName.trim()) {
      this.error.set('City name is required');
      return;
    }
    if (!this.countryName.trim()) {
      this.error.set('Country name is required');
      return;
    }

    this.submitting.set(true);
    this.error.set('');

    this.sensorService.registerSensor({
      id: this.sensorId,
      cityName: this.cityName.trim(),
      countryName: this.countryName.trim()
    }).subscribe({
      next: (created) => {
        this.router.navigate(['/sensors', created.id]);
      },
      error: (err) => {
        this.submitting.set(false);
        this.error.set(err.error?.message || 'Registration failed');
      }
    });
  }
}
