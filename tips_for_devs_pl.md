# Typowy przebieg pracy w git
Każda zmiana powinna być wprowadzona na osobnej gałęzi. Schemat pracy wygląda następująco:
```git
git pull rebase[A[D[D[D[D[D[D[D[D[D[Dsh[B[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D
git [A[C[C[C[C[C[C[D--[2~rebase[B[D[D[D[D[D[D[D[D[D[D[D[D[Dcheckout -b issue#1234
```[A[A[A[C[C[B[B[B
Po wprowadzeniu zmian:
```sh
git stash
git pull --rebase
git 
[A[C[A[A[C[C[C[C[C[C[C[C[C[C[D[D
[C[C[C[C[C[C[C[C[C[C[C[C[C[Ccheckout master[B[C[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[Cgit pull rebase[D[D[D[D[D[D[D[D[D[D[[C[C[C[C[C[C[C[C[C[C[C[C[C[C[D[D[D[D--rebase
git [Apull --rebase[3~[C[C[[B[D[D[D[D[D[D[D[D[D[D[D[D[Dcheckout issue#1234
git rebase master
```[A[A[A[A[A[A[C[A[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[ należy przenieść HEAD gałęzi do bieżących zmian:[B[B[B[B[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[D[B[D[D[B[D[D[B[B
Następnie rozpocząć aktualizację repozytorium poprzez
```sh
git pop
git commit %params%
git push
```
gdzie params[1;5D[1;5D[1;5D[1;5D[1;5D[1;5D%params%[D[D[D[D[D[D[D[D[D[C`%params%` to `-m`, Opis zmiany(typ, [D[D[D[C, stan#1234), np:
`-m "Added[A[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[C[Cgdzie:
typ[C[C[C[C[C:
[A* typ:
 - bugfix
 -feature
* stan:
 - involves
 - resolves

# Plik issues
