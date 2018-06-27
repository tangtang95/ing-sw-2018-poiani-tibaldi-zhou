# Progetto di Ingegneria del Software 2018 - Sagrada

## Membri del gruppo

- Poiani Riccardo
    - Matricola: 
    - Codice persona:
- Tibaldi Mattia
    - Matricola: 843840
    - Codice persona: 10521778
- Zhou Tang-tang
    - Matricola: 847685
    - Codice persona: 10540904

## Coverage dei test

## Diagramma UML

Link alla [cartella uml]()

## Funzionalità implementate

- Single Player
- Multi partita

## Spiegazioni aggiuntive

- TODO


### How to run RMI Server and Client
Requirements: [nanohttpd-webserver-2.3.2-snap.jar](https://github.com/affo/ingsoft-project/blob/master/rmi/warehouse/nanohttpd-webserver-2.3.2-snap.jar)
1. Compile the project with ```mvn compile``` from terminal or Intellij IDEA Maven executor
2. Start the web server:
```bash
# pwd=ing-sw-2018-poiani-tibaldi-zhou/
# jarfilepwd=ing-sw-2018-poiani-tibaldi-zhou/

#Unix
java -cp nanohttpd-webserver-2.3.2-snap.jar org.nanohttpd.webserver.SimpleWebServer --dir ./target/classes/
```
3. Start the rmiregistry:
```bash
#Unix
rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false

#Windows
start rmiregistry -J-Djava.rmi.server.useCodebaseOnly=false
```
4. Launch server:
```bash
#pwd=ing-sw-2018-poiani-tibaldi-zhou/target/classes/

#Unix
java -Djava.rmi.server.useCodebaOnly=false -Djava.rmi.server.logCalls=true -Djava.rmi.server.codebase=http://localhost:8080/ -cp . org.poianitibaldizhou.sagrada.ServerApp
```
It's also possible to launch the server from Intellij IDEA:
- goto Run -> Edit Configurations... 
- add a new configuration of the Server class and in the VM options add ```-Djava.rmi.server.logCalls=true -Djava.rmi.server.codebase=http://localhost:8080/```
- run the configuration (there is no necessity to compile the project)
5. Launch client:
```bash
#pwd=ing-sw-2018-poiani-tibaldi-zhou/target/classes/

#Unix
java -Djava.rmi.server.useCodebaOnly=false -Djava.rmi.server.logCalls=true -Djava.rmi.server.codebase=http://localhost:8080/ -cp . org.poianitibaldizhou.sagrada.ClientApp
```
It's also possible to launch the client from Intellij IDEA:
- goto Run -> Edit Configurations... 
- add a new configuration of the Client class and in the VM options add ```-Djava.rmi.server.logCalls=true -Djava.rmi.server.codebase=http://localhost:8080/```
- run the configuration (there is no necessity to compile the project)
