# Spring Boot Authentication Starter - Piano di realizzazione

## 1. Obiettivo

Realizzare una libreria di autenticazione riutilizzabile per progetti Spring Boot indipendenti.

Una nuova applicazione dovra poter integrare registrazione, login e protezione degli endpoint aggiungendo una sola dipendenza Maven e poche proprieta di configurazione, senza dover implementare nuovamente filtri JWT, gestione delle password e ciclo dei refresh token.

La prima versione sara uno Spring Boot Starter opinionato: offrira impostazioni predefinite sicure e un numero limitato di punti di estensione. Non avra l'obiettivo di sostituire un identity provider come Keycloak e non cerchera di supportare ogni possibile modello utente.

## 2. Decisioni iniziali

- Repository separato da quello delle applicazioni consumer.
- Un solo progetto Maven e un solo JAR nella prima versione.
- Installazione iniziale nel repository Maven locale.
- Java 17.
- Spring Boot 3.2.x.
- MySQL come unico database ufficialmente supportato nell'MVP.
- Account tecnici e credenziali gestiti dalla libreria.
- Profili utente applicativi separati dagli account tecnici.
- Access token JWT di breve durata.
- Refresh token persistiti, ruotati e revocabili.
- Componenti crittografici e JWT standard di Spring Security.

Artifact indicativo:

```xml
<dependency>
    <groupId>com.example.security</groupId>
    <artifactId>spring-boot-authentication-starter</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## 3. Responsabilita

### Responsabilita della libreria

- Conservare account e credenziali.
- Registrare account tramite email e password.
- Verificare le credenziali durante il login.
- Codificare e verificare le password.
- Emettere e validare access token.
- Emettere, ruotare e revocare refresh token.
- Configurare Spring Security.
- Esporre gli endpoint standard di autenticazione.
- Produrre errori HTTP uniformi.
- Esporre punti di estensione per autorita e creazione del profilo applicativo.

### Responsabilita dell'applicazione

- Conservare il profilo utente e gli altri dati di dominio.
- Definire ruoli e permessi applicativi.
- Autorizzare i singoli endpoint.
- Eseguire eventuali operazioni applicative successive alla registrazione.
- Configurare le rotte pubbliche aggiuntive.
- Fornire secret, datasource e impostazioni specifiche dell'ambiente.

## 4. Perimetro MVP

Endpoint previsti:

```text
POST /api/auth/register
POST /api/auth/login
POST /api/auth/refresh
POST /api/auth/logout
```

Funzionalita incluse:

- Registrazione tramite email e password.
- Login.
- Access token JWT.
- Refresh token persistito.
- Rotazione e revoca dei refresh token.
- Logout tramite revoca del refresh token.
- Account abilitabile e bloccabile.
- Password BCrypt.
- Autorita incluse nell'access token.
- Endpoint e durate configurabili.
- Gestione uniforme degli errori.
- Auto-configurazione Spring Boot.
- Test unitari e di integrazione su MySQL.

Funzionalita escluse dall'MVP:

- Verifica email.
- Recupero e cambio password.
- Autenticazione multifattore.
- Login social.
- Single Sign-On.
- Multi-tenancy.
- Supporto per database diversi da MySQL.
- Supporto per versioni Spring Boot diverse dalla linea 3.2.x.
- Personalizzazione completa dei controller.

## 5. Struttura del repository

Nella prima versione verra prodotto un solo JAR. La separazione delle responsabilita avverra tramite package:

```text
spring-boot-authentication-starter/
|-- pom.xml
|-- README.md
|-- src/main/java/
|   `-- .../authentication/
|       |-- account/
|       |-- token/
|       |-- security/
|       |-- web/
|       |-- config/
|       |-- extension/
|       `-- error/
|-- src/main/resources/
|   `-- META-INF/spring/
`-- src/test/
```

- `account`: entita, repository e servizi account.
- `token`: generazione e gestione di access e refresh token.
- `security`: integrazione con Spring Security.
- `web`: controller e DTO HTTP.
- `config`: proprieta e auto-configurazione.
- `extension`: contratti sostituibili dall'applicazione.
- `error`: eccezioni e risposte HTTP.

La suddivisione in piu artifact Maven verra valutata soltanto in presenza di un caso d'uso concreto, per esempio la necessita di usare il core senza JPA o senza controller.

## 6. Modello persistente

### Account tecnico

Tabella `auth_account`:

```text
id UUID
email VARCHAR, NOT NULL, UNIQUE
password_hash VARCHAR, NOT NULL
enabled BOOLEAN, NOT NULL
locked BOOLEAN, NOT NULL
created_at TIMESTAMP, NOT NULL
updated_at TIMESTAMP, NOT NULL
version BIGINT, NOT NULL
```

Regole:

- Email normalizzata prima del salvataggio e della ricerca.
- Vincolo univoco gestito anche a livello di database.
- Password mai conservata o registrata in chiaro.
- Optimistic locking tramite `version`.
- Tabelle della libreria prefissate con `auth_`.

### Autorita

Tabella `auth_account_authority`:

```text
account_id UUID, FK
authority VARCHAR, NOT NULL
```

Le autorita saranno stringhe generiche, per esempio:

```text
ROLE_USER
ROLE_ADMIN
VOCABULARY_READ
```

La libreria non definira enum applicativi come `Role` o `Permission`.

### Refresh token

Tabella `auth_refresh_token`:

```text
id UUID
account_id UUID, FK
token_hash VARCHAR, NOT NULL, UNIQUE
expires_at TIMESTAMP, NOT NULL
revoked_at TIMESTAMP, NULL
replaced_by UUID, NULL
created_at TIMESTAMP, NOT NULL
```

Nel database verra conservato soltanto l'hash del refresh token. Il token originale sara restituito al client esclusivamente al momento dell'emissione.

## 7. Gestione dello schema

La libreria non dovra modificare silenziosamente il database in produzione.

Approccio previsto:

- Fornire uno schema MySQL versionato.
- Usare una posizione dedicata per evitare conflitti con le migrazioni applicative.
- Rendere esplicita l'applicazione delle migrazioni.
- Raccomandare `ddl-auto=validate` negli ambienti non locali.
- Consentire `create` o `update` soltanto durante sviluppo e prototipazione.

Prima dell'implementazione andra scelta una strategia tra:

1. Migrazioni Flyway incluse e attivate esplicitamente.
2. Script SQL versionati distribuiti con la libreria e applicati dall'applicazione consumer.

La scelta dovra evitare la creazione di un bean Flyway che interferisca con eventuali migrazioni gia presenti nell'applicazione.

## 8. Password e protezione account

La libreria fornira:

- `PasswordEncoder` BCrypt.
- Costo BCrypt configurabile.
- Validazione minima della password.
- Normalizzazione dell'email.
- Controllo atomico dell'unicita dell'email.
- Account abilitabili e bloccabili.
- Risposta di login generica.

Il login non dovra distinguere pubblicamente tra email inesistente e password errata:

```json
{
  "code": "INVALID_CREDENTIALS",
  "message": "Invalid credentials"
}
```

## 9. Access token

Componenti da utilizzare:

- Spring Security OAuth2 Resource Server.
- `JwtEncoder`.
- `JwtDecoder`.
- Nimbus JOSE.
- `SecurityFilterChain`.

Non verranno implementati parser JWT, filtri Bearer o algoritmi crittografici proprietari.

Claim iniziali dell'access token:

```json
{
  "sub": "account-uuid",
  "iss": "application-name",
  "iat": 1710000000,
  "exp": 1710000900,
  "authorities": ["ROLE_USER"]
}
```

Per l'MVP si potra usare HS256 con un secret esterno di entropia adeguata. La progettazione dovra consentire l'aggiunta futura di chiavi RSA o EC senza modificare il contratto HTTP.

Non saranno presenti secret hardcoded o valori di produzione predefiniti.

## 10. Refresh token

Flusso previsto:

1. Il login genera access e refresh token.
2. Il refresh verifica hash, scadenza e stato del token.
3. Il vecchio refresh token viene revocato.
4. Viene emessa una nuova coppia di token.
5. Il riutilizzo di un refresh token gia ruotato viene rifiutato.
6. Il logout revoca il refresh token presentato.
7. L'access token resta valido fino alla propria breve scadenza.

Configurazione indicativa:

```yaml
authentication:
  token:
    issuer: ${spring.application.name}
    secret: ${AUTHENTICATION_JWT_SECRET}
    access-duration: 15m
    refresh-duration: 30d
```

## 11. Contratto HTTP

### Registrazione

Richiesta:

```json
{
  "email": "user@example.com",
  "password": "password"
}
```

Risposta `201 Created`:

```json
{
  "accountId": "uuid",
  "accessToken": "...",
  "refreshToken": "...",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

### Login

Restituisce la stessa struttura della registrazione. Un errore di autenticazione non rivela l'esistenza dell'account.

### Refresh

Riceve il refresh token e restituisce una nuova coppia di token.

### Logout

Revoca il refresh token presentato e non restituisce informazioni sensibili.

Tutti i DTO di input saranno validati tramite Jakarta Validation.

## 12. Auto-configurazione

L'auto-configurazione verra registrata tramite:

```text
META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

Dovra:

- Attivarsi soltanto quando sono presenti le dipendenze richieste.
- Poter essere disabilitata tramite proprieta.
- Creare bean soltanto se non sono gia stati dichiarati dall'applicazione.
- Fallire all'avvio quando manca una configurazione obbligatoria.
- Evitare conflitti con una `SecurityFilterChain` personalizzata.
- Esporre metadati per l'autocompletamento delle proprieta negli IDE.

Configurazione indicativa:

```yaml
authentication:
  enabled: true

  endpoints:
    base-path: /api/auth
    registration-enabled: true

  token:
    issuer: ${spring.application.name}
    secret: ${AUTHENTICATION_JWT_SECRET}
    access-duration: 15m
    refresh-duration: 30d

  password:
    bcrypt-strength: 12

  security:
    public-paths:
      - /actuator/health
```

## 13. Integrazione Spring Security

### Configurazione semplice

In assenza di una configurazione applicativa personalizzata, la libreria fornira una configurazione predefinita:

- Endpoint `/api/auth/**` pubblici.
- Rotte configurate in `public-paths` pubbliche.
- Tutte le altre richieste autenticate.
- Sessioni stateless.
- CSRF disabilitato per API Bearer stateless.
- HTTP Basic disabilitato.
- Validazione Bearer affidata a OAuth2 Resource Server.

### Configurazione avanzata

Se l'applicazione dichiara una propria `SecurityFilterChain`, potra applicare un configuratore fornito dalla libreria. Questo contratto dovra essere progettato prima dell'implementazione per evitare che la libreria impedisca configurazioni di sicurezza specifiche dell'applicazione.

## 14. Punti di estensione

### Autorita dell'account

```java
public interface AccountAuthoritiesProvider {
    Collection<String> getAuthorities(UUID accountId);
}
```

L'implementazione predefinita potra restituire `ROLE_USER`. L'applicazione potra sostituirla dichiarando un proprio bean.

### Creazione del profilo applicativo

```java
public interface AccountRegistrationHandler {
    void afterRegistration(RegisteredAccount account);
}
```

L'applicazione potra usare questo hook per creare un profilo collegato all'account tecnico:

```text
application_user
id
auth_account_id
first_name
last_name
```

Andra stabilito se il primo hook e la registrazione debbano partecipare alla stessa transazione, cosi che un errore durante la creazione del profilo annulli anche l'account.

## 15. Gestione errori

Formato previsto:

```json
{
  "code": "INVALID_CREDENTIALS",
  "message": "Invalid credentials",
  "timestamp": "2026-08-05T10:00:00Z",
  "path": "/api/auth/login"
}
```

Errori iniziali:

```text
INVALID_REQUEST
INVALID_CREDENTIALS
EMAIL_ALREADY_REGISTERED
ACCOUNT_DISABLED
ACCOUNT_LOCKED
INVALID_ACCESS_TOKEN
INVALID_REFRESH_TOKEN
REFRESH_TOKEN_EXPIRED
REFRESH_TOKEN_REVOKED
AUTHENTICATION_CONFIGURATION_ERROR
```

Le risposte non dovranno esporre stack trace, dettagli crittografici o informazioni non necessarie sull'account.

## 16. Test

### Test unitari

- Normalizzazione email.
- Codifica e verifica password.
- Generazione e validazione access token.
- Scadenza token.
- Rotazione refresh token.
- Revoca refresh token.
- Rifiuto del riutilizzo di un token ruotato.
- Account bloccato o disabilitato.
- Risoluzione delle autorita.

### Test di auto-configurazione

- Creazione dei bean predefiniti.
- Override tramite bean applicativi.
- Starter disabilitato.
- Proprieta obbligatorie mancanti.
- Configurazione personalizzata.
- Coesistenza con una `SecurityFilterChain` applicativa.

### Test di integrazione

Usare Spring Boot Test, MockMvc e MySQL Testcontainers per verificare:

1. Registrazione.
2. Accesso a un endpoint protetto.
3. Login.
4. Refresh.
5. Rifiuto del vecchio refresh token.
6. Logout.
7. Access token scaduto.
8. Token malformato.
9. Registrazione concorrente della stessa email.
10. Transazione tra account e profilo applicativo.

## 17. Applicazione di esempio

Il repository conterra una piccola applicazione usata per test e documentazione, ma non inclusa nel JAR pubblicato.

Dovra dimostrare:

- Integrazione tramite una sola dipendenza.
- Configurazione MySQL.
- Applicazione dello schema.
- Registrazione e login.
- Endpoint pubblico.
- Endpoint autenticato.
- Uso di `@PreAuthorize`.
- Creazione di un profilo applicativo.

L'applicazione attuale verra integrata solo dopo aver stabilizzato lo starter, cosi da separare i difetti della libreria dai comportamenti gia presenti nel progetto.

## 18. Distribuzione e versionamento

Distribuzione iniziale:

```bash
mvn clean install
```

Versione iniziale:

```text
0.1.0-SNAPSHOT
```

Evoluzioni successive:

- GitHub Packages o altro repository Maven privato.
- Pipeline CI.
- Release non `SNAPSHOT`.
- Semantic Versioning.
- Changelog.
- Matrice di compatibilita con le versioni Spring Boot.

## 19. Fasi di implementazione

### Fase 1 - Specifica

- Confermare i requisiti dell'MVP.
- Documentare responsabilita e confini.
- Definire un threat model essenziale.
- Stabilire API HTTP e proprieta pubbliche.
- Decidere la strategia di gestione dello schema.
- Decidere la semantica transazionale dell'hook di registrazione.

### Fase 2 - Bootstrap

- Creare il repository separato.
- Creare il progetto Maven Java 17 / Spring Boot 3.2.x.
- Definire coordinate Maven e package base.
- Configurare dipendenze e test.
- Registrare l'auto-configurazione Spring Boot.

### Fase 3 - Account e database

- Implementare entita account, autorita e refresh token.
- Implementare repository JPA.
- Preparare schema MySQL versionato.
- Implementare normalizzazione email e vincoli.

### Fase 4 - Registrazione e password

- Configurare BCrypt.
- Implementare validazione delle richieste.
- Implementare registrazione atomica.
- Implementare gestione email duplicata.
- Integrare autorita predefinite e hook applicativo.

### Fase 5 - Login e access token

- Integrare `JwtEncoder` e `JwtDecoder`.
- Implementare autenticazione credenziali.
- Implementare generazione access token.
- Implementare errori non enumerabili.

### Fase 6 - Refresh e logout

- Implementare generazione sicura del refresh token.
- Conservare soltanto l'hash.
- Implementare scadenza, rotazione e revoca.
- Implementare logout.
- Gestire il tentativo di riutilizzo.

### Fase 7 - Web e sicurezza

- Implementare controller e DTO.
- Implementare gestione uniforme degli errori.
- Configurare OAuth2 Resource Server.
- Implementare configurazione di sicurezza semplice e avanzata.

### Fase 8 - Auto-configurazione

- Implementare `@ConfigurationProperties`.
- Implementare condizioni e override dei bean.
- Validare le proprieta all'avvio.
- Generare metadata delle proprieta.

### Fase 9 - Verifica

- Completare test unitari.
- Completare test di auto-configurazione.
- Completare test con MySQL Testcontainers.
- Verificare scenari di errore e concorrenza.
- Creare l'applicazione di esempio.

### Fase 10 - Distribuzione e integrazione

- Scrivere README e guida di integrazione.
- Installare `0.1.0-SNAPSHOT` nel repository Maven locale.
- Integrare la libreria in questa applicazione.
- Raccogliere i problemi reali prima di ampliare configurabilita e funzionalita.

## 20. Criteri di completamento dell'MVP

L'MVP sara completo quando una nuova applicazione potra:

- Aggiungere una sola dipendenza Maven.
- Configurare MySQL e un secret esterno.
- Applicare lo schema della libreria.
- Usare registrazione, login, refresh e logout.
- Proteggere endpoint senza implementare un filtro JWT.
- Usare `@PreAuthorize` con autorita applicative.
- Associare un profilo applicativo all'account tecnico.
- Sostituire il provider delle autorita.
- Personalizzare la configurazione Spring Security quando necessario.
- Superare tutti i test di integrazione contro MySQL tramite Testcontainers.

## 21. Benefici attesi

- Riduzione del codice ripetuto nei nuovi backend Spring Boot.
- Uso coerente di Spring Security tra applicazioni.
- Password, token ed errori gestiti uniformemente.
- Correzione centralizzata delle vulnerabilita.
- Aggiornamento centralizzato delle policy di sicurezza.
- Contratto di integrazione documentato e verificabile.
- Maggiore velocita nella creazione di nuovi progetti.
- Possibilita di evolvere in seguito verso piu moduli o un repository Maven condiviso.

## 22. Rischi da controllare

- Un difetto della libreria puo propagarsi a tutte le applicazioni consumer.
- Le auto-configurazioni possono diventare difficili da diagnosticare se troppo implicite.
- Le migrazioni della libreria possono entrare in conflitto con quelle applicative.
- Un eccesso di opzioni puo trasformare lo starter in un framework difficile da mantenere.
- La compatibilita con nuove versioni di Spring Boot richiedera test dedicati.
- Registrazione e modello del profilo possono variare sensibilmente tra applicazioni.
- La libreria resta una responsabilita di sicurezza interna e richiede manutenzione continuativa.

La strategia per limitare questi rischi e mantenere ristretto l'MVP, usare componenti standard di Spring Security e introdurre nuovi punti di estensione soltanto a partire da casi d'uso reali.
