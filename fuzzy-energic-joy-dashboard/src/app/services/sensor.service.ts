import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Sensor, MetricDetail, MetricsQuery } from '../models/sensor.model';

@Injectable({
  providedIn: 'root'
})
export class SensorService {
  private readonly apiUrl = 'http://localhost:8080/sensors';

  constructor(private http: HttpClient) {}

  getAllSensors(): Observable<Sensor[]> {
    return this.http.get<Sensor[]>(this.apiUrl);
  }

  getSensor(id: number): Observable<Sensor> {
    return this.http.get<Sensor>(`${this.apiUrl}/${id}`);
  }

  registerSensor(sensor: { id: number; cityName: string; countryName: string }): Observable<Sensor> {
    return this.http.post<Sensor>(`${this.apiUrl}/register`, sensor);
  }

  getSensorMetrics(sensorId: number): Observable<MetricDetail[]> {
    return this.http.get<MetricDetail[]>(`${this.apiUrl}/${sensorId}/metrics`);
  }

  queryMetrics(sensorIds: number[], metrics: string[], days: number): Observable<MetricsQuery> {
    let params = new HttpParams();
    if (sensorIds.length > 0) {
      sensorIds.forEach(id => params = params.append('sensorIds', id.toString()));
    }
    metrics.forEach(m => params = params.append('metrics', m));
    params = params.set('days', days.toString());
    return this.http.get<MetricsQuery>(`${this.apiUrl}/query-metrics`, { params });
  }
}
