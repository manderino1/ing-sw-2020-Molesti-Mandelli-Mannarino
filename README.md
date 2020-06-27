# Progetto ing-sw
## Componenti del gruppo:
 - Alessandro Molesti (10581232)
 - Ottavio Mannarino (10578486)
 - Marco Mandelli (10561394)
## Funzionalitá realizzate:
 - Regole Complete
 - CLI
 - GUI
 - Persistenza
 - Partite Multiple
## Istruzioni per avviare il gioco
Versione di Java utilizzata: **Java 11**
### Per avviare il server:
Su windows é sufficiente doppio click sul jar, per chiuderlo é poi necessario farlo da gestione attivitá
In alternativa

    java -jar server.jar
Utilizzare ctrl+c per terminare il processo
### Per avviare il client:
Per avviare la gui é sufficiente fare doppio click sul jar
Il gioco é testato solo su windows ma sono incluse le dipendenze per tutti i sistemi operativi (mac, windows, linux). Non dovrebbero quindi esserci problemi di funzionamento su sistemi operativi differenti.
In alternativa:

    java -jar client.jar
Per avviare la cli utilizzare il seguente comando da terminale. Per visualizzare correttamente l'output su windows, utilizzare windows terminal:

    java -jar client.jar cli

## Istruzioni per ripristinare un backup
Una volta riconnessi tutti i client che stavano partecipando alla partita al server, inserire gli stessi nickname che si stavano utilizzando, la partita verrá ripresa dal punto in cui era stata interrotta.
I backup vengono salvati nella cartella backup all'interno della stessa directory in cui é presente il jar del server.