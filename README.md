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

Tutte le immagini si trovano nella seguente cartella: [UML](/images/uml/)

- [Package principale](/images/uml/class__package.jpg):
![Package principale del progetto](/images/uml/class__package.jpg)

- [Lobby](/images/uml/class__lobby__lobby.jpg):
![Package della lobby](/images/uml/class__lobby__lobby.jpg)

- Network:
![Package della network](/images/uml/class__network__network.jpg)
    - Observers:
    ![Package di tutti gli observer nel progetto](/images/uml/class__observers__observers.jpg)
    - Protocol:
    ![Package del protocollo di comunicazione di rete](/images/uml/class__protocol__protocol.jpg)

- CLI:
![Package della CLI](/images/uml/class__cli__cli.jpg)

- GUI:
![Package della GUI](/images/uml/class__graphics__graphics.jpg)
    - Controller:
    ![Package dei controller della GUI](/images/uml/class__controller__controller2.jpg)
    - Listeners:
    ![Package dei listener della GUI](/images/uml/class__listener__listener.jpg)
    - Component:
    ![Package dei component della GUI](/images/uml/class__component__component.jpg)
    
- Game:
![Package del Game](/images/uml/class__game__game.jpg)
    - Model:
    ![Package del modello del Game](/images/uml/class__model__model.jpg)
        - Cards:
        ![Package delle cards](/images/uml/class__cards__cards.jpg)
            - ToolCards:
            ![Package delle tool cards](/images/uml/class__toolcards__toolcards.jpg)
                - Commands:
                ![Package dei commands delle toolCards](/images/uml/class__commands__commands.jpg)
            - ObjectiveCards:
            ![Package delle objectiveCards](/images/uml/class__objectivecards__objectivecards.jpg)
        - States:
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

- Per tutta la comunicazione di rete esclusa dal punto precedente, è stato creato un protocollo, basato su JSON, 
per lo scambio di informazioni. 

- Nel progetto la parte riguardante la rete è stata decouplata al meglio delle nostre possibilità dal modello. Al fine
di fare ciò, sono stati creati delle classi (FakeObserver) che contengono al loro interno la gestione delle eccezioni
dovute alla comunicazione. 

- La parte di rete, per quanto riguarda RMI e socket, è stata gestita al piu' possibile in maniera uguale, in modo 
da ricreare lo stesso comportamento e sfruttare la reusability del codice. Per quanto riguarda i socket... (inserire
qui discroso sulla reflection)

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




