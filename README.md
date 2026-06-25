# M-Motors - API Back-End (Spring Boot)

Ce dépôt contient la solution digitale Back-End développée pour la refonte du système de gestion de parc automobile et de traitement des dossiers de financement (Achat & LLD) de l'entreprise M-Motors.

## 🛠️ Stack Technique & Outils Utilisés
* **Framework Principal :** Spring Boot (Java 17)
* **Base de Données :** PostgreSQL (Hébergée sur le Cloud)
* **Gestionnaire de Dépendances :** Maven
* **Déploiement & Hébergement :** Render Cloud Services (Opération "Move to Cloud")

---

## 🌿 Stratégie de Gestion des Branches Git
Pour garantir la qualité et la traçabilité du code, nous appliquons un flux de travail structuré basé sur l'isolation stricte de chaque fonctionnalité :

* **`main` (Branche principale) :** Contient le code stable et validé en production. Chaque fusion validée sur cette branche déclenche le déploiement continu vers le serveur de production Render.
* **Branches `feature/us-XXX` (Branches de fonctionnalités) :** Pour chaque User Story spécifiée sur Trello, une branche éphémère dédiée a été créée afin de garantir l'étanchéité du code en cours de développement.

### Historique des branches vérifiable sur le dépôt :
* `feature/us-001-inscription` (US-001 - Inscription client)
* `feature/us-002-connexion` (US-002 - Connexion client)
* `feature/us-003-vehicle-search` (US-003 - Recherche de véhicules)
* `feature/us-004-vehicle-detail` (US-004 - Détail véhicule)
* `feature/us-005-depot-dossier` (US-005 - Dépôt de dossier)
* `feature/us-007-back-vehicules` (US-007 - Ajout / Modification véhicule)
* `feature/us-008-back-finalisation` (US-008 - Visualisation et validation des dossiers)

---

## 🔄 Démarche de Développement d'une User Story (Agile)
Chaque fonctionnalité suit un cycle de vie strict pour valider les critères de la *Definition of Done* (DoD) :
1. **Spécification :** Analyse des critères d'acceptation de la User Story sur Trello.
2. **Isolation :** Création et bascule sur la branche locale correspondante `feature/us-XXX`.
3. **Implémentation :** Écriture du code (Contrôleurs REST, Services, Modèles).
4. **Validation :** Écriture et exécution des tests unitaires (objectif de 80% de couverture minimum).
5. **Merge & Déploiement :** Fusion de la fonctionnalité dans `main` et vérification du comportement en ligne sur Render.

---

## 🧪 Lancement des Tests Unitaires
Pour nettoyer le projet et exécuter l'intégralité de la suite de tests unitaires JUnit/Mockito, lancez la commande suivante dans votre terminal :

```bash
mvn clean test