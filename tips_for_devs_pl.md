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

gdzie `%params%` to `-m`, Opis zmiany(typ, stan #1234), koniecznie ze spacją.

* typ:
 - bugfix
 - feature

* stan:
 - involves
 - resolves

# Plik issues

/Przeniesione na GitHub/

## Schemat wiadomości Server-Client:
```javascript
{ "header": <nagłówek>, "data1": "value1", "data2": "value2", ... }
```

1. Logowanie/rejestracja:
	- logowanie:
		* Klient:
		```javascript
		{ "header" : "login", "uname" : "admin", "upass" : "123", "locale" : "pl_PL" }
		```
		* Serwer:
			* W przypadku sukcesu:
			```javascript
			{ "login" : "true" }
			```
			* Natomiast w przypadku niepowodzenia np tak:
			```javascript
			{ "login" : "false", "alert" : "Wrong password!" }		
			```
	- rejestracja:
		* Klient:
		```javascript
		{ "header" : "register", "uname" : "admin", "upass": "123", "backup_code" : "1234", "email" : "janusz@hackers.pl", "locale" : "pl_PL" }
		```
		* Serwer:
			* W przypadku sukcesu:
			```javascript
			{ "register" : "true" }
			```
			* Natomiast w przypadku niepowodzenia np tak:
			```javascript
			{ "register" : "false", "alert" : "E-mail is already in use" }		
			```
2. Lobby:
	- stworzenie:	
		* Klient:
		```javascript
		{ "header" : "lcreate" }
		```
		* Serwer:
		```javascript
		{ "enterCode" : 303 }
		```
		
	- dołączenie:
		* Klient:
		```javascript
		{ "header" : "ljoin", "enterCode" : 102 }
		```
		* Serwer:
		```javascript
		{ "ljoin" : "true"/"false" }
		```
		
	- lista graczy: (odświeżana automatycznie przez serwer)
		* Serwer:
		```javascript
		{ "llist" : [{ "name" : "PlayerTaki" }, { "name", "PlayerŚmaki" }, ...] } 
		```