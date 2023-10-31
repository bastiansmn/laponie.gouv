import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatIconModule} from "@angular/material/icon";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatSidenavModule} from "@angular/material/sidenav";
import { MainComponent } from './components/main/main.component';
import { AuthComponent } from './components/auth/auth.component';
import {ReactiveFormsModule} from "@angular/forms";
import {MatInputModule} from "@angular/material/input";
import {HttpClientModule} from "@angular/common/http";
import { FamilyListComponent } from './components/main/family-list/family-list.component';
import {MatListModule} from "@angular/material/list";
import { FamilyComponent } from './components/main/family/family.component';
import {MatCheckboxModule} from "@angular/material/checkbox";
import {MatTooltipModule} from "@angular/material/tooltip";
import { MarkAsGiftedComponent } from './components/main/family/mark-as-gifted/mark-as-gifted.component';
import {MatDialogModule} from "@angular/material/dialog";
import { AddUserComponent } from './components/main/family/add-user/add-user.component';
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import { AddFamilyDialogComponent } from './components/main/family-list/add-family-dialog/add-family-dialog.component';
import { AddWishComponent } from './components/main/family/add-wish/add-wish.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    MainComponent,
    AuthComponent,
    FamilyListComponent,
    FamilyComponent,
    MarkAsGiftedComponent,
    AddUserComponent,
    AddFamilyDialogComponent,
    AddWishComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatIconModule,
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    ReactiveFormsModule,
    MatInputModule,
    HttpClientModule,
    MatListModule,
    MatCheckboxModule,
    MatTooltipModule,
    MatDialogModule,
    MatAutocompleteModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
