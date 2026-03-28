import { Routes } from '@angular/router';
import { SensorListComponent } from './components/sensor-list/sensor-list';
import { SensorDetailComponent } from './components/sensor-detail/sensor-detail';
import { SensorRegisterComponent } from './components/sensor-register/sensor-register';
import { QueryMetricsComponent } from './components/query-metrics/query-metrics';

export const routes: Routes = [
  { path: '', redirectTo: '/sensors', pathMatch: 'full' },
  { path: 'sensors', component: SensorListComponent },
  { path: 'sensors/:id', component: SensorDetailComponent },
  { path: 'register', component: SensorRegisterComponent },
  { path: 'query', component: QueryMetricsComponent },
];
