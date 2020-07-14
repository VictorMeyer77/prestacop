# PRESTACOP PROJECT
### 4 IABD Baptiste Fauvert, Amine Faiz, Victor Meyer

Simulation de communication entre des drônes et la NYPD.

#### Important

L'infrastructure du projet est dockerisée, ainsi que la simulation des drônes et
le gestionnaire de records. 
Mais le gestionnaire des alertes et le module de statistiques n'ont pas marché sur docker
pour cause de problème de mémoire.
Vous pouvez activer la dockeristaion en décommentant les lignes du docker-compose.yaml

Nous ne garantissons pas un bon fonctionement de ces composants car nous n'avons pas pu les tester.


#### Prérequis

1) Renommer tous les "debian" par le nom de votre
machine dans votre réseau:

 * drones/conf/configuration.json -> kafkaHost
 * alertManager/conf/configuration.json -> kafkaHost
 * nypdCsvReader/conf/configuration.json -> kafkaHost
 * recordManager/conf/configuration.json -> kafkaHost, MongoUrl
 * recordManager/Dockerfile -> SPARK_MASTER_NAME
 * docker-compose.yml -> *

2) Compiler les jar
    
    
    cd recordManager
    
    sbt clean assembly
    
    cd alertManager
        
    sbt clean assembly
    
    cd statistics
    
    sbt clean assembly

#### Configuration simulation

Pour modifier le nombre de drones:

 * drones/conf/configuration.json -> nbDrones
 
#### Lancement

    sudo docker-compose up
    
    # lancement gestionnaire alertes
    spark-submit --class com.prestacop.project.Main alertManager/target/scala-2.11/alertManager-assembly-0.1.jar alertManager/conf/configuration.json
    
    # lancement statistiques
    spark-submit --class com.prestacop.project.Main statistics/target/scala-2.11/statistics-assembly-0.1.jar
    
#### Monitoring spark

http://localhost:8080/

#### Accès à MongoDB

Si vous avez mongodb en local

    mongo

sinon

    sudo docker exec -it {mongo_conteneur_id} mongo

#### Insertion CSV NYPD

 * modifier csvPath de nypdCsvReader/conf/configuration.json
 
    cd nypdCsvReader
    
    sbt "run conf/configuration.json"