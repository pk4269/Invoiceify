# Invoiceify

# GroceryStore-Android

A complete **offline-first** grocery-store management app for Android, built with Kotlin, Jetpack libraries, CameraX + ML Kit, Room, and ZXing for barcode generation.

---

#Core Features

| Product catalog | ✅ Room schema, three-tier pricing |
| Customer loyalty | ✅ Points & member prices |
| Barcode scanning | ✅ CameraX + ML Kit (EAN/UPC) |
| Invoice & receipt | ✅ POS screen + ESC/POS 55 mm |
| Stock adjustment | ✅ PIN-secured workflow + audit trail |
| Barcode generator | ✅ CODE-128 / EAN-13 / UPC-A |
| Data export | ✅ CSV (sales & inventory) + PDF/JPEG invoice |
| Print label | ✅ 80 mm thermal bitmap |

---

#Tech Stack

- Language: Kotlin 100 %  
- UI: Jetpack Compose / XML views (hybrid)  
- Database: Room (SQLite)  
- Camera: CameraX + ML Kit Barcode Scanning  
- Barcode: ZXing Core  
- PDF: iText 5 (FOSS)  
- DI: Hilt  
- Async: Coroutines + Flow  

---

### 📦  Repositories & Modules

| Package | Purpose |
|---------|---------|
| `data` | Room entities, DAOs, repositories |
| `ui` | Fragments, Dialogs, Compose screens |
| `util` | Barcode gen, CSV/PDF/JPEG export, ESC/POS helpers |
| `viewmodel` | Hilt ViewModels |

---
