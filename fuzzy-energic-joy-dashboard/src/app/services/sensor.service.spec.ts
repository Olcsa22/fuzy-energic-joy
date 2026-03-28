import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { SensorService } from './sensor.service';

describe('SensorService', () => {
  let service: SensorService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        SensorService
      ]
    });
    service = TestBed.inject(SensorService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get all sensors', () => {
    const mockSensors = [
      { id: 1, cityName: 'Budapest', countryName: 'Hungary' },
      { id: 2, cityName: 'London', countryName: 'UK' }
    ];

    service.getAllSensors().subscribe(sensors => {
      expect(sensors.length).toBe(2);
      expect(sensors[0].cityName).toBe('Budapest');
    });

    const req = httpMock.expectOne('http://localhost:8080/sensors');
    expect(req.request.method).toBe('GET');
    req.flush(mockSensors);
  });

  it('should get sensor by id', () => {
    const mockSensor = { id: 1, cityName: 'Budapest', countryName: 'Hungary' };

    service.getSensor(1).subscribe(sensor => {
      expect(sensor.id).toBe(1);
    });

    const req = httpMock.expectOne('http://localhost:8080/sensors/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSensor);
  });

  it('should register a sensor', () => {
    const input = { id: 3, cityName: 'Berlin', countryName: 'Germany' };
    const output = { id: 3, cityName: 'Berlin', countryName: 'Germany' };

    service.registerSensor(input).subscribe(sensor => {
      expect(sensor.id).toBe(3);
    });

    const req = httpMock.expectOne('http://localhost:8080/sensors/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(input);
    req.flush(output);
  });

  it('should get sensor metrics', () => {
    service.getSensorMetrics(1).subscribe(metrics => {
      expect(metrics.length).toBe(1);
    });

    const req = httpMock.expectOne('http://localhost:8080/sensors/1/metrics');
    expect(req.request.method).toBe('GET');
    req.flush([{ id: 1, sensorId: 1, temperature: 25.0, humidity: 60.0, createdOn: new Date().toISOString() }]);
  });

  it('should query metrics with sensor IDs', () => {
    service.queryMetrics([1, 2], ['TEMPERATURE', 'HUMIDITY'], 7).subscribe(result => {
      expect(result.averageTemperature).toBe(22.5);
    });

    const req = httpMock.expectOne(r =>
      r.url === 'http://localhost:8080/sensors/query-metrics' &&
      r.params.getAll('sensorIds')?.join(',') === '1,2' &&
      r.params.getAll('metrics')?.join(',') === 'TEMPERATURE,HUMIDITY' &&
      r.params.get('days') === '7'
    );
    expect(req.request.method).toBe('GET');
    req.flush({ averageTemperature: 22.5, averageHumidity: 55.0 });
  });

  it('should query metrics without sensor IDs for all sensors', () => {
    service.queryMetrics([], ['TEMPERATURE'], 30).subscribe(result => {
      expect(result.averageTemperature).toBe(20.0);
    });

    const req = httpMock.expectOne(r =>
      r.url === 'http://localhost:8080/sensors/query-metrics' &&
      !r.params.has('sensorIds') &&
      r.params.get('days') === '30'
    );
    expect(req.request.method).toBe('GET');
    req.flush({ averageTemperature: 20.0, averageHumidity: null });
  });
});
