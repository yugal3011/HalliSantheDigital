(# HalliSanthe Digital — Android Marketplace App)

**Project Summary:**
- **Name:** HalliSanthe Digital — a lightweight Android marketplace app to browse, upload and view products.
- **Platform:** Android (Kotlin) using Android Gradle Plugin and AndroidX libraries.
- **Primary features:** Product browsing (grid), product detail view, image picking/compression and upload, Firebase Firestore for product storage, Firebase Storage for product images.

**Quick Links:**
- **Build config:** [build.gradle](build.gradle#L1)
- **App module:** [app/build.gradle](app/build.gradle#L1)
- **Firebase config:** [app/google-services.json](app/google-services.json)
- **Main source:** [app/src/main/java/com/hallisanthe/digital](app/src/main/java/com/hallisanthe/digital)

**Architecture & Key Components**
- **UI Layer:** Activities in `com.hallisanthe.digital`:
	- `MainActivity` — product grid, search and navigation (see [MainActivity.kt](app/src/main/java/com/hallisanthe/digital/MainActivity.kt#L1)).
	- `ProductDetailActivity` — product details and actions (see [ProductDetailActivity.kt](app/src/main/java/com/hallisanthe/digital/ProductDetailActivity.kt#L1)).
	- `UploadActivity` — pick/compress image, upload to Firebase Storage, and add product to Firestore (see [UploadActivity.kt](app/src/main/java/com/hallisanthe/digital/UploadActivity.kt#L1)).

- **Data Layer:**
	- `FirestoreManager` — thin wrapper around Firebase Firestore for `addProduct` and `getProducts` (see [FirestoreManager.kt](app/src/main/java/com/hallisanthe/digital/firebase/FirestoreManager.kt#L1)).
	- `Product` model — Serializable Kotlin data class with `id`, `name`, `price`, `imageUrl`, `timestamp` (see [Product.kt](app/src/main/java/com/hallisanthe/digital/models/Product.kt#L1)).

- **UI / Adapter:**
	- `ProductAdapter` — RecyclerView adapter for product grid (see [ProductAdapter.kt](app/src/main/java/com/hallisanthe/digital/adapters/ProductAdapter.kt#L1)).

- **Utilities:**
	- `ImageCompressor` — wraps `id.zelory:compressor` for image compression.
	- `PermissionHelper` — runtime permission helpers for camera / storage access.

**Dependencies & Third-Party Services**
- AndroidX, Material, ConstraintLayout, Lifecycle (core UI/backbone).
- Firebase: Firestore + Storage via the BOM declared in [app/build.gradle](app/build.gradle#L1).
- Glide for image loading, id.zelory:compressor for compression.

**Repository Layout (high-level)**
- Root files: [build.gradle](build.gradle#L1), [gradle.properties](gradle.properties#L1), [settings.gradle](settings.gradle#L1).
- App module: [app/](app) — sources under `app/src/main/`, resources under `app/src/main/res/`, module Gradle config at [app/build.gradle](app/build.gradle#L1).
- IDE & tooling: `.idea/`, `.vscode/` (workspace settings), Gradle wrapper under `gradle/`.

**Setup & Prerequisites**
- Install Android Studio (Arctic Fox or newer recommended) and JDK 11+.
- Use the Gradle wrapper included; run platform-specific wrapper commands.

Commands (from repository root):
```bash
# Linux / macOS
./gradlew clean assembleDebug

# Windows
gradlew.bat clean assembleDebug
```

**Local configuration (required files)**
- `app/google-services.json`: Firebase configuration required for Firestore and Storage. Keep this file private for production and do not commit secrets into public repositories. The repo currently contains this file — if this is sensitive, consider removing it and using CI secrets instead.
- `local.properties`: Android SDK path (machine-specific). Do not commit; already ignored by module .gitignore.

**Build variants & flavors**
- Current project uses standard `debug` and `release` build types defined in `app/build.gradle`.

**Testing**
- Unit tests: `testImplementation 'junit:junit:4.13.2'` is present. Android instrumentation tests use `androidTestImplementation` dependencies in the module `build.gradle`.

**Security & Secrets**
- Avoid committing private credentials. If you publish this repo or share it, rotate Firebase credentials if leaked.
- `google-services.json` contains project identifiers — treat as sensitive for public repos.

**Common Tasks & Troubleshooting**
- If `git add .` fails due to Gradle cache lock files (permission denied), add/ensure a root `.gitignore` contains `.gradle/` and `build/`. See [.gitignore](.gitignore#L1).
- Line-ending warnings on Windows: to normalize, choose a policy and set `.gitattributes` or configure Git `core.autocrlf`. Example:
```bash
# set to true on Windows (converts LF->CRLF on checkout)
git config --global core.autocrlf true
```

**Code Style & Conventions**
- Kotlin idiomatic usage (data classes, coroutines for background work in `UploadActivity`).
- ViewBinding is enabled (`buildFeatures.viewBinding = true`), prefer `binding` usage over `findViewById`.

**Contribution & Development Workflow**
- Fork / branch from `main` for features and fixes, open PRs with clear titles and testing instructions.
- Keep `google-services.json` and other machine-specific files out of public PRs unless explicitly required and safe.

**Next Steps / Recommendations**
- Add a `.gitattributes` and a small `CONTRIBUTING.md` if multiple contributors join.
- Consider moving Firebase config to environment-managed secrets for CI and keep only minimal placeholders in the repo.
- Add basic CI (GitHub Actions) to run `./gradlew assembleDebug` and unit tests on PRs.

---

If you want, I can also:
- add `CONTRIBUTING.md` and `.gitattributes`,
- generate a short `CHANGELOG.md` template, or
- create a small GitHub Actions workflow that builds the app on push/PR.

