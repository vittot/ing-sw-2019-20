# ing-sw-2019-20

Le funzionalità sviluppate sono
Regole Complete + CLI + GUI + Socket + RMI + 1 FA (partite multiple)

Eseguendo il goal maven package vengono generati nella cartella target i jar del client (AdrenalinaClient.jar) e del server (AdrenalinaServer.jar).
Viene generato anche un terzo file jar (ing-sw-2019-20-1.0.jar) da ignorare. 
I due jar di client e server possono essere lanciati da command line con
java -jar AdrenalinaClient.jar
java -jar AdrenalinaServer.jar

Il client richiederà come prima cosa se procedere in CLI o avviare la GUI

Per il corretto funzionamento di RMI occorre che il codice rilevi correttamente l'indirizzo IP locale della macchina (sia su client che su server) e questo non avviene su mac, ove il programma richiede all'utente di inserirlo manualmente
