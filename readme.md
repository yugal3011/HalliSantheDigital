# Halli-Santhe Digital

**Halli-Santhe Digital** is a Kotlin Android marketplace app for browsing and uploading products with Firebase-backed storage. The current implementation focuses on a simple product catalog, image upload flow, search, and product detail screens. The broader product direction is an offline-first marketplace for rural artisans and urban buyers.

---

## Table of Contents

- [Project Overview](#project-overview)
- [Current Implementation](#current-implementation)
- [Product Vision](#product-vision)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Folder Structure](#folder-structure)
- [Setup & Installation](#setup--installation)
- [Build & Run](#build--run)
- [Configuration](#configuration)
- [Testing](#testing)
- [Security Notes](#security-notes)
- [Future Scope](#future-scope)
- [Contributing](#contributing)
- [License](#license)

---

## Project Overview

Halli-Santhe Digital is designed to connect local sellers with buyers through a mobile-first product catalog. The repository currently implements a lightweight Android app with Firebase Firestore and Firebase Storage. The long-term product direction, described below, expands this into an offline-first artisan marketplace with local persistence, sync, sharing, and profit tracking.

### Quick Links

- [Root Gradle config](build.gradle)
- [App Gradle config](app/build.gradle)
- [Firebase config](app/google-services.json)
- [Main app package](app/src/main/java/com/hallisanthe/digital)

---

## Current Implementation

The existing app includes three main screens:

- `MainActivity` for browsing products in a 2-column grid, searching by name, and navigating to the sell flow.
- `ProductDetailActivity` for viewing a selected product and its image.
- `UploadActivity` for selecting an image, compressing it, uploading it to Firebase Storage, and saving product metadata to Firestore.

Supporting code includes:

- `FirestoreManager` for reading and writing product data.
- `Product` as a serializable model with `name`, `price`, `imageUrl`, and `timestamp`.
- `ProductAdapter` for rendering grid cards.
- `ImageCompressor` for reducing image size before upload.
- `PermissionHelper` for runtime camera and storage permission handling.

Current dependencies in the repo include AndroidX, Material Components, Glide, Firebase Firestore, Firebase Storage, and `id.zelory:compressor`.

---

## Product Vision

The broader product concept for Halli-Santhe Digital is an offline-first marketplace for rural artisans and urban buyers.

### Problem Statement

Many artisans still sell through local weekly markets and have limited digital visibility. Buyers often cannot discover handmade products unless they travel physically. The goal of this project is to reduce that gap with a simple mobile catalog that is easy to use on low-connectivity devices.

### Solution Overview

- A simple product upload flow for artisans with name, price, and photo.
- A browse-and-search catalog for buyers.
- Offline-first persistence for use without continuous internet.
- Sync to Firebase when connectivity is restored.
- Optional catalog and report sharing for wider reach.
- Future profit visibility and business tracking for artisans.

---

## Key Features

| Feature | Status | Description |
|---|---|---|
| Product browsing | Implemented | Buyers can view products in a grid layout. |
| Product details | Implemented | Product image, title, and price are shown in a detail screen. |
| Search | Implemented | Local name-based filtering in the catalog. |
| Image upload | Implemented | Users can pick and upload product photos. |
| Image compression | Implemented | Images are compressed before upload to reduce size. |
| Firebase sync | Implemented | Product metadata and images are stored in Firestore and Storage. |
| Offline-first catalog | Planned | Local persistence and sync when connectivity returns. |
| Rapid product entry | Planned | A minimal “2-tap + amount” flow for sellers. |
| Monthly profit dashboard | Planned | Income and cost summaries for artisans. |
| PDF or WhatsApp sharing | Planned | Export product or sales summaries for sharing. |

---

## Tech Stack

### Current Stack

| Category | Technologies |
|---|---|
| Language | Kotlin |
| UI | Android XML layouts + ViewBinding |
| Cloud Database | Firebase Firestore |
| Cloud Storage | Firebase Storage |
| Image Loading | Glide |
| Image Compression | `id.zelory:compressor` |
| Async Work | Kotlin coroutines |

### Target / Future Stack

| Category | Technologies |
|---|---|
| Architecture | MVVM + Clean Architecture |
| Local Database | Room |
| Reactive State | Flow / StateFlow |
| Dependency Injection | Dagger Hilt |
| PDF Generation | `android.graphics.pdf.PdfDocument` + FileProvider |
| Sharing | Android share intents / WhatsApp integration |

---

## System Architecture

### Current Architecture

The current app is a simple Android client with the following flow:

1. `MainActivity` loads products from `FirestoreManager`.
2. Products are rendered in `ProductAdapter`.
3. Search filters the in-memory list on the client.
4. `UploadActivity` compresses an image and uploads it to Firebase Storage.
5. A `Product` document is created in Firestore.

### Target Architecture

The intended architecture for the broader vision is:

- **View layer:** screens, composables or activities, and UI state.
- **ViewModel layer:** state management and user actions.
- **Domain layer:** use cases such as fetch products, upload product, search, and profit calculation.
- **Data layer:** repository coordinating local Room cache and remote Firebase sync.
- **Offline-first storage:** Room as the source of truth when offline.
- **Cloud sync:** Firestore and Storage used for synchronization and media persistence.

This target architecture matches the richer product story you provided, while the current codebase remains simpler.

---

## Folder Structure

### Current Repository Layout

- `build.gradle` — top-level Gradle configuration.
- `settings.gradle` — project module wiring.
- `gradle/` — Gradle wrapper files.
- `app/build.gradle` — Android app module dependencies and build settings.
- `app/google-services.json` — Firebase configuration.
- `app/src/main/java/com/hallisanthe/digital/` — Kotlin source files.
- `app/src/main/res/` — layouts, drawables, menus, and values.

### Planned Higher-Level Structure

The target architecture you shared can be used as a roadmap for future refactoring:

```text
app/src/main/java/com/hallisanthe/digital/
├── data/
│   ├── local/
│   ├── remote/
│   └── repository/
├── domain/
│   └── usecases/
├── presentation/
│   ├── ui/
│   ├── viewmodels/
│   └── components/
├── di/
└── utils/
```

---

## Setup & Installation

### Prerequisites

- Android Studio installed
- JDK 11 or newer
- Android SDK configured
- Firebase project created if you want Firestore and Storage to work

### Steps

1. Clone the repository.
2. Open the project in Android Studio.
3. Ensure `app/google-services.json` exists in the `app/` folder.
4. Sync the Gradle project.
5. Run the app on an emulator or physical device.

---

## Build & Run

From the repository root:

```bash
# Windows
gradlew.bat clean assembleDebug

# Linux / macOS
./gradlew clean assembleDebug
```

To run from Android Studio, use the standard Run button after Gradle sync.

---

## Configuration

- `app/google-services.json` is required for Firebase integration.
- `local.properties` should contain your Android SDK path and should not be committed.
- The project uses standard `debug` and `release` build types.

---

## Testing

The module includes basic test dependencies for unit and instrumentation testing. If you expand the architecture into Room, repositories, and ViewModels, add tests for repository sync, search behavior, and upload flows.

---

## Security Notes

- Do not commit private Firebase credentials to public repositories.
- Treat `google-services.json` as sensitive project configuration.
- If you publish the app publicly, review storage and Firestore rules carefully.

---

## Future Scope

The product vision for Halli-Santhe Digital can grow in these directions:

- Offline-first browsing with Room database.
- Sync queue for uploads when the device reconnects.
- Seller dashboard with monthly earnings and profit estimates.
- PDF export for product catalogs and monthly reports.
- Share products through WhatsApp and other Android share targets.
- Better filtering, categories, and search suggestions.
- Dagger Hilt-based dependency injection.
- A cleaner MVVM + Clean Architecture refactor.

---

## Contributing

If you add features, keep the README aligned with the actual implementation and clearly separate current behavior from planned roadmap items. Use small, focused pull requests and include screenshots or short testing notes where possible.

---

## License

No license file is present yet. Add one if you plan to share or publish this project.


