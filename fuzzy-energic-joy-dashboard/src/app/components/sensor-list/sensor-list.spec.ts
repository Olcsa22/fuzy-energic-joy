import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { SensorListComponent } from './sensor-list';

describe('SensorListComponent', () => {
  let component: SensorListComponent;
  let fixture: ComponentFixture<SensorListComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SensorListComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([])
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(SensorListComponent);
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
    req.flush([
      { id: 1, cityName: 'Budapest', countryName: 'Hungary' },
      { id: 2, cityName: 'London', countryName: 'UK' }
    ]);

    expect(component.sensors().length).toBe(2);
    expect(component.loading()).toBeFalsy();
  });

  it('should handle error when loading sensors', () => {
    fixture.detectChanges();

    const req = httpMock.expectOne('http://localhost:8080/sensors');
    req.error(new ProgressEvent('error'));

    expect(component.error()).toBeTruthy();
    expect(component.loading()).toBeFalsy();
  });
});
