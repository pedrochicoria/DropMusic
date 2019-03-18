Para correr o projeto é necessário aceder à pasta Jar File,via terminal, incluida na pasta do projeto.

De seguida, na mesma pasta, é necessário correr o comando
    $  rmiregistry 7000 

Após isso, é só correr os ficheiros .Jar

1º - $ java -jar dataserver.jar  //Multicast Server
2º - $ java -jar server.jar	//RMI Server
3º - $ java -jar client.jar	//RMI Client
