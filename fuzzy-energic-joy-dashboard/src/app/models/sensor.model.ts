export interface Sensor {
  id: number;
  cityName: string;
  countryName: string;
}

export interface MetricDetail {
  id: number;
  sensorId: number;
  temperature: number | null;
  humidity: number | null;
  createdOn: string;
}

export interface MetricsQuery {
  averageTemperature: number | null;
  averageHumidity: number | null;
}

export interface ErrorResponse {
  message: string;
}
