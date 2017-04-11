# Typowy przebieg pracy w git
Każda zmiana powinna być wprowadzona na osobnej gałęzi. Schemat pracy wygląda następująco:

```sh
git pull --rebase
checkout -b issue#1234
```

Po wprowadzeniu zmian:
```sh
git stash
git pull --rebase
git rebase master
```
należy przenieść HEAD gałęzi do bieżących zmian:

```sh
git pop
git commit %params%
git push
```

gdzie `%params%` to `-m`, Opis zmiany(typ, stan#1234)

typ:
 - bugfix
 - feature

* stan:
 - involves
 - resolves

# Plik issues

/Przeniesione na GitHub/

## Schemat wiadomości Server-Client:
`header&data0%data1%...
1. Logowanie/rejestracja:
	- logowanie:
		* c:
			h:login
			d:string%string //uname%upass
		* s:
			Jeżeli błąd
			h:alert
			d:string // wiadomość
			Jeżeli pomyślnie
			h:login
			d:bool //wyłącznie true
	- rejestracja:
		* c:
			- h:register
			- d:string%string%string%string // uname, upass, backup_code, email
		* s:
			- Jeżeli pomyślnie:
				* h:register
				* d:(bool)true
			- Jeżeli błąd
				* h:alert
				* d:string // wiadomość
2. Lobby:
	- stworzenie:
		* c:
			- h:lcreate
			- d:null
		* s:
			- h:lcreate
			- d:int // enterCode
	- dołączenie:
		* c:
			- h:ljoin
			- d:int //enterCode
		* s:
			- h:ljoin
			- d:bool
	- lista graczy:
		* s:
			- h:llist
			- d:string%string... // nazwy graczy 
