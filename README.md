# PRESTACOP PROJECT
### 4 IABD Baptiste Fauvert, Amine Faiz, Victor Meyer

Simulation de communication entre des drônes et la NYPD.

L'intégralié du projet est dockerisé

#### Prérequis

1) Renommer tous les "debian" par le nom de votre
machine dans votre réseau:

 * drones/conf/configuration.json -> kafkaHost
 * alertManager/conf/configuration.json -> kafkaHost
 * recordManager/conf/configuration.json -> kafkaHost, MongoUrl
 * recordManager/Dockerfile -> SPARK_MASTER_NAME
 * docker-compose.yml -> *

2) Compiler le jar du recordManager et de l'alertManager
    
    
    cd recordManager
    
    sbt clean assembly
    
    cd alertManager
        
    sbt clean assembly


#### Temporaire

Tant que alertManager n'est pas dockerisé: 
adapter chemin du spark-submit dans lanch.sh

#### Configuration simulation

Pour modifier le nombre de drones:

 * drones/conf/configuration.json -> nbDrones
 
#### Lancement

    sudo sh launch.sh

#### Monitoring spark

http://localhost:8080/