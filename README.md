# Progetto di Ingegneria del Software 2018 - Sagrada

## Membri del gruppo

- Poiani Riccardo
    - Matricola: 846379
    - Codice persona: 10533902
- Tibaldi Mattia
    - Matricola: 843840
    - Codice persona: 10521778
- Zhou Tang-tang
    - Matricola: 847685
    - Codice persona: 10540904

## Coverage dei test

Copertura dei test di tutte le classi: 
![Copertura di tutte le classi](/images/coverage_all_packages.png)

Copertura dei test (escluso CLI, GUI e rete):
![Copertura con escluso rete, gui e client](/images/coverage_exclude_client_and_network.png)

## Diagramma UML

Tutte le immagini si trovano nella seguente cartella: [UML](/images/uml/), invece il primo diagramma UML 
si trova in questa cartella: [OLD UML](/images/old_uml/)

- Package principale:

![Package principale del progetto](/images/uml/class__package.jpg)

- Lobby:

![Package della lobby](/images/uml/class__lobby__lobby.jpg)

- Network:

![Package della network](/images/uml/class__network__network.jpg)
    1. Observers:
    ![Package di tutti gli observer nel progetto](/images/uml/class__observers__observers.jpg)
    2. Protocol:
    ![Package del protocollo di comunicazione di rete](/images/uml/class__protocol__protocol.jpg)

- CLI:

![Package della CLI](/images/uml/class__cli__cli.jpg)

- GUI:

![Package della GUI](/images/uml/class__graphics__graphics.jpg)
    1. Controller:
    ![Package dei controller della GUI](/images/uml/class__controller__controller2.jpg)
    2. Listeners:
    ![Package dei listener della GUI](/images/uml/class__listener__listener.jpg)
    3. Component:
    ![Package dei component della GUI](/images/uml/class__component__component.jpg)
    
- Game:

![Package del Game](/images/uml/class__game__game.jpg)
    1. Model:
    ![Package del modello del Game](/images/uml/class__model__model.jpg)
        - Cards (il package all'interno del modello):
        ![Package delle cards](/images/uml/class__cards__cards.jpg)
            - ToolCards (il package all'interno delle cards):
            ![Package delle tool cards](/images/uml/class__toolcards__toolcards.jpg)
                - Commands (il package all'interno delle toolcards):
                ![Package dei commands delle toolCards](/images/uml/class__commands__commands.jpg)
            - ObjectiveCards (il package all'interno delle cards):
            ![Package delle objectiveCards](/images/uml/class__objectivecards__objectivecards.jpg)
        - States (il package all'interno del modello):
        ![Package degli state del Game](/images/uml/class__state__state.jpg)

## Funzionalità implementate

- Regole complete
- CLI
- GUI
- RMI
- Socket
- Single Player
- Multi partita

## Spiegazioni aggiuntive

- Limitazione: il progetto sviluppato non consente lo sviluppo di client che non siano scritti in Java, in quanto il 
controller richiede delle interfacce, che risultano essere oggetti concreti lato client serializzabili, come parametri 
di alcuni metodi. Questi oggetti sono principalmente observer del modello, fatta eccezione per le classi view, 
attraverso le quali il server manda semplici stringhe, di errore o di acknowledgement, ai client.   
In ogni caso, lo scambio di questi oggetti è stato limitato alle fasi di connessione, riconessione e "binding" fra gli 
observer e gli oggetti osservati. 

- Nella CLI dal terminale di Windows, l'inserimento di alcuni valori da tastiera non viene mostrato immediatamente ma solo
dopo il tasto invio. Per evitare questo problema su Windows, abbiamo utilizzato un altro terminale chiamato Cygwin. Mentre
su MacOS funziona correttamente

- Per tutta la comunicazione di rete esclusa dal punto precedente, è stato creato un protocollo, basato su JSON, 
per lo scambio di informazioni. 

- Nel progetto la parte riguardante la rete è stata decouplata al meglio delle nostre possibilità dal modello. Al fine
di fare ciò, sono stati creati delle classi (FakeObserver) che contengono al loro interno la gestione delle eccezioni
dovute alla comunicazione. 

- La parte di rete, per quanto riguarda RMI e socket, è stata gestita al piu' possibile in maniera uguale, in modo 
da ricreare lo stesso comportamento e sfruttare la reusability del codice. Per quanto riguarda i socket è stato usato 
la reflection usando la classe Proxy che praticamente è un modo veloce per scrivere un oggetto del Proxy Pattern.

- Tutti i messaggi che il server invia ai client vengono inviati all'interno di un thread creato appositamente,
in quanto, se la connessione veniva stabilita con RMI risultava essere bloccante. Tutti i messaggi che il server
invia ai client, di conseguenza, sono stati gestiti in una coda di thread (una per ogni client), con un massimo di un
thread attivo, in modo da assicurare che tutti i messaggi arrivino nel corretto ordine.

- Le tool card sono state realizzate tramite dei comandi, che svolgono micro funzionalità individuate in fase di 
progettazione. La lista di comandi, come il resto delle informazioni riguardanti le carte, vengono caricate da 
file JSON. Nel file riguardanti le carte utensili, la stringa action specifica i vari
commands associati alla tool card che verranno eseguiti. Action è scritta in un linguaggio creato da noi con la seguente
sintassi: [x-nome_comando], dove nome_comando è il nome del comando all'interno di una grammatica (dichiarata nel codice
all'interno del ToolCardLanguageParser)e x è un numero intero positivo che specifica la posizioni di esecuzione del 
comando all'interno di un albero di esecuzione binario. Avere un albero di esecuzione binario consente di distringuere
flussi di esecuzioni a seconda di scelte dell'utente o condizioni di vario tipo.

## CLIENT JAR

- [Client App](/jars/client) oppure link al [zip](https://github.com/tangtang95/ing-sw-2018-poiani-tibaldi-zhou/releases/) 

Requisiti:
 - Cartella resources presente con 1 file json:
    - clientSettings.json: questo file contiene l'ip del server, la porta RMI e la porta SOCKET del server
    
Per avviare l'app:
``
java -jar sagrada_client.jar
``

## SERVER JAR

- [Server App](/jars/server) oppure link al [zip](https://github.com/tangtang95/ing-sw-2018-poiani-tibaldi-zhou/releases/)

Requisiti:
 - WebServer attivo: ``java -cp nanohtt-webserver-2.3.2-snap.jar org.nanohttpd.webserver.SimpleWebServer --dir jars/server/``
 - RMI attivo: ``rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false``
 - Cartella resources presente con 5 file json:
    - privateObjectiveCards.json
    - publicObjectiveCards.json
    - schemaCards.json
    - settings.json: contiene tutte le impostazioni di timeout, porta RMI e porta SOCKET
    - toolCards.json
    
Dopo aver fatto partire il WebServer con nanohttpd e rmiregistry, per avviare l'app:
``
java -Djava.rmi.server.codebase=http://localhost:8080/sagrada_server.jar -jar sagrada_server.jar 
``





