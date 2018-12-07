import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from "@angular/common/http";
import { DoelLocatie } from 'src/app/classes/DoelLocatie';

@Injectable()
export class DataService {

  private url:string = "http://84.197.96.121/api/citycheck/";

  private chosenLoc:ILocRoot;


  constructor(private _http:HttpClient) {

   }

   //Ophallen en setten van een gekozen locatie om te gaan bewerken
   public setChosenLoc(loc:ILocRoot){
      this.chosenLoc = loc;
   }


   public getChosenLoc(){
      return this.chosenLoc;
   }
   

   //locaties ophalen
   getLocations(page:number, naam?:string): Observable<ILocRoot[]> {

    if(naam != null){
      //we zoeken op naam
      return this._http.get<ILocRoot[]>(this.url+"allDoelLocs?naam="+naam)
    }

    return this._http.get<ILocRoot[]>(this.url+"allDoelLocs?page="+page)
    
  }


  //nieuwe locatie posten
  postLocation(loc:DoelLocatie): Observable<DoelLocatie> {
    console.log(loc);

    return this._http.post<DoelLocatie>(this.url+"addDoelLocs",loc)
    
  }


  //locatie verwijderen
  delLocation(id:number): Observable<string> {
    return this._http.delete<string>(this.url+"delDoelLocs/"+id)
    
  }


  //locatie editen
  editLocation(id:number, loc:ILocRoot): Observable<ILocRoot> {
    return this._http.put<ILocRoot>(this.url+"editDoelLocs/"+id, loc)
    
  }


  //Vragen bij een locatie ophalen




  //Vraag bij een locatie toevoegen





//   createAthlete(vnaam:string,anaam:string,natio:string,leeft:number,aantala:number,sportid:number, key:string){
//     return this._http.post(this.mainUrl+`athletes?key=`+key,{
//       "voornaam": vnaam,
//       "achternaam": anaam,
//       "nationaliteit": natio,
//       "leeftijd": leeft,
//       "aantalAwards": aantala,
//       "SportId": sportid
//   })
//   }




}


//interfaces


export interface ILocRoot {
    id: number;
    titel: string;
    locatie: Locatie;
    vragen?: any;
  }
  
 export interface Locatie {
    id: number;
    lat: number;
    long: number;
  }

