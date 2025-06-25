1. Modèles de données
User : Représentation d'un utilisateur avec identifiant, nom d'utilisateur et rôle
Document : Représentation d'un document avec titre, contenu, créateur et dates de création/modification
DocumentPermission : Relation entre un utilisateur et un document définissant le type d'accès (lecture, écriture, suppression)
2. Interfaces de service
UserService : Interface définissant les opérations CRUD pour les utilisateurs
DocumentService : Interface définissant les opérations CRUD pour les documents et le partage
3. Implémentations de service
UserServiceImpl : Implémentation du service utilisateur avec stockage en mémoire
DocumentServiceImpl : Implémentation du service document avec stockage en mémoire
4. Service d'autorisation
AuthorizationService : Gestion des règles d'accès avec Casbin (ajout de politiques, rôles, vérification des permissions)
5. Intercepteurs et configuration
AuthenticationInterceptor : Intercepteur pour authentifier les requêtes HTTP
WebMvcConfig : Configuration des intercepteurs et des chemins à intercepter
6. Contrôleurs REST
UserController : API REST pour la gestion des utilisateurs, rôles et permissions
DocumentController : API REST pour la gestion des documents avec contrôle d'accès
Cette architecture respecte le modèle MVC (Modèle-Vue-Contrôleur) et permet une séparation claire des responsabilités :


Les modèles définissent la structure des données
Les services encapsulent la logique métier
Les contrôleurs exposent les API REST
Les intercepteurs gèrent l'authentification et l'autorisation
L'utilisation de Casbin permet une gestion flexible des autorisations basée sur des règles définies à l'extérieur du code.
