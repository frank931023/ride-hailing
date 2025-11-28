# 白癡 SA 作業

## 專案更新 (2025-11-28)
- **架構重整**：已將專案結構調整為標準 Maven 目錄結構 (`src/main/java/...`)。
- **容器化**：新增 `Dockerfile` 與 `docker-compose.yml`，支援一鍵啟動。
- **依賴修正**：修復 `pom.xml` 依賴與 Plugin 設定，解決編譯錯誤。
- **功能修復**：修正 Model 缺漏與語法錯誤，Web 服務已可正常存取。

## 啟動方式 (推薦)
本專案已支援 Docker Compose，無需手動安裝 Java 或 Maven 環境。

1. 確保已安裝 **Docker** 與 **Docker Compose**。
2. 在專案根目錄執行：
   ```bash
   docker-compose up --build
   ```
3. 等待啟動完成後，開啟瀏覽器存取：
   - 首頁：http://localhost:8080

## 傳統啟動方式 (不推薦)
若仍需使用本機 Maven 啟動：
1. `java -version` (需 JDK 17+)
2. `mvn -v`
3. `mvn spring-boot:run`

## 專案結構
- `src/main/java`: 後端原始碼 (Controller, Model, Service)
- `src/main/resources`: 設定檔與靜態資源 (HTML)
- `docker-compose.yml`: 容器編排設定
