# ing-sw-2019-20

Le funzionalità sviluppate sono
Regole Complete + CLI + GUI + Socket + RMI + 1 FA (partite multiple)

Eseguendo il goal maven package vengono generati nella cartella target i jar del client (AdrenalinaClient.jar) e del server (AdrenalinaServer.jar).
Viene generato anche un terzo file jar (ing-sw-2019-20-1.0.jar) da ignorare. 
I due jar di client e server possono essere lanciati da command line con
java -jar AdrenalinaClient.jar
java -jar AdrenalinaServer.jar

Il client richiederà come prima cosa se procedere in CLI o avviare la GUI

Il jar del client potrebbe non avviare correttamente la gui su tutte le piattaforme delle diverse libreria di javafx nei diversi sistemi operativi. Su git sono caricati 3 jar generati su windows, mac e linux, in caso di problemi è consigliabile generare il jar sul sistema operativo su cui dovrà essere avviato.

Per il corretto funzionamento di RMI occorre che il codice rilevi correttamente l'indirizzo IP locale della macchina (sia su client che su server) e questo non avviene su mac, ove il programma richiede all'utente di inserirlo manualmente. 

Per visualizzare correttamente la CLI su windows occorre che sia possibile vedere i caratteri speciali ANSII, su Windows 10 è possibile farlo con una modifica al registro:

In HKCU\Console occorre create una DWORD di nome VirtualTerminalLevel con valore 1 (e riavviare cmd.exe).
Nella root di github è caricato un file bat che provvede all'aggiunta di questa chiave.
 
I jar sono caricati qui https://drive.google.com/open?id=1zo3iv4ln4xMX42SphkUkHwFY7z2VNvzL perchè git impedisce di caricare file così grandi
