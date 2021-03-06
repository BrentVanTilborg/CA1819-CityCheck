import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {ButtonsModule, CardsFreeModule, MDBBootstrapModule, WavesModule} from 'angular-bootstrap-md';
import { RouterModule, Routes } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
const appRoutes: Routes = [
  {
    path: 'doel',
    component: DoelComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'detail',
    component: DetailsComponent
  },
  { path: '',
    component: LoginComponent
  },
  { path: '**', component: LoginComponent }
];

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { DoelComponent } from './doel/doel.component';
import { FormsModule } from '@angular/forms';


//map
import { LeafletModule } from '@asymmetrik/ngx-leaflet';

//firebase
import { AngularFireModule } from 'angularfire2';
import { AngularFirestoreModule } from 'angularfire2/firestore';
import { AngularFireAuthModule } from 'angularfire2/auth';
import { environment } from '../environments/environment';
import { AuthService } from 'src/services/auth/auth.service';
import { DataService } from 'src/services/Data/data.service';
import { DetailsComponent } from './details/details.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DoelComponent,
    DetailsComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(
      appRoutes
      ),
    MDBBootstrapModule.forRoot(),
    FormsModule,
    WavesModule,
    CardsFreeModule,
    ButtonsModule,
    LeafletModule.forRoot(),
    AngularFireModule.initializeApp(environment.firebase),
    AngularFirestoreModule,
    AngularFireAuthModule,
    HttpClientModule
  ],
  providers: [
    AuthService,
    DataService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
