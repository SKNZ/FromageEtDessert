# FromageEtDessert

Réalisé par Thomas MUNOZ et Floran NARENJI-SHESHKALANI

FED

Le code des Phases 0 à 3 est dans le dossier homonyme.
Le dossier "final" comporte notre chaine de datamining (qui comporte les phases 0 à 3 mais adaptées et améliorées pour répondre aux besoins de la version Web).
Le dossier "frontend" contient la partie statique du site web.
Le dossier "backend" contient la partie dynamique du site web. Elle est codée avec Slimframework et requiert l'activation de l'url rewriting.
Le site utilise une base de données qui est configurée sur un de nos serveurs dédiés personnels. Les accès (aussi disponibles dans le dossier final ou dans le dossier backend) sont les suivants: mysql;srv0.sknz.info;fed;fedup

Visible en ligne à l'adresse: http://fed.munoz.ovh

## Comment l'utiliser ? 
Il suffit de vous rendre à cette adresse http://fed.munoz.ovh et de selectionner vos données en entrée (recherche twitter, fichier csv, etc.), de paramétrer minconf, minfreq et le lift et de lancer le mining. 

L'application vous indiquera alors l'avancée de la tâche en cours et la liste des autres opérations effectuées.

En cliquant sur une opération terminée, vous pourrez visualiser le résultat sous la forme d'un nuage de tag (plus le mot est retrouvé et plus il est grand) ou un tableau (où vous pourrez trier les résultats, y effectuer des recherches, etc.)

## Attention

Sans savoir pourquoi, il arrive que lorsque l'on capture en mode Twitter stream (live), le nombre de motifs fréquents soit très grands, entrainant un calcul des règles d'associations très long et et avec de nombreux résultats.
