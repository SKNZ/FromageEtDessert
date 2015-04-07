# FromageEtDessert

FED

## Choix dans la date

Les différents champs de la date (ajoutés à la fin de chaque enregistrement) seront paramétrables par l'utilisateur
À l'heure actuelle nous nous basons sur le daily trend, il n'est pas donc pertinent d'ajouter le mois courant, 
nous récupérons donc uniquement le jour de la semaine et l'heure.

## Interface web 

- L'interface web permet de lancer des captures sur des mots clés saisis par l'utilisateur.
- Une fois la demande effectuée, le serveur capture une série d'éléments et les traite grâce aux merveilleux
algorithmes appris en cours. 
- Lorsque tout le traitement est terminé, l'utilisateur reçoit par mail un lien pour consulter le résultat de sa recherche
avec des graphiques trop #SWAG (chart.js maybe)
- Une limitation par nombre de traitements simultanés pourrait être utile (commentaire le plus pertinent du monde) 
- À chaque lot de données traités, un nouveau traitement concernant le nouvel ensemble capturé pourrait être effectué 
(utiliser ce qui a été appris en FED (cube, cuboïdes toussa ...))
- La possibilité d'utiliser des sources autres que Twitter (txt, github, etc.)
- Suivi en temps réel d'un traitement (Javascript websocket ?) ?
- API REST en Java ? 
- Restitution format brut des résultat ? Graphiques ? TagCrowd ?