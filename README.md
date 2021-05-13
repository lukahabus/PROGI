# WorldPIIS

Project for the Software Enginnering course at FER, UNIZG, 2020/2021

NAPOMENA: testiranje promjene podataka profila mora biti zadnje, jer je sustav labilan po tom pitanju (detaljnije ispod)

Web aplikacija nalazi se na [worldpiis.duckdns.org](http://worldpiis.duckdns.org) ili na adresi [104.248.33.213](http://104.248.33.213).

Korisnici za testiranje:
* uprave - `uprava`
* koordinatora - `mhren`
* voditelja tima - `epav`
* običnog developera - `alu`

Lozinka je svima `loz`

Podaci za prijavu u GoogleCalendar su `worldpiis.fer.hr@gmail.com` i lozinka `123lozinka!`

Samo tim 100 ima neke zadatke, ostali su prazni - imali bi problem opisan ispod. 

Detaljan opis problema: kad se promijene neki detalji u opisu profila, poredak u bazi se promijeni i to utječe na način kako frontend percipira korisnike (iako nebi smjelo, konkretno greška je negdje u Jackson serijalizaciji objekata, tj. kada Java objekt prelazi u JSON zapis).

Ovo smo primijetili na korisniku `epav`, no možda se dešava i s drugim korisnicima (probali smo neke kombinacije koje nisu imale utjecaja, ali neke druge možda imaju).

Iz tog razloga preporučam testiranje funkcionalnosti uređivanja profila zadnje, jer jednom kad se slomi, slomi se do kraja - konkretno, u bazi se promijeni poredak, koji zauzvrat onemogućuje frontendu pravilan ispis zadataka. U tom slučaju, profil se može uređivati bez obzira, i zadaci se mogu dodavati, ali niti jedan zadatak niti jednom korisniku neće biti vidljiv. 

Pojednostavljeni primjer manifestacije:

Očekujemo od REST-a da nam vrati [{id: 12, username: mhren, "team" : {id: 100, name: "Tim 100"}}, {id: 13, username: epav, "team" : {id: 100, name: "Tim 100"}}], no REST nam vraća [{id: 12, username: mhren, "team" : {id: 100, name: "Tim 100"}}, {id: 13, username: epav, "team" : 10}], dakle, informacija o timu za drugog zaposlenika je nekako iz nekog razloga reducirana na ID tog tima. 
