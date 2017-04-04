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