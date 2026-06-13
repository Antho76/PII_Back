# PII Back

Backend de l'application PII — API REST construite avec Spring Boot, sécurisée et connectée à une base PostgreSQL.

## Stack technique

| Technologie | Version | Rôle |
|---|---|---|
| Java | 21 | Langage principal |
| Spring Boot | 4.0.5 | Framework backend |
| Spring Security | — | Authentification & autorisation |
| Spring Data JPA | — | ORM / accès base de données |
| Spring Validation | — | Validation des données entrantes |
| PostgreSQL | 16 | Base de données relationnelle |
| Lombok | — | Réduction du code boilerplate |
| Maven | 3.9 | Gestionnaire de dépendances / build |
| Docker & Docker Compose | — | Conteneurisation de l'application |

## Prérequis

- [Docker Desktop](https://www.docker.com/get-started) (inclut Docker Compose)
- Aucune installation manuelle de Java ou Maven nécessaire (géré par Docker)

### Installation de Docker

#### macOS

```bash
# Via Homebrew
brew install --cask docker
# Puis lancer Docker Desktop depuis les Applications
```

Ou télécharger directement : [Docker Desktop pour Mac](https://docs.docker.com/desktop/install/mac-install/)

#### Windows

Télécharger et installer [Docker Desktop pour Windows](https://docs.docker.com/desktop/install/windows-install/).  
WSL 2 doit être activé (Docker Desktop le propose lors de l'installation).

#### Linux (Ubuntu/Debian)

```bash
# Désinstaller les anciennes versions si besoin
sudo apt remove docker docker-engine docker.io

# Installer Docker Engine
sudo apt update
sudo apt install -y ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Ajouter son utilisateur au groupe docker (évite d'utiliser sudo)
sudo usermod -aG docker $USER
newgrp docker
```

Vérifier l'installation :

```bash
docker --version
docker compose version
```

## Configuration de l'environnement

Créer un fichier `.env` à la racine du projet (en s'aidant du `.neon` présent) :

```env
POSTGRES_DB=pii
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_password
```

## Installation & lancement

```bash
# Cloner le repo
git clone https://github.com/Antho76/PII_Back.git
cd PII_Back

# Configurer le .env
cp .neon .env
# Éditer .env avec vos valeurs

# Démarrer l'ensemble des services (PostgreSQL + Backend)
docker compose up --build
```

L'API est accessible sur [http://localhost:8080](http://localhost:8080).  
La base PostgreSQL tourne sur le port `5432`.

### Lancement en arrière-plan

```bash
docker compose up --build -d
```

### Arrêt des services

```bash
docker compose down
```

Pour supprimer également les volumes (données PostgreSQL) :

```bash
docker compose down -v
```

## Structure du projet

```
PII_Back/
├── src/
│   └── main/
│       ├── java/com/pii/        # Code source Spring Boot
│       └── resources/           # application.properties
├── Dockerfile                   # Image Docker multi-stage (build + run)
├── docker-compose.yml           # Orchestration PostgreSQL + Backend
├── pom.xml                      # Dépendances Maven
└── .neon                        # Template de variables d'environnement
```

## Architecture Docker


Le `docker-compose.yml` orchestre deux services :
- `postgres` : base de données avec health check intégré
- `backend` : application Spring Boot, démarre uniquement après que PostgreSQL soit prêt
