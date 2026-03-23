# TripBooking API

REST API de gestion de réservations de voyages — Spring Boot 4.x + HATEOAS.

**Base URL:** `http://localhost:8080`

---

## Démarrage

```bash
./mvnw spring-boot:run
```

---

## Authentification & Rôles

### Login Admin
```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@trip.com","password":"admin123"}'
```

### Login Customer
```bash
curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@trip.com","password":"customer123"}'
```

> **Astuce :** Sauvegarde ton token dans une variable shell pour les tests suivants :
> ```bash
> TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
>   -H "Content-Type: application/json" \
>   -d '{"email":"admin@trip.com","password":"admin123"}' \
>   | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")
> echo $TOKEN
> ```

---

## Voyages

### Créer un voyage `ADMIN`
```bash
curl -s -X POST http://localhost:8080/api/voyages \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "idOrigine": 1,
    "idDestination": 2,
    "prix": 50000,
    "depart": "2026-05-01T08:00:00",
    "idVoiture": 1,
    "idChauffeur": 1
  }' | python3 -m json.tool
```

### Modifier un voyage `ADMIN`
```bash
curl -s -X PUT http://localhost:8080/api/voyages/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "idOrigine": 1,
    "idDestination": 3,
    "prix": 75000,
    "depart": "2026-05-02T09:00:00",
    "idVoiture": 2,
    "idChauffeur": 2
  }' | python3 -m json.tool
```

### Annuler un voyage `ADMIN`
```bash
curl -s -X DELETE http://localhost:8080/api/voyages/1 \
  -H "Authorization: Bearer $TOKEN" | python3 -m json.tool
```

### Consulter tous les voyages `PUBLIC`
```bash
curl -s http://localhost:8080/api/voyages | python3 -m json.tool
```

### Consulter avec filtres `PUBLIC`

**Par origine :**
```bash
curl -s "http://localhost:8080/api/voyages?idOrigine=1" | python3 -m json.tool
```

**Par destination :**
```bash
curl -s "http://localhost:8080/api/voyages?idDestination=2" | python3 -m json.tool
```

**Par date de départ :**
```bash
curl -s "http://localhost:8080/api/voyages?depart=2026-05-01" | python3 -m json.tool
```

**Par état (1=actif, 0=annulé) :**
```bash
curl -s "http://localhost:8080/api/voyages?etat=1" | python3 -m json.tool
```

**Combiné :**
```bash
curl -s "http://localhost:8080/api/voyages?idOrigine=1&idDestination=2&etat=1" | python3 -m json.tool
```

---

## Tester les erreurs

### Sans token → 401
```bash
curl -s -o /dev/null -w "HTTP %{http_code}" \
  -X POST http://localhost:8080/api/voyages \
  -H "Content-Type: application/json" \
  -d '{"idOrigine":1,"idDestination":2,"prix":50000,"depart":"2026-05-01T08:00:00","idVoiture":1,"idChauffeur":1}'
```

### Avec token CUSTOMER sur endpoint ADMIN → 403
```bash
CTOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"customer@trip.com","password":"customer123"}' \
  | python3 -c "import sys,json; print(json.load(sys.stdin)['token'])")

curl -s -o /dev/null -w "HTTP %{http_code}" \
  -X POST http://localhost:8080/api/voyages \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CTOKEN" \
  -d '{"idOrigine":1,"idDestination":2,"prix":50000,"depart":"2026-05-01T08:00:00","idVoiture":1,"idChauffeur":1}'
```

---

## Données par défaut

### Lieux
| ID | Nom |
|----|-----|
| 1  | Antananarivo |
| 2  | Antsirabe |
| 3  | Fianarantsoa |

### Voitures
| ID | Numéro | Marque |
|----|--------|--------|
| 1  | AA-001-AA | Toyota |
| 2  | AA-002-AA | Mercedes |

### Chauffeurs
| ID | Nom | Téléphone |
|----|-----|-----------|
| 1  | Jean Dupont | +261321234567 |
| 2  | Marie Martin | +261322345678 |

### Utilisateurs
| Email | Password | Rôle |
|-------|----------|------|
| admin@trip.com | admin123 | ADMIN |
| customer@trip.com | customer123 | CUSTOMER |

---

## Responsables

| Endpoint | Responsable |
|----------|-------------|
| Auth + rôles | Isabelle |
| `POST /voyages` | Isabelle |
| `PUT /voyages/{id}` | Isabelle |
| `DELETE /voyages/{id}` | Isabelle |
| `GET /voyages` | Isabelle |
| `POST /reservations` | To |
| `PUT /reservations/{id}` | To |
| `PUT /cancel-reservation/{id}` | To |
| `PUT /confirm-reservation/{id}` | To |
| `GET /voyages/{id}` | To |
| `GET /voyages/statistiques/{année}/{mois}` | To |
