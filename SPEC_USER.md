# Spécification fonctionnelle — Gestion des utilisateurs (User)

## Objectif

Cette spécification décrit le comportement attendu pour la gestion d’un utilisateur
dans une application Java.

Elle sert de **base pour générer automatiquement des tests unitaires et fonctionnels**
(avec ou sans l’aide de l’IA), avant l’implémentation du code.

---

## 1. Modèle de données

Un utilisateur est représenté par une classe `User` contenant les champs suivants :

- `email` : String
- `password` : String
- `role` : Role

### Enum `Role`

L’énumération `Role` contient exactement les valeurs suivantes :
- `USER`
- `ADMIN`

---

## 2. Règles de validation

### 2.1 Email

- Obligatoire (non null, non vide après `trim`)
- Doit contenir exactement **un seul caractère `@`**
- Doit contenir au moins **un `.` après le `@`**

#### Exemples valides
- `alice@test.com`
- `bob.smith@company.io`

#### Exemples invalides
- `""`
- `"   "`
- `"alice"`
- `"alice@"`
- `"@test.com"`
- `"alice@test"`
- `"alice@@test.com"`

---

### 2.2 Password

- Obligatoire (non null, non vide après `trim`)
- Doit respecter la politique de sécurité suivante :
    - longueur ≥ 8 caractères
    - au moins une majuscule
    - au moins une minuscule
    - au moins un chiffre
    - au moins un caractère spécial (tout caractère non lettre et non chiffre)

La méthode existante suivante doit être utilisée :
`PasswordPolicy.isStrong(password)`

---

### 2.3 Role

- Obligatoire (non null)
- Valeurs autorisées : `USER`, `ADMIN`

---

## 3. Normalisation des données

- L’email est automatiquement **trim()** avant stockage
- Le password n’est **pas modifié**
- Le rôle est stocké tel quel

---

## 4. Comportements attendus

### 4.1 Autorisation d’accès

La classe `User` expose la méthode :

```java
boolean canAccessAdminArea
```

Règle
- Retourne true si et seulement si role == ADMIN
- Retourne false dans tous les autres cas

---

## 5. Gestion des erreurs

- En cas de non-respect des règles :
  - Une IllegalArgumentException doit être levée
  - Le message d’erreur doit être explicite et cohérent
- Messages minimaux attendus :
  - "email must be valid"
  - "password must be strong"
  - "role must not be null"

---

## 6. Service utilisateur

Une classe UserService doit exposer la méthode suivante :
```java
User register(String email, String password, Role role);
```

Règles
- Crée un utilisateur valide
- Retourne l’utilisateur créé
- Propage les exceptions si les données sont invalides

---

## 7. Tests attendus

À partir de cette spécification, vous devez produire :
- Tests unitaires
- Validation des règles sur User
- Méthode canAccessAdminArea()
- Cas limites et cas d’erreur

Tests fonctionnels légers
- Tests sur UserService.register(...)
- Validation du comportement métier global

*Aucun test UI, HTTP ou base de données n’est attendu.*

## Note importante
- Cette spécification est volontairement textuelle.
- Votreme objectif est de la transforr en comportements vérifiables via des tests,
- avec l’aide éventuelle de l’IA, tout en gardant un esprit critique.