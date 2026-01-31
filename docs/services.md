# Services Overview

This document provides a concise overview of the project's service layer and responsibilities.

## ProductService

Responsible for managing products in the system. Key behaviors:

- Create, update, delete, and retrieve products as `ProductDto` objects.
- List products with pagination, optional sorting (e.g. `price,asc`), and optional category filtering.
- Caching: list results are cached for performance. Cache is invalidated on create/update/delete.
- Image upload: accepts multipart files, stores uploaded files on disk (configured by `file.upload-dir`) and persists the image path on the product entity.
- Error behavior: operations that require an existing product throw `IllegalArgumentException` if the product is not found.

### Examples
- Create: call `ProductService.create(dto)` with required fields like name and price.
- List: `ProductService.list(0, 20, "price,asc", "electronics", null, null)` returns a `Page<ProductDto>`.

---

If you want, I can extend this page with sequence diagrams or example JSON payloads for each operation.