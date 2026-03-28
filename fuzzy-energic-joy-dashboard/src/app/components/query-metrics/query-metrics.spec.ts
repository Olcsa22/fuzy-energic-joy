import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { QueryMetricsComponent } from './query-metrics';

describe('QueryMetricsComponent', () => {
  let component: QueryMetricsComponent;
  let fixture: ComponentFixture<QueryMetricsComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QueryMetricsComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(QueryMetricsComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load sensors on init', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:8080/sensors');
    req.flush([{ id: 1, cityName: 'Budapest', countryName: 'Hungary' }]);

    expect(component.sensors().length).toBe(1);
  });

  it('should toggle sensor selection', () => {
    component.toggleSensor(1);
    expect(component.isSensorSelected(1)).toBeTruthy();

    component.toggleSensor(1);
    expect(component.isSensorSelected(1)).toBeFalsy();
  });

  it('should not query without metrics selected', () => {
    component.includeTemperature = false;
    component.includeHumidity = false;

    fixture.detectChanges();
    const req = httpMock.expectOne('http://localhost:8080/sensors');
    req.flush([]);

    component.query();
    expect(component.error()).toBe('Select at least one metric');
  });
});